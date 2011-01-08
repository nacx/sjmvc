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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * Base class for all request parameter binding unit tests.
 * 
 * @author Ignasi Barrera
 * 
 * @param <T> The type of the object to bind.
 */
public class RequestBinderTestBase<T>
{
	/** The base path used for web requests. */
	private static final String BASE_PATH = "http://sjmvc.org/sjmvc/web";

	/** The binder to test. */
	protected RequestParameterBinderTestSupport<T> binder;

	/** The target of the binding. */
	protected T target;

	protected void initBinder(String... parameters) throws Exception
	{
		ServletRequest request = getRequest(parameters);
		binder = new RequestParameterBinderTestSupport<T>(target, request);
	}

	protected HttpServletRequest getRequest(String... parameters)
			throws Exception
	{
		ServletUnitClient sc = new ServletRunner().newClient();
		WebRequest request = new PostMethodWebRequest(BASE_PATH);
		Map<String, String[]> paramMap = buildParameterMap(parameters);

		for (Map.Entry<String, String[]> param : paramMap.entrySet())
		{
			request.setParameter(param.getKey(), param.getValue());
		}

		return sc.newInvocation(request).getRequest();
	}

	private Map<String, String[]> buildParameterMap(String... parameters)
	{
		Map<String, String[]> paramMap = new HashMap<String, String[]>();

		for (String parameter : parameters)
		{
			paramMap.put(RequestParameterBinder.BIND_PARAMETER_PREFIX
					+ parameter, new String[] { parameter });
		}

		return paramMap;
	}
}
