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

package org.sjmvc.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.sjmvc.error.Errors;

/**
 * Validator implementation that validates objects based on the JPA annotations
 * of the target object.
 * 
 * @author Ignasi Barrera
 * 
 */
public class JPAValidator implements Validator
{

	@Override
	public Errors validate(Object target)
	{
		Errors errors = new Errors();

		// Perform the JPA validation
		Set<ConstraintViolation<Object>> validationErrors = Validation
				.buildDefaultValidatorFactory().getValidator().validate(target);

		// Get all error messages
		for (ConstraintViolation<Object> error : validationErrors)
		{
			errors.add(error.getMessage());
		}

		return errors;
	}
}
