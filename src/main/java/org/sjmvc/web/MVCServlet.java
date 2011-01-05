/**
 * Copyright (c) 2010 Ignasi Barrera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.sjmvc.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sjmvc.config.Configuration;
import org.sjmvc.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process requests performed from the Web UI.
 * 
 * @author Ignasi Barrera
 * 
 * @see Controller
 */
public class MVCServlet extends HttpServlet
{
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MVCServlet.class);

	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/** The view path. */
	private static final String VIEW_PATH = "/jsp";

	/** The view suffix. */
	private static final String VIEW_SUFFIX = ".jsp";

	/** The layout path. */
	private static final String LAYOUT_PATH = VIEW_PATH + "/layout";

	/** Mappings from request path to {@link Controller} class objects. */
	protected Map<String, Class<Controller>> controllerClassess;

	/** The main layout file to use in the application. */
	protected String layout;

	/**
	 * Default constructor.
	 */
	public MVCServlet()
	{
		controllerClassess = new HashMap<String, Class<Controller>>();
	}

	/**
	 * Initializes the servlet.
	 * 
	 * @throws If the servlet cannot be initialized.
	 */
	@Override
	public void init() throws ServletException
	{
		try
		{
			readConfiguration();
		}
		catch (Exception ex)
		{
			throw new ServletException("Could read MVC configuration: "
					+ ex.getMessage(), ex);
		}

	}

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException
	{
		String controllerPath = null;
		Class<Controller> controllerClass = null;
		String requestedPath = getRequestedPath(req);

		for (String path : controllerClassess.keySet())
		{
			if (requestedPath.startsWith(path))
			{
				controllerPath = path;
				controllerClass = controllerClassess.get(path);

				LOGGER.debug("Using {} controller to handle request to: {}",
						controllerClass.getName(), req.getRequestURI());
			}
		}

		if (controllerClass != null)
		{
			try
			{
				// Instantiate the controller on each request to make it
				// thread-safe
				Controller controller = controllerClass.newInstance();
				String viewName = controller.execute(req, resp);
				String viewPath = VIEW_PATH + controllerPath + "/" + viewName
						+ VIEW_SUFFIX;

				req.setAttribute("currentView", viewPath);
				getServletContext().getRequestDispatcher(layout).forward(req,
						resp);
			}
			catch (Exception ex)
			{
				String errorMessage = "An error occured during request handling: "
						+ ex.getMessage();

				LOGGER.error(errorMessage, ex);

				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						errorMessage);
			}
		}
		else
		{
			resp.sendError(
					HttpServletResponse.SC_NOT_FOUND,
					"No controller was found to handle request to: "
							+ req.getRequestURI());
		}
	}

	@Override
	protected void doPost(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException
	{
		doGet(req, resp);
	}

	/**
	 * Get the requested path relative to the servlet path.
	 * 
	 * @param req The request.
	 * @return The requested path.
	 */
	private String getRequestedPath(final HttpServletRequest req)
	{
		return req.getRequestURI().replaceFirst(req.getContextPath(), "")
				.replaceFirst(req.getServletPath(), "");
	}

	/**
	 * Load configured controller mappings.
	 * 
	 * @throws Exception If mappings cannot be loaded.
	 */
	protected void readConfiguration() throws Exception
	{
		Properties config = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		config.load(cl.getResourceAsStream(Configuration.CONFIG_FILE));

		LOGGER.info("Loading controller mappings...");

		for (Object mappingKey : config.keySet())
		{
			String key = (String) mappingKey;

			if (Configuration.isControllerProperty(key))
			{
				String path = config.getProperty(key);
				String clazz = config.getProperty(key.replace(
						Configuration.CONTROLLER_PATH_SUFFIX,
						Configuration.CONTROLLER_CLASS_SUFFIX));

				if (clazz == null)
				{
					throw new Exception("Missing controller class for path: "
							+ path);
				}

				@SuppressWarnings("unchecked")
				Class<Controller> controllerClass = (Class<Controller>) Class
						.forName(clazz, true, cl);

				controllerClassess.put(path, controllerClass);

				LOGGER.info("Mapping {} to {}", path, controllerClass
						.getClass().getName());
			}
		}

		layout = config.getProperty(Configuration.LAYOUT_PROPERTY);

		if (layout == null)
		{
			throw new Exception("You must set the main layout file");
		}

		layout = LAYOUT_PATH + "/" + layout;

		LOGGER.info("Using {} as the main layout", layout);
	}
}
