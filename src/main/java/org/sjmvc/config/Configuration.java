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

package org.sjmvc.config;

import java.util.Properties;

import org.sjmvc.controller.Controller;
import org.sjmvc.web.dispatch.path.AntPathMatcher;
import org.sjmvc.web.dispatch.path.PathMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global application configuration.
 * 
 * @author Ignasi Barrera
 */
public class Configuration
{
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Configuration.class);

	// View configuration

	/** The view path. */
	public static final String VIEW_PATH = "/jsp";

	/** The view suffix. */
	public static final String VIEW_SUFFIX = ".jsp";

	/** The layout path. */
	public static final String LAYOUT_PATH = VIEW_PATH + "/layout";

	/** The attribute name where the model will be published. */
	public static final String MODEL_ATTRIBUTE = "model";

	/** The attribute name where the controller errors will be published. */
	public static final String MESSAGES_ATTRIBUTE = "messages";

	/** The attribute name where the current layout will be published. */
	public static final String CURRENT_LAYOUT_ATTRIBUTE = "currentLayout";

	/** The attribute name where the current view will be published. */
	public static final String CURRENT_VIEW_ATTRIBUTE = "currentView";

	// Controller configuration

	/** The prefix for controller mapping properties. */
	public static final String CONTROLLER_PREFIX = "sjmvc.controller.";

	/** The suffix for controller path mapping properties. */
	public static final String CONTROLLER_PATH_SUFFIX = ".path";

	/** The suffix for controller layout mapping properties. */
	public static final String CONTROLLER_LAYOUT_SUFFIX = ".layout";

	/** The suffix for controller class mapping properties. */
	public static final String CONTROLLER_CLASS_SUFFIX = ".class";

	// Dipatcher configuration

	/** The suffix for controller class mapping properties. */
	public static final String PATH_MATCHER_PROPERTY = "sjmvc.path.matcher.class";

	/** The default {@link PathMatcher} to use if none is configured. */
	public static final Class<? extends PathMatcher> DEFAULT_PATH_MATCHER = AntPathMatcher.class;

	// Main configuration

	/** The main configuration file. */
	private static final String CONFIG_FILE = "sjmvc.properties";

	/** The singleton instance of the configuration object. */
	private static Configuration instance;

	/** The configuration properties. */
	private Properties properties;

	/**
	 * Private constructor. This class should ot be instantiated.
	 */
	private Configuration()
	{
		super();
	}

	/**
	 * Gets the configuration properties.
	 * 
	 * @return The configuration properties.
	 */
	public static Properties getConfiguration()
	{
		if (instance == null)
		{
			instance = new Configuration();

			LOGGER.debug("Loading configuration from {}", CONFIG_FILE);

			// Load properties
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			instance.properties = new Properties();

			try
			{
				instance.properties.load(cl.getResourceAsStream(CONFIG_FILE));
			}
			catch (Exception ex)
			{
				throw new ConfigurationException(
						"Could not load configuration file: " + ex.getMessage());
			}

			LOGGER.debug("Loaded {} configuration properties",
					instance.properties.size());
		}

		return instance.properties;
	}

	/**
	 * Get the configuration value for the given property name.
	 * 
	 * @return The value for the given property or <code>null</code> if the
	 *         value is not defined.
	 */
	public static String getConfigValue(final String propertyName)
	{
		return getConfiguration().getProperty(propertyName);
	}

	/**
	 * Checks if the given property defines a {@link Controller} path mapping.
	 * 
	 * @param property The property to check.
	 * @return Boolean indicating if the given property defines a
	 *         <code>Controller</code> mapping.
	 */
	public static boolean isControllerPathProperty(final String property)
	{
		return property.startsWith(CONTROLLER_PREFIX)
				&& property.endsWith(CONTROLLER_PATH_SUFFIX);
	}

	/**
	 * Get the configured path matcher class to use to resolve
	 * {@link Controller} URIs.
	 * 
	 * @return The configured
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends PathMatcher> getPathMatcherClass()
	{
		String configuredPathMatcher = getConfigValue(PATH_MATCHER_PROPERTY);

		if (configuredPathMatcher != null)
		{
			try
			{
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				return (Class<? extends PathMatcher>) Class.forName(
						configuredPathMatcher, true, cl);
			}
			catch (ClassNotFoundException ex)
			{
				throw new ConfigurationException(
						"Could not load configured path matcher class: "
								+ ex.getMessage());
			}
		}
		else
		{
			return DEFAULT_PATH_MATCHER;
		}
	}
}
