package org.sjmvc.binding;

import java.io.Serializable;

/**
 * Plain object used to perform binding tests.
 * 
 * @author ibarrera
 */
public class BindPojo implements Serializable
{
    /** Serial UID. */
    private static final long serialVersionUID = 1L;

    /** A String property. */
    private String stringProperty;

    // Getters and setters

    public String getStringProperty()
    {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty)
    {
        this.stringProperty = stringProperty;
    }

}
