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
import java.util.LinkedList;
import java.util.List;

import org.sjmvc.controller.Controller;

/**
 * Generic error to be reported by a {@link Controller}.
 * 
 * @author Ignasi Barrera
 */
public class Errors implements Serializable
{
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/** The list of errors. */
	private List<Exception> errors = new LinkedList<Exception>();

	/**
	 * Adds the given error to the {@link #errors} list.
	 * 
	 * @param error The error to add.
	 */
	public void add(final String error)
	{
		errors.add(new Exception(error));
	}

	/**
	 * Adds the given error to the {@link #errors} list.
	 * 
	 * @param error The error to add.
	 * @param cause The error cause.
	 */
	public void add(final String error, final Exception cause)
	{
		errors.add(new Exception(error, cause));
	}

	/**
	 * Add to the current <code>Errors</code> object the errors in the given
	 * <code>Errors</code> object.
	 * 
	 * @param errors The errors to add.
	 */
	public void addAll(Errors errors)
	{
		this.errors.addAll(errors.getErrors());
	}

	/**
	 * Checks if there are any errors.
	 * 
	 * @return Boolean indicating if there are any errors.
	 */
	public boolean hasErrors()
	{
		return !errors.isEmpty();
	}

	/**
	 * Clear the list of errors.
	 */
	public void clear()
	{
		errors.clear();
	}

	/**
	 * Get the number of errors.
	 * 
	 * @return The number of errors.
	 */
	public int errorCount()
	{
		return errors.size();
	}

	// Getters and setters

	public List<Exception> getErrors()
	{
		return errors;
	}

}
