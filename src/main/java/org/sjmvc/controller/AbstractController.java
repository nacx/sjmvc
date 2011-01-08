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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sjmvc.binding.BindingResult;
import org.sjmvc.binding.RequestParameterBinder;
import org.sjmvc.error.Errors;
import org.sjmvc.validation.JPAValidator;
import org.sjmvc.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link Controller} implementations.
 * 
 * @author Ignasi Barrera
 */
public abstract class AbstractController implements Controller
{
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractController.class);

	/** The attribute name where the model will be published. */
	public static final String MODEL_ATTRIBUTE = "model";

	/** The attribute name where the controller errors will be published. */
	public static final String ERRORS_ATTRIBUTE = "errors";

	/** The validator used to validate model objects. */
	private Validator validator;

	/** List of errors produced during method execution. */
	private Errors errors = new Errors();

	/** The view to return. */
	private String returnView;

	/** The class of the model object. */
	private Class<?> modelClass;

	/** The model objects used to render the view. */
	// private Map<String, Object> model = new HashMap<String, Object>();

	/**
	 * Creates a new <code>AbstractController</code>
	 */
	public AbstractController()
	{
		super();
		errors = new Errors();
		validator = new JPAValidator();
	}

	/**
	 * Internal method to implement the controller logic.
	 * <p>
	 * This method returns void and expects this method to set the view to
	 * render by calling the {@link #setView(String)} method.
	 * <p>
	 * The model must be also populated using the
	 * {@link #addModel(String, Object)} method in order to let the views render
	 * properly.
	 * 
	 * @param request The request.
	 * @param response The response.
	 * @throws Exception If an exception occurs during controller logic
	 *             execution.
	 */
	protected abstract void doExecute(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception;

	@Override
	public String execute(final HttpServletRequest request,
			final HttpServletResponse response) throws ControllerException
	{
		// Clear previous data and create a new model object
		errors.clear();
		returnView = null;
		Object model = createModel();

		// Bind and validate input data
		if (model != null)
		{
			bindAndValidate(model, request);
		}

		try
		{
			// Execute controller logic
			doExecute(request, response);

			if (returnView == null)
			{
				throw new Exception("There was no view set. "
						+ "Use the setView method to set the view to render");
			}
		}
		catch (Exception ex)
		{
			if (ex.getCause() instanceof ControllerException)
			{
				// If it is a Controller exception, just propagate it
				throw (ControllerException) ex.getCause();
			}

			throw new ControllerException(
					"Could not execute the Controller logic at "
							+ this.getClass().getName(),
					ex.getCause() == null ? ex : ex.getCause());
		}

		// Populate model and errors
		if (modelClass != null)
		{
			request.setAttribute(MODEL_ATTRIBUTE, model);
		}

		request.setAttribute(ERRORS_ATTRIBUTE, errors);

		return returnView;
	}

	/**
	 * Binds the request parameters to the model object and validates it.
	 * 
	 * @param model The model object where to bind the request parameters.
	 * @param request The request containing the input parameters.
	 */
	protected void bindAndValidate(Object model, HttpServletRequest request)
	{
		// Bind request parameters to model object
		RequestParameterBinder<Object> binder = new RequestParameterBinder<Object>(
				model, request);
		BindingResult<Object> bindingErrors = binder.bind();
		errors.addAll(bindingErrors.getErrors());

		// Only validate if there are no binding errors
		if (!errors())
		{
			errors.addAll(validator.validate(model));
		}
	}

	/**
	 * Adds the given error to the {@link #errors} list.
	 * 
	 * @param error The error to add.
	 */
	protected void error(final String error)
	{
		errors.add(error);
	}

	/**
	 * Adds the given error to the {@link #errors} list.
	 * 
	 * @param error The error to add.
	 * @param cause The error cause.
	 */
	protected void error(final String error, final Exception cause)
	{
		errors.add(error);
	}

	/**
	 * Checks if there are any errors.
	 * 
	 * @return Boolean indicating if there are any errors.
	 */
	protected boolean errors()
	{
		return errors.hasErrors();
	}

	/**
	 * Set the view to be returned.
	 * 
	 * @param viewName The name of the view to be returned.
	 */
	protected void setView(String viewName)
	{
		returnView = viewName;
	}

	/**
	 * Get the view returned by the controller.
	 * 
	 * @return The name of the view returned by the controller.
	 */
	protected String getView()
	{
		return returnView;
	}

	/**
	 * Sets the model class that will be used by the controller.
	 * 
	 * @param modelClass The class of the model object.
	 */
	protected void setModelClass(Class<?> modelClass)
	{
		this.modelClass = modelClass;
	}

	/**
	 * Creates the model object defined in the {@link #modelClass} property.
	 * 
	 * @return The model object or <code>null</code> if the
	 *         <code>modelClass</code> has not been defined.
	 * @throws ControllerException If the model oject cannot be created.
	 */
	private Object createModel() throws ControllerException
	{
		if (modelClass != null)
		{
			try
			{
				return modelClass.newInstance();
			}
			catch (Exception ex)
			{
				throw new ControllerException(
						"Could not instantiate the model. "
								+ "Does the model class have a default constructor?");
			}
		}
		else
		{
			LOGGER.warn("The modelClass property has not been set. "
					+ "Binding and validation will be disabled.");

			return null;
		}
	}

}
