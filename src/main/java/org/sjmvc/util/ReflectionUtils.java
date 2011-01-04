package org.sjmvc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

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
     * @param <F> The type of the field to get.
     * @param target The object that has the field.
     * @param name The name of the property.
     * @param type The type of the object to get.
     * @return The value of the property.
     * @throws Exception If the value of the property cannot be retrieved.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Object target, String name, Class<T> type) throws Exception
    {
        Method getter = ReflectionUtils.getter(name, target.getClass());
        T value = (T) getter.invoke(target, (Object[]) null);

        // Instantiate object if necessary
        if (value == null)
        {
            value = type.newInstance();
        }

        return value;
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
        Method setter = ReflectionUtils.setter(name, target.getClass(), value.getClass());
        setter.invoke(target, new Object[] {value});
    }

    /**
     * Gets the getter for the given field.
     * 
     * @param fieldName The name of the field.
     * @param clazz The class that has the field.
     * @return The getter for the given field.
     * @throws Exception If the getter cannot be obtained.
     */
    public static Method getter(String fieldName, Class< ? > clazz) throws Exception
    {
        try
        {
            return getter("get", fieldName, clazz);
        }
        catch (NoSuchMethodException ex)
        {
            return getter("is", fieldName, clazz);
        }
    }

    /**
     * Gets the setter for the given field.
     * 
     * @param fieldName The name of the field.
     * @param clazz The class that has the field.
     * @param type The type of the field to set.
     * @return The setter for the given field.
     * @throws Exception If the setter cannot be obtained.
     */
    public static Method setter(String fieldName, Class< ? > clazz, Class< ? > type)
        throws Exception
    {
        String name = "set" + StringUtils.capitalize(fieldName);
        Method method = clazz.getMethod(name, new Class< ? >[] {type});
        method.setAccessible(true);
        return method;
    }

    /**
     * Convert the given String to the given class.
     * 
     * @param clazz The destination class.
     * @param value The String value to convert.
     * @return The converted value.
     */
    public static Object fromString(Class< ? > clazz, String value)
    {
        if (clazz.equals(Integer.class))
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

        return value;
    }

    /**
     * Gets the getter for the given field.
     * 
     * @param prefix The field prefix.
     * @param fieldName The name of the field.
     * @param clazz The class that has the field.
     * @return The getter for the given field.
     * @throws Exception If teh getter cannot be obtained.
     */
    private static Method getter(String prefix, String fieldName, Class< ? > clazz)
        throws Exception
    {
        String name = prefix + StringUtils.capitalize(fieldName);
        return clazz.getMethod(name, new Class< ? >[0]);
    }
}
