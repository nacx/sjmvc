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

package org.sjmvc.util;

import static org.sjmvc.util.ReflectionUtils.fromString;
import static org.sjmvc.util.ReflectionUtils.getFieldArrayType;
import static org.sjmvc.util.ReflectionUtils.getFieldCollectionType;
import static org.sjmvc.util.ReflectionUtils.getFieldType;
import static org.sjmvc.util.ReflectionUtils.getProperty;
import static org.sjmvc.util.ReflectionUtils.setValue;
import static org.sjmvc.util.ReflectionUtils.transformAndSet;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.sjmvc.NestedTestPojo;
import org.sjmvc.TestPojo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link ReflectionUtils} class.
 * 
 * @author Ignasi Barrera
 */
public class ReflectionUtilsTest
{
    /** The target objetc used in tests. */
    private TestPojo target;

    @BeforeMethod
    public void setUp()
    {
        target = new TestPojo();
        target.setRequiredFields();
    }

    @Test
    public void testGetProperty() throws Exception
    {
        String notNullField = (String) getProperty(target, "stringProperty");
        assertNotNull(notNullField);
        assertEquals(notNullField, target.getStringProperty());

        String[] nullField = (String[]) getProperty(target, "stringArray");
        assertNull(nullField);
    }

    @Test(expectedExceptions = NoSuchFieldException.class)
    public void testInvalidGetProperty() throws Exception
    {
        getProperty(target, "unexistingProperty");
    }

    @Test
    public void testSetValue() throws Exception
    {
        setValue(target, "stringProperty", "test");
        setValue(target, "integerProperty", 5);
        setValue(target, "integerArray", null);

        assertEquals(target.getStringProperty(), "test");
        assertEquals(target.getIntegerProperty(), new Integer(5));
        assertEquals(target.getIntegerArray(), null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidSetValue() throws Exception
    {
        // Unexisting field exception is already tested in the
        // testInvalidGetProperty method
        setValue(target, "integerProperty", "13");
    }

    @Test
    public void testFromString() throws Exception
    {
        assertEquals(fromString(Object.class, null), null);
        assertEquals(fromString(String.class, "test"), "test");
        assertEquals(fromString(Integer.class, "5"), new Integer(5));
        assertEquals(fromString(Double.class, "5.6"), new Double(5.6));
        assertEquals(fromString(Float.class, "5.7"), new Float(5.7F));
        assertEquals(fromString(Long.class, "12345"), new Long(12345L));
        assertEquals(fromString(Short.class, "12"), new Short((short) 12));
        assertEquals(fromString(Boolean.class, "true"), Boolean.TRUE);
        assertEquals(fromString(Byte.class, "1"), new Byte((byte) 1));
        assertEquals(fromString(TestEnum.class, "VALUE"), TestEnum.VALUE);

    }

    @Test
    public void testInvalidFromString()
    {
        checkInvalidFromString(TestPojo.class, "value");
        checkInvalidFromString(Integer.class, "value");
        checkInvalidFromString(TestEnum.class, "value");
    }

    @Test
    public void testTransformAndSet() throws Exception
    {
        transformAndSet(target, "stringProperty", "test");
        transformAndSet(target, "integerProperty", "5");
        transformAndSet(target, "integerArray", null);

        assertEquals(target.getStringProperty(), "test");
        assertEquals(target.getIntegerProperty(), new Integer(5));
        assertEquals(target.getIntegerArray(), null);
    }

    @Test
    public void testInvalidTransformAndSet()
    {
        checkInvalidTransformAndSet(target, "unexistingProperty", "test");
        checkInvalidTransformAndSet(target, "integerProperty", "test");
        checkInvalidTransformAndSet(target, "nestedProperty", "test");
    }

    @Test
    public void testGetFieldType() throws Exception
    {
        assertEquals(getFieldType("stringProperty", TestPojo.class), String.class);
        assertEquals(getFieldType("integerProperty", TestPojo.class), Integer.class);
        assertEquals(getFieldType("nestedProperty", TestPojo.class), NestedTestPojo.class);

        // Unexisting field exception is already tested in the
        // testInvalidGetProperty method
    }

    @Test
    public void testGetFieldArrayType() throws Exception
    {
        assertEquals(getFieldArrayType("stringArray", TestPojo.class), String.class);
        assertEquals(getFieldArrayType("integerArray", TestPojo.class), Integer.class);

        // Not array fields
        assertEquals(getFieldArrayType("stringProperty", TestPojo.class), null);
        assertEquals(getFieldArrayType("integerProperty", TestPojo.class), null);
    }

    @Test
    public void testGetFieldCollectionType() throws Exception
    {
        assertEquals(getFieldCollectionType("stringList", TestPojo.class), String.class);
        assertEquals(getFieldCollectionType("integerList", TestPojo.class), Integer.class);
    }

    @Test
    public void testInvalidGetFieldCollectionType() throws Exception
    {
        checkInvalidGetFieldCollectionType("stringProperty", TestPojo.class);
        checkInvalidGetFieldCollectionType("integerProperty", TestPojo.class);
    }

    public void checkInvalidFromString(final Class< ? > clazz, final String value)
    {
        try
        {
            fromString(clazz, value);
            fail("fromString method should have failed");
        }
        catch (Exception ex)
        {
            // Do nothing. Test success.
        }
    }

    public void checkInvalidTransformAndSet(final Object target, final String name,
        final String value)
    {
        try
        {
            transformAndSet(target, name, value);
            fail("transformAndSet method should have failed");
        }
        catch (Exception ex)
        {
            // Do nothing. Test success.
        }
    }

    public void checkInvalidGetFieldCollectionType(final String name, final Class< ? > clazz)
        throws Exception
    {
        try
        {
            getFieldCollectionType(name, clazz);
            fail("getFieldCollectionType method should throw a ClassCastException");
        }
        catch (ClassCastException ex)
        {
            // Do nothing. Test success.
        }
    }

    private enum TestEnum
    {
        VALUE
    }
}
