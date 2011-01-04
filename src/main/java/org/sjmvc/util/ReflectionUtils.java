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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * Utility method to perform reflection operations.
 * 
 * @author Ignasi Barrera
 */
public class ReflectionUtils
{
    /**
     * Get the value of the given property
     * 
     * @param target The object that has the field.
     * @param name The name of the property.
     * @return The value of the property.
     * @throws Exception If the value of the property cannot be retrieved.
     */
    public static Object getProperty(Object target, String name) throws Exception
    {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * Sets the value of the given property.
     * 
     * @param target The object that has the field.
     * @param name The name of the property.
     * @param value The value to set.
     * @throws Exception If the value of the property cannot be set.
     */
    public static void transformAndSet(Object target, String name, String value) throws Exception
    {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, fromString(field.getType(), value));
    }

    /**
     * Sets the value of the given property.
     * 
     * @param target The object that has the field.
     * @param name The name of the property.
     * @param value The value to set.
     * @throws Exception If the value of the property cannot be set.
     */
    public static void setValue(Object target, String name, Object value) throws Exception
    {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Convert the given String to the given class.
     * 
     * @param clazz The destination class.
     * @param value The String value to convert.
     * @return The converted value.
     * @throws Exception If the given value cannot be transformed.
     */
    public static Object fromString(Class< ? > clazz, String value) throws Exception
    {
        if (clazz.equals(String.class))
        {
            return value;
        }
        else if (clazz.equals(Integer.class))
        {
            return Integer.valueOf(value);
        }
        else if (clazz.equals(Double.class))
        {
            return Double.valueOf(value);
        }
        else if (clazz.equals(Float.class))
        {
            return Float.valueOf(value);
        }
        else if (clazz.equals(Boolean.class))
        {
            return Boolean.valueOf(value);
        }
        else if (clazz.equals(Byte.class))
        {
            return Byte.valueOf(value);
        }
        else if (clazz.equals(Long.class))
        {
            return Long.valueOf(value);
        }
        else if (clazz.equals(Short.class))
        {
            return Short.valueOf(value);
        }

        throw new Exception("Could not transform [" + value + "] to an object of class ["
            + clazz.getName() + "]");
    }

    /**
     * Get type for the given property.
     * 
     * @param name The name of the property.
     * @param clazz The class that has the property.
     * @return The type of the property.
     * @throws Exception If the type of the property cannot be retrieved.
     */
    public static Class< ? > getFieldType(String name, Class< ? > clazz) throws Exception
    {
        Field field = clazz.getDeclaredField(name);
        return field.getType();
    }

    /**
     * Get type for the elements of the given array property.
     * 
     * @param name The name of the array property.
     * @param clazz The class that has the array property.
     * @return The type of the elements of the given array property.
     * @throws Exception If the type of the elements of the given property cannot be retrieved.
     */
    public static Class< ? > getFieldArrayType(String name, Class< ? > clazz) throws Exception
    {
        Field field = clazz.getDeclaredField(name);
        return field.getType().getComponentType();
    }

    /**
     * Get the generic type for the given collection property.
     * 
     * @param name The name of the collection property.
     * @param clazz The class that has the property.
     * @return The type of the elements of the collection.
     * @throws Exception If the generic type of the collection cannot be retrieved.
     */
    public static Class< ? > getFieldCollectionType(String name, Class< ? > clazz) throws Exception
    {
        Field field = clazz.getDeclaredField(name);
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return (Class< ? >) type.getActualTypeArguments()[0];
    }
}
