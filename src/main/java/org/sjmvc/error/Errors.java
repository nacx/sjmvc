package org.sjmvc.error;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.sjmvc.controller.Controller;

/**
 * Generic error to be reported by a {@link Controller}.
 * 
 * @author Ignasi Barrera
 */
public class Errors implements Serializable
{
    /** Serial UID. */
    private static final long serialVersionUID = 1L;

    /** The list of errors. */
    private List<Exception> errors = new LinkedList<Exception>();

    /**
     * Adds the given error to the {@link #errors} list.
     * 
     * @param error The error to add.
     */
    public void add(final String error)
    {
        errors.add(new Exception(error));
    }

    /**
     * Adds the given error to the {@link #errors} list.
     * 
     * @param error The error to add.
     * @param cause The error cause.
     */
    public void add(final String error, final Exception cause)
    {
        errors.add(new Exception(error, cause));
    }

    /**
     * Checks if there are any errors.
     * 
     * @return Boolean indicating if there are any errors.
     */
    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }

    /**
     * Clear the list of errors.
     */
    public void clear()
    {
        errors.clear();
    }

}
