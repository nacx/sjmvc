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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Utility response wrapper to expose the HTTP status of the response.
 * 
 * @author Ignasi Barrera
 * 
 */
public class StatusExposingResponseWrapper extends HttpServletResponseWrapper
{
	/** The response status. By default 200. */
	private int httpStatus = HttpServletResponse.SC_OK;

	/**
	 * Creates the wrapper.
	 * 
	 * @param response The response to wrap.
	 */
	public StatusExposingResponseWrapper(HttpServletResponse response)
	{
		super(response);
	}

	/**
	 * Checks if the response status is an OK status.
	 * 
	 * @return Boolean indicating if the response status is an OK status.
	 */
	public boolean isOk()
	{
		return httpStatus >= HttpServletResponse.SC_OK
				&& httpStatus < HttpServletResponse.SC_MULTIPLE_CHOICES;
	}

	@Override
	public void sendError(int sc) throws IOException
	{
		httpStatus = sc;
		super.sendError(sc);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException
	{
		httpStatus = sc;
		super.sendError(sc, msg);
	}

	@Override
	public void sendRedirect(String location) throws IOException
	{
		httpStatus = HttpServletResponse.SC_FOUND;
		super.sendRedirect(location);
	}

	// Getters and setters

	@Override
	public void setStatus(int sc)
	{
		httpStatus = sc;
		super.setStatus(sc);
	}

	public int getStatus()
	{
		return httpStatus;
	}

}
