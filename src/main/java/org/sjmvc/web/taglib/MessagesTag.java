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

package org.sjmvc.web.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.sjmvc.config.Configuration;
import org.sjmvc.controller.Controller;
import org.sjmvc.controller.Messages;
import org.sjmvc.error.Error;
import org.sjmvc.error.Errors;

/**
 * Tag library used to render {@link Controller} messages.
 * 
 * @author Ignasi Barrera
 */
public class MessagesTag extends TagSupport
{
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException
	{
		Messages messages = (Messages) pageContext.getRequest().getAttribute(
				Configuration.MESSAGES_ATTRIBUTE);

		// If AbstractController has not been executed, errors may be null
		if (messages != null)
		{
			StringBuffer output = new StringBuffer();

			printErrors(output, messages.getErrors());
			printMessages(output, messages.getMessages());

			try
			{
				pageContext.getOut().write(output.toString());
			}
			catch (IOException ex)
			{
				throw new JspException("Could not write errors", ex);
			}
		}

		return TagSupport.SKIP_BODY;
	}

	/**
	 * Print the given errors.
	 * 
	 * @param output The buffer where the errors will be written.
	 * @param errors The errors to print.
	 */
	private void printErrors(StringBuffer output, Errors errors)
	{
		output.append("<div class=\"errors\">");
		output.append("<ul>");

		for (Error error : errors.getErrors())
		{
			output.append("<li>");
			output.append(error.toString());
			output.append("</li>");
		}

		output.append("</ul>");
		output.append("</div>");
	}

	/**
	 * Print the given messages.
	 * 
	 * @param output The buffer where the messages will be written.
	 * @param messages The messages to print.
	 */
	private void printMessages(StringBuffer output, List<String> messages)
	{
		output.append("<div class=\"messages\">");
		output.append("<ul>");

		for (String message : messages)
		{
			output.append("<li>");
			output.append(message);
			output.append("</li>");
		}

		output.append("</ul>");
		output.append("</div>");
	}
}
