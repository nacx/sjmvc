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

import java.io.Serializable;

import org.sjmvc.controller.Controller;
import org.sjmvc.web.dispatch.path.PathBasedRequestDispatcher;

/**
 * This class holds the resource mapping configuration such as the resource URI,
 * the controller class used to handle request to the mapped resource and the
 * layout to use when rendering the resolved view.
 * 
 * @author Ignasi Barrera
 * @see Controller
 * @see PathBasedRequestDispatcher
 */
public class ResourceMapping implements Serializable
{
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/** The {@link Controller} class. */
	private Class<Controller> controllerClass;

	/** The mapped resource path. */
	private String path;

	/**
	 * The layout to use to render the resolved views.
	 * <p>
	 * If this property is <code>null</code> no layout will be used.
	 */
	private String layout;

	// Getters and setters

	public Class<Controller> getControllerClass()
	{
		return controllerClass;
	}

	public void setControllerClass(Class<Controller> controllerClass)
	{
		this.controllerClass = controllerClass;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getLayout()
	{
		return layout;
	}

	public void setLayout(String layout)
	{
		this.layout = layout;
	}

}
