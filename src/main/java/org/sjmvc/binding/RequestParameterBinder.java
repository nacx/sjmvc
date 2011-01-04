package org.sjmvc.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.sjmvc.error.Errors;
import org.sjmvc.util.ReflectionUtils;

/**
 * {@link Binder} implementation that binds request parameters to the defined target object.
 * 
 * @author Ignasi Barrera
 */
public class RequestParameterBinder<T> implements Binder<T, ServletRequest>
{
    /** Prefix for the parameters that will be included in the binding process. */
    private static final String BIND_PARAMETER_PREFIX = "model.";

    /** The object target of the binding. */
    private T target;

    /** The request to bind. */
    private ServletRequest request;

    /** The binding errors. */
    private Errors errors;

    /**
     * Creates the <code>RequestParameterBinder</code> for the given request.
     * 
     * @param request The request to bind.
     */
    public RequestParameterBinder(T target, ServletRequest request)
    {
        super();
        this.target = target;
        this.request = request;
        this.errors = new Errors();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BindingResult<T> bind()
    {
        doBind(request.getParameterMap());

        BindingResult<T> result = new BindingResult<T>();
        result.setTarget(target);
        result.setErrors(errors);

        return result;
    }

    @Override
    public ServletRequest getSource()
    {
        return request;
    }

    @Override
    public T getTarget()
    {
        return target;
    }

    /**
     * Bind the given parameter map to the target object.
     * 
     * @param parameters The parameter map to bind.
     * @return The binding result.
     */
    protected void doBind(Map<String, String[]> parameters)
    {
        for (Map.Entry<String, String[]> param : parameters.entrySet())
        {
            String name = param.getKey();
            String[] values = param.getValue();

            // Only bind the bindable parameters
            if (name.startsWith(BIND_PARAMETER_PREFIX))
            {
                bindField(target, name, values);
            }
        }
    }

    /**
     * Binds the given field to the given value in the target object.
     * 
     * @param currentObject The current object being processed.
     * @param name The name of the field to bind.
     * @param values The values to bind to the field.
     */
    protected void bindField(Object currentObject, String name, String[] values)
    {
        String[] path = name.split("\\.");

        try
        {
            if (path.length == 1)
            {
                // Bind simple property
                setValue(currentObject, path[0], values);
            }
            else
            {
                // Recursively bind the nested values
                String remainingPath = StringUtils.join(path, "", 1, path.length);
                Object nestedObject =
                    ReflectionUtils.getProperty(currentObject, path[0], Object.class);

                bindField(nestedObject, remainingPath, values);
            }
        }
        catch (Exception ex)
        {
            errors.add(ex.getMessage(), ex);
        }
    }

    /**
     * Sets the given value to the given field.
     * 
     * @param currentObject The current object being processed.
     * @param name The name of the field.
     * @param values The values to set.
     * @throws BindingError If the value of the property cannot be set.
     */
    protected void setValue(Object currentObject, String name, String values[]) throws BindingError
    {
        try
        {
            Field field = currentObject.getClass().getDeclaredField(name);
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers))
            {

                if (values.length == 1)
                {
                    ReflectionUtils.transformAndSet(currentObject, name, values[0]);
                }
                else
                {
                    Collection<Object> col =
                        ReflectionUtils.getProperty(currentObject, name, ArrayList.class);
                    col.clear();

                    for (String currentValue : values)
                    {
                        // FIXME: retrieve the collection type
                        col.add(ReflectionUtils.fromString(Object.class, currentValue));
                    }

                    ReflectionUtils.setValue(currentObject, name, col);
                }
            }
        }
        catch (Exception ex)
        {
            throw new BindingError("Could not bind property [" + name + "] of ["
                + currentObject.getClass().getName() + "]", ex);
        }
    }
}
