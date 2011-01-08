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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * Unit tests for the {@link RequestParameterBinder} class.
 * 
 * @author Ignasi Barrera
 */
public class RequestParameterBinderTest
{
	/** The base path used for web requests. */
	private static final String BASE_PATH = "http://sjmvc.org/sjmvc/web";

	/** The binder to test. */
	private RequestParameterBinderTestSupport binder;

	/** The target of the binding. */
	private BindPojo target;

	@BeforeMethod
	public void setUp() throws Exception
	{
		target = new BindPojo();
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
		// Build the request parameter map
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		for (String parameter : parameters)
		{
			paramMap.put("model." + parameter, new String[] { parameter });
		}

		// Create the binder
		initBinder(paramMap);

		// Execute binding and check results
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

	private void initBinder(Map<String, String[]> parameters) throws Exception
	{
		ServletRequest request = getRequest(parameters);
		binder = new RequestParameterBinderTestSupport(target, request);
	}

	private ServletRequest getRequest(Map<String, String[]> parameters)
			throws Exception
	{
		ServletUnitClient sc = new ServletRunner().newClient();
		WebRequest request = new PostMethodWebRequest(BASE_PATH);

		for (Map.Entry<String, String[]> param : parameters.entrySet())
		{
			request.setParameter(param.getKey(), param.getValue());
		}

		return sc.newInvocation(request).getRequest();
	}
}
