package org.sjmvc.binding;

import org.sjmvc.error.Errors;

/**
 * Result of a bind operation.
 * 
 * @author Ignasi Barrera
 * @param <T> The type of the object target of the binding.
 */
public class BindingResult<T>
{
    /** The target object of the binding. */
    private T target;

    /** The binding errors, if any. */
    private Errors errors = new Errors();

    // Getters and setters

    public T getTarget()
    {
        return target;
    }

    public void setTarget(T target)
    {
        this.target = target;
    }

    public Errors getErrors()
    {
        return errors;
    }

    public void setErrors(Errors errors)
    {
        this.errors = errors;
    }

}
