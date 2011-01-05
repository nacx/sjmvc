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

package org.sjmvc.binding;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Array;
import java.util.Collection;

import org.sjmvc.util.ReflectionUtils;

/**
 * Support methods for Binding unit tests.
 * 
 * @author Ignasi Barrera
 * 
 * @param <T> The type of the target object used in tests.
 * 
 */
public class BindingTestSupport<T>
{
	/** The binder to test. */
	protected AbstractBinder<T, Object> binder;

	/** The target of the binding. */
	protected T target;

	protected void checkSetValue(String propertyName, String value)
			throws Exception
	{
		binder.setValue(target, propertyName, new String[] { value });

		Object setValue = ReflectionUtils.getProperty(target, propertyName);
		Class<?> type = ReflectionUtils.getFieldType(propertyName,
				target.getClass());

		assertEquals(setValue, ReflectionUtils.fromString(type, value));
	}

	protected void checkInvalidSetValue(String propertyName, String value)
			throws Exception
	{
		try
		{
			checkSetValue(propertyName, value);
			fail("Binding of [" + propertyName + "] should fail");
		}
		catch (BindingError ex)
		{
			// Test success
		}
	}

	protected void checkSetCollectionValues(String propertyName,
			String... values) throws Exception
	{
		binder.setValue(target, propertyName, values);

		Collection<?> setValue = (Collection<?>) ReflectionUtils.getProperty(
				target, propertyName);
		Class<?> type = ReflectionUtils.getFieldCollectionType(propertyName,
				target.getClass());

		assertEquals(setValue.size(), values.length);

		int i = 0;
		for (Object elem : setValue)
		{
			Object value = ReflectionUtils.fromString(type, values[i++]);
			assertEquals(elem, value);
		}
	}

	protected void checkInvalidSetCollectionValues(String propertyName,
			String... values) throws Exception
	{
		try
		{
			checkSetCollectionValues(propertyName, values);
			fail("Binding of [" + propertyName + "] should fail");
		}
		catch (BindingError ex)
		{
			// Test success
		}
	}

	protected void checkSetArrayValues(String propertyName, String... values)
			throws Exception
	{
		binder.setValue(target, propertyName, values);

		Object setValue = ReflectionUtils.getProperty(target, propertyName);
		Class<?> type = ReflectionUtils.getFieldArrayType(propertyName,
				target.getClass());

		assertEquals(Array.getLength(setValue), values.length);

		for (int i = 0; i < values.length; i++)
		{
			Object value = ReflectionUtils.fromString(type, values[i]);
			assertEquals(Array.get(setValue, i), value);
		}
	}

	protected void checkInvalidSetArrayValues(String propertyName,
			String... values) throws Exception
	{
		try
		{
			checkSetArrayValues(propertyName, values);
			fail("Binding of [" + propertyName + "] should fail");
		}
		catch (BindingError ex)
		{
			// Test success
		}
	}

	protected void checkBindSimpleField(String propertyName, String... values)
			throws Exception
	{
		binder.bindField(target, propertyName, values);

		Object setValue = ReflectionUtils.getProperty(target, propertyName);
		Class<?> type = ReflectionUtils.getFieldType(propertyName,
				target.getClass());

		assertEquals(setValue, ReflectionUtils.fromString(type, values[0]));
	}

	protected void checkInvalidBindSimpleField(String propertyName,
			String... values) throws Exception
	{
		binder.bindField(target, propertyName, values);
		assertTrue(binder.errors.hasErrors());
	}
}
