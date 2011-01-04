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

/**
 * Binds the values of an object to another object.
 * 
 * @author Ignasi Barrera
 * @param <T> The type of the object target of the binding.
 * @param <S> The type of the source target of the binding.
 * @see RequestParameterBinder
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
