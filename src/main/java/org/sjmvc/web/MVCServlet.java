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
 * @see Controller
 * @see RequestDispatcher
 * @see PathBasedRequestDispatcher
 */
public class MVCServlet extends HttpServlet
{
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MVCServlet.class);

    /** Serial UID. */
    private static final long serialVersionUID = 1L;

    /** The request dispatcher used to dispatch requests to {@link Controller}. */
    private RequestDispatcher dispatcher;

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
            dispatcher = new PathBasedRequestDispatcher();
        }
        catch (Exception ex)
        {
            throw new ServletException("Could not read MVC configuration: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        try
        {
            StatusExposingResponseWrapper response = new StatusExposingResponseWrapper(resp);
            dispatcher.dispatch(req, response);
            
            // Only forward if no errors have been committed to the response
            if (response.isOk() && !response.isCommitted())
            {
                String currentLayout = (String) req.getAttribute(Configuration.CURRENT_LAYOUT_ATTRIBUTE);
                String currentView = (String) req.getAttribute(Configuration.CURRENT_VIEW_ATTRIBUTE);

                String viewToRender = (currentLayout != null)? currentLayout : currentView;                
                getServletContext().getRequestDispatcher(viewToRender).forward(req, response);
            }
        }
        catch (Exception ex)
        {
            String errorMessage = "An error occured during request handling: " + ex.getMessage();

            LOGGER.error(errorMessage, ex);

            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
        }
    }
}
