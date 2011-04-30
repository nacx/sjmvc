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

import static org.sjmvc.config.Configuration.CONTROLLER_PATH_SUFFIX;
import static org.sjmvc.config.Configuration.CONTROLLER_PREFIX;
import static org.sjmvc.config.Configuration.DEFAULT_PATH_MATCHER;
import static org.sjmvc.config.Configuration.PATH_MATCHER_PROPERTY;
import static org.sjmvc.config.Configuration.getConfigValue;
import static org.sjmvc.config.Configuration.getConfiguration;
import static org.sjmvc.config.Configuration.getPathMatcherClass;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Properties;

import org.sjmvc.web.dispatch.path.RegExpPathMatcher;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Configuration} class.
 * 
 * @author Ignasi Barrera
 */
public class ConfigurationTest
{
	@AfterMethod
	public void tearDown()
	{
		// Reset the configuration to the default values in the test
		// configuration file
		getConfiguration().remove(PATH_MATCHER_PROPERTY);
	}

	@Test
	public void testIsControllerPathProperty()
	{
		assertTrue(Configuration.isControllerPathProperty(CONTROLLER_PREFIX
				+ CONTROLLER_PATH_SUFFIX));
		assertTrue(Configuration.isControllerPathProperty(CONTROLLER_PREFIX
				+ " " + CONTROLLER_PATH_SUFFIX));
		assertTrue(Configuration.isControllerPathProperty(CONTROLLER_PREFIX
				+ "test" + CONTROLLER_PATH_SUFFIX));
		assertTrue(Configuration.isControllerPathProperty(CONTROLLER_PREFIX
				+ "." + CONTROLLER_PATH_SUFFIX));

		assertFalse(Configuration.isControllerPathProperty(""));
		assertFalse(Configuration.isControllerPathProperty("test"));
		assertFalse(Configuration.isControllerPathProperty(CONTROLLER_PREFIX));
		assertFalse(Configuration
				.isControllerPathProperty(CONTROLLER_PATH_SUFFIX));
		assertFalse(Configuration.isControllerPathProperty(CONTROLLER_PREFIX
				+ "test"));
		assertFalse(Configuration.isControllerPathProperty("test"
				+ CONTROLLER_PATH_SUFFIX));
	}

	@Test
	public void testGetConfiguration()
	{
		Properties props = getConfiguration();

		assertNotNull(props);
		assertEquals(props.size(), 3);
	}

	@Test
	public void testGetConfigValue()
	{
		// Existing properties
		assertEquals(getConfigValue("sjmvc.controller.mock.path"), "/mock");
		assertEquals(getConfigValue("sjmvc.controller.mock.layout"),
				"layout.jsp");
		assertEquals(getConfigValue("sjmvc.controller.mock.class"),
				"org.sjmvc.controller.MockController");

		// Unexisting properties
		assertEquals(getConfigValue("sjmvc.unexisting"), null);
	}

	@Test
	public void testGetPathMatcher()
	{
		// Default Path Matcher
		assertEquals(getPathMatcherClass(), DEFAULT_PATH_MATCHER);

		// Configured Path Matcher
		getConfiguration().put(PATH_MATCHER_PROPERTY,
				RegExpPathMatcher.class.getName());
		assertEquals(getPathMatcherClass(), RegExpPathMatcher.class);
	}

	@Test(expectedExceptions = ConfigurationException.class)
	public void testGetPathMatcherUnexistingClass()
	{
		getConfiguration().put(PATH_MATCHER_PROPERTY,
				"org.sjmvc.UnexistingClass");
		getPathMatcherClass();
	}
}
