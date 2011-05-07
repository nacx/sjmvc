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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.sjmvc.TestPojo;
import org.sjmvc.binding.RequestBinderTestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AbstractController} class.
 * 
 * @author Ignasi Barrera
 * 
 */
public class AbstractControllerTest extends RequestBinderTestBase<TestPojo>
{
	/** The controller to test. */
	private MockController controller;

	@BeforeMethod
	public void setUp()
	{
		controller = new MockController();
		target = new TestPojo();
	}

	@Test
	public void testBind() throws Exception
	{
		HttpServletRequest request = getRequest("stringProperty");
		controller.bind(target, request);

		assertFalse(controller.messages.getErrors().hasErrors());
		assertEquals(target.getStringProperty(), "stringProperty");
	}

	@Test
	public void testInvalidBind() throws Exception
	{
		HttpServletRequest request = getRequest("unexistingProperty");
		controller.bind(target, request);

		assertTrue(controller.messages.getErrors().hasErrors());
		assertEquals(controller.messages.getErrors().errorCount(), 1);
	}

	@Test
	public void testValidate() throws Exception
	{
		target.setRequiredFields();
		controller.validate(target);

		assertFalse(controller.messages.getErrors().hasErrors());
	}

	@Test
	public void testInvalidValidate() throws Exception
	{
		controller.validate(target);

		assertTrue(controller.messages.getErrors().hasErrors());
		assertEquals(controller.messages.getErrors().errorCount(), 2);
	}

	@Test
	public void testBindAndValidateInvalidBinding() throws Exception
	{
		HttpServletRequest request = getRequest("unexistingProperty");
		controller.bindAndValidate(target, request);

		// Validation should not be executed,
		// only the binding error should be present
		assertTrue(controller.messages.getErrors().hasErrors());
		assertEquals(controller.messages.getErrors().errorCount(), 1);
	}

	@Test
	public void testBindAndValidateInvalidValidation() throws Exception
	{
		HttpServletRequest request = getRequest("stringProperty");
		controller.bindAndValidate(target, request);

		// Binding should succeed and validation fail
		assertTrue(controller.messages.getErrors().hasErrors());
		assertEquals(controller.messages.getErrors().errorCount(), 2);
	}

	@Test
	public void testBindAndValidate() throws Exception
	{
		// Set required fields except one. Binding will set it
		target.setRequiredFields();
		target.getNestedProperty().setStringProperty(null);

		// Bind the remaining required property
		HttpServletRequest request = getRequest("nestedProperty.stringProperty");

		controller.bindAndValidate(target, request);

		// Binding should succeed and validation fail
		assertFalse(controller.messages.getErrors().hasErrors());
	}
}
