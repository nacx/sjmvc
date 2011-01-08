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

package org.sjmvc.error;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * The generic error object.
 * 
 * @author Ignasi Barrera
 * 
 */
public class Error implements Serializable
{
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/** The type of the error. */
	private ErrorType type;

	/** The error message. */
	private String message;

	/**
	 * Creates a new error.
	 * 
	 * @param type The type of the error.
	 * @param message The error details.
	 */
	public Error(ErrorType type, String message)
	{
		super();
		this.type = type;
		this.message = message;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(StringUtils.capitalize(type.name().toLowerCase()));
		sb.append(" error: ");
		sb.append(message);

		return sb.toString();
	}

	// Getters and setters

	public ErrorType getType()
	{
		return type;
	}

	public void setType(ErrorType type)
	{
		this.type = type;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
