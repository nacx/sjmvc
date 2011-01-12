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
import org.sjmvc.error.Error;

/**
 * Tag library used to render {@link Controller} errors.
 * 
 * @author Ignasi Barrera
 */
public class ErrorsTag extends TagSupport
{

    /** Serial UID. */
    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException
    {
        @SuppressWarnings("unchecked")
        List<Error> errors =
            (List<Error>) pageContext.getRequest().getAttribute(Configuration.ERRORS_ATTRIBUTE);

        // If AbstractController has not been executed, errors may be null
        if (errors != null)
        {
            StringBuffer output = new StringBuffer();

            output.append("<div class=\"errors\">");
            output.append("<ul>");

            for (Error error : errors)
            {
                output.append("<li>");
                output.append(error.toString());
                output.append("</li>");
            }

            output.append("</ul>");
            output.append("</div>");

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
}
