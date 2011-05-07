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

package org.sjmvc.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.sjmvc.error.Errors;

/**
 * Messages to be shown in the views.
 * <p>
 * This class contains the info messages and the list of errors to be populated
 * to the views.
 * 
 * @author Ignasi Barrera
 * 
 * @see Errors
 */
public class Messages implements Serializable
{
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/** The list of messages. */
	private List<String> messages = new LinkedList<String>();

	/** The list of errors. */
	private Errors errors = new Errors();

	/**
	 * Add the given message to the message list.
	 * 
	 * @param message THe message to add.
	 */
	public void add(String message)
	{
		messages.add(message);
	}

	/**
	 * Checks if there are any messages.
	 * 
	 * @return Boolean indicating if there are any messages.
	 */
	public boolean hasMessages()
	{
		return !messages.isEmpty();
	}

	/**
	 * Clear the list of messages.
	 */
	public void clear()
	{
		messages.clear();
		errors.clear();
	}

	/**
	 * Get the number of messages.
	 * 
	 * @return The number of messages.
	 */
	public int messageCount()
	{
		return messages.size();
	}

	// Getters and setters

	public List<String> getMessages()
	{
		return messages;
	}

	public void setMessages(List<String> messages)
	{
		this.messages = messages;
	}

	public Errors getErrors()
	{
		return errors;
	}

}
