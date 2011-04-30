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

package org.sjmvc.web.dispatch.path;

import static org.sjmvc.config.Configuration.DEFAULT_PATH_MATCHER;
import static org.sjmvc.config.Configuration.PATH_MATCHER_PROPERTY;
import static org.sjmvc.config.Configuration.getConfiguration;
import static org.testng.Assert.assertEquals;

import org.sjmvc.config.ConfigurationException;
import org.sjmvc.controller.MockController;
import org.sjmvc.web.ResourceMapping;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link PathBasedRequestDispatcher} class.
 * 
 * @author Ignasi Barrera
 * 
 */
public class PathBasedRequestDispatcherTest
{
	// Request handling unit testing is covered in the MVCServletTest class

	@AfterMethod
	public void tearDown()
	{
		// Reset the configuration to the default values in the test
		// configuration file
		getConfiguration().remove(PATH_MATCHER_PROPERTY);
	}

	@Test
	public void testLoadControllerMappings()
	{
		PathBasedRequestDispatcher dispatcher = new PathBasedRequestDispatcher();

		assertEquals(dispatcher.mappings.size(), 1);

		ResourceMapping mapping = dispatcher.mappings.get("/mock");

		assertEquals(mapping.getPath(), "/mock");
		assertEquals(mapping.getLayout(), "layout.jsp");
		assertEquals(mapping.getControllerClass(), MockController.class);
	}

	@Test
	public void testLoadDefaultPathMatcher()
	{
		PathBasedRequestDispatcher dispatcher = new PathBasedRequestDispatcher();
		assertEquals(dispatcher.pathMatcher.getClass(), DEFAULT_PATH_MATCHER);
	}

	@Test
	public void testLoadConfiguredPathMatcher()
	{
		getConfiguration().put(PATH_MATCHER_PROPERTY,
				RegExpPathMatcher.class.getName());
		PathBasedRequestDispatcher dispatcher = new PathBasedRequestDispatcher();
		assertEquals(dispatcher.pathMatcher.getClass(), RegExpPathMatcher.class);
	}

	@Test(expectedExceptions = ConfigurationException.class)
	public void testLoadInvalidPathMatcher()
	{
		getConfiguration().put(PATH_MATCHER_PROPERTY,
				"org.sjmvc.UnexistingClass");
		new PathBasedRequestDispatcher();
	}
}
