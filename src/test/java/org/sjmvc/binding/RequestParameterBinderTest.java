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

package org.sjmvc.binding;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sjmvc.TestPojo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link RequestParameterBinder} class.
 * 
 * @author Ignasi Barrera
 */
public class RequestParameterBinderTest extends RequestBinderTestBase<TestPojo>
{

	@BeforeMethod
	public void setUp() throws Exception
	{
		target = new TestPojo();
	}

	@Test
	public void testDoBind() throws Exception
	{
		checkDoBind(new String[] {});
		checkDoBind("stringProperty");
		checkDoBind("stringProperty", "integerProperty");
		checkDoBind("stringList", "integerList");
		checkDoBind("stringList", "integerProperty",
				"nestedProperty.stringProperty");
	}

	private void checkDoBind(String... parameters) throws Exception
	{
		// Build the request parameter map and bind to the model object
		initBinder(parameters);
		binder.doBind();

		assertEquals(parameters.length, binder.parameters.size());

		List<String> paramList = Arrays.asList(parameters);
		Collections.sort(paramList, String.CASE_INSENSITIVE_ORDER);
		Collections.sort(binder.parameters, String.CASE_INSENSITIVE_ORDER);

		int i = 0;
		for (String param : paramList)
		{
			assertEquals(param, binder.parameters.get(i++));
		}
	}
}
