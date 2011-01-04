package org.sjmvc.binding;

/**
 * Binds the values of an object to another object.
 * 
 * @author Ignasi Barrera
 * @param <T> The type of the object target of the binding.
 */
public interface Binder<T, S>
{
    /**
     * Executes the binding to the target object type.
     * 
     * @return The result of the binding.
     */
    public BindingResult<T> bind();

    /**
     * Get the object target of the binding.
     * 
     * @return The object target of the binding.
     */
    public T getTarget();

    /**
     * Get the object source of the binding.
     * 
     * @return The object source of the binding.
     */
    public S getSource();
}
