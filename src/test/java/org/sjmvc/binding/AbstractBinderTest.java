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
import static org.testng.Assert.fail;

import java.lang.reflect.Array;
import java.util.Collection;

import org.sjmvc.util.ReflectionUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link AbstractBinderBinder} class.
 * 
 * @author Ignasi Barrera
 */
public class AbstractBinderTest
{
    /** The binder to test. */
    private AbstractBinder<BindPojo, Object> binder;

    /** The target of the binding. */
    private BindPojo target;

    @BeforeMethod
    public void setUp()
    {
        target = new BindPojo();
        binder = new AbstractBinder<BindPojo, Object>(target, null)
        {
            @Override
            protected void doBind()
            {
                // Do nothing. Not used in this test.
            }
        };
    }

    @Test
    public void testSetSimpleValue() throws Exception
    {
        // Strings
        checkSetValue("stringProperty", null);
        checkSetValue("stringProperty", "");
        checkSetValue("stringProperty", "test");

        // Numbers
        checkInvalidSetValue("integerProperty", null);
        checkInvalidSetValue("integerProperty", "");
        checkInvalidSetValue("integerProperty", "test");
        checkSetValue("integerProperty", "-1");
        checkSetValue("integerProperty", "0");
        checkSetValue("integerProperty", "17");
    }

    @Test
    public void testSetCollectionValue() throws Exception
    {
        // Strings
        checkSetCollectionValues("stringList", new String[] {null});
        checkSetCollectionValues("stringList", "");
        checkSetCollectionValues("stringList", null, null);
        checkSetCollectionValues("stringList", "elem1", "elem2", "elem3");

        // Numbers
        checkInvalidSetCollectionValues("integerList", new String[] {null});
        checkInvalidSetCollectionValues("integerList", "");
        checkInvalidSetCollectionValues("integerList", "test");
        checkSetCollectionValues("integerList", "17");
        checkSetCollectionValues("integerList", "-1", "0", "17");
    }

    @Test
    public void testSetArrayValue() throws Exception
    {
        // Strings
        checkSetArrayValues("stringArray", new String[] {null});
        checkSetArrayValues("stringArray", "");
        checkSetArrayValues("stringArray", null, null);
        checkSetArrayValues("stringArray", "elem1", "elem2", "elem3");

        // Numbers
        checkInvalidSetArrayValues("integerArray", new String[] {null});
        checkInvalidSetArrayValues("integerArray", "");
        checkInvalidSetArrayValues("integerArray", "test");
        checkSetArrayValues("integerArray", "17");
        checkSetArrayValues("integerArray", "-1", "0", "17");
    }

    @Test
    public void testBindField()
    {
        // Simple
    }

    // Helper methods

    private <T> void checkSetValue(String propertyName, String value) throws Exception
    {
        binder.setValue(target, propertyName, new String[] {value});

        Object setValue = ReflectionUtils.getProperty(target, propertyName);
        Class< ? > type = ReflectionUtils.getFieldType(propertyName, target.getClass());

        assertEquals(setValue, ReflectionUtils.fromString(type, value));
    }

    private void checkInvalidSetValue(String propertyName, String value) throws Exception
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

    private <T> void checkSetCollectionValues(String propertyName, String... values)
        throws Exception
    {
        binder.setValue(target, propertyName, values);

        Collection< ? > setValue =
            (Collection< ? >) ReflectionUtils.getProperty(target, propertyName);
        Class< ? > type = ReflectionUtils.getFieldCollectionType(propertyName, target.getClass());

        assertEquals(setValue.size(), values.length);

        int i = 0;
        for (Object elem : setValue)
        {
            Object value = ReflectionUtils.fromString(type, values[i++]);
            assertEquals(elem, value);
        }
    }

    private void checkInvalidSetCollectionValues(String propertyName, String... values)
        throws Exception
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

    private <T> void checkSetArrayValues(String propertyName, String... values) throws Exception
    {
        binder.setValue(target, propertyName, values);

        Object setValue = ReflectionUtils.getProperty(target, propertyName);
        Class< ? > type = ReflectionUtils.getFieldArrayType(propertyName, target.getClass());

        assertEquals(Array.getLength(setValue), values.length);

        for (int i = 0; i < values.length; i++)
        {
            Object value = ReflectionUtils.fromString(type, values[i]);
            assertEquals(Array.get(setValue, i), value);
        }
    }

    private void checkInvalidSetArrayValues(String propertyName, String... values) throws Exception
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
}
