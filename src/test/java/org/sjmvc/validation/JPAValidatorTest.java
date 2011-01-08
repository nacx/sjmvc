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

import static org.testng.Assert.assertEquals;

import org.sjmvc.TestPojo;
import org.sjmvc.error.Errors;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link JPAValidator} class.
 * 
 * @author Ignasi Barrera
 * 
 */
public class JPAValidatorTest
{
	/** The v alidator to test. */
	private JPAValidator validator;

	/** The target object to validate. */
	private TestPojo target;

	@BeforeMethod
	public void setUp()
	{
		validator = new JPAValidator();
		target = new TestPojo();
		target.setRequiredFields();
	}

	@Test
	public void testValidationOK()
	{
		checkValidation(0);
	}

	@Test
	public void testValidationFailure()
	{
		target.setStringProperty("abcdef");
		checkValidation(1);

		target.setIntegerProperty(0);
		checkValidation(2);

		target.setNestedProperty(null);
		checkValidation(3);
	}

	private void checkValidation(int numberOfErrors)
	{
		Errors errors = validator.validate(target);
		assertEquals(errors.errorCount(), numberOfErrors);
	}
}
