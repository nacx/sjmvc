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

package org.sjmvc.config;

import static org.sjmvc.config.Configuration.CONTROLLER_PATH_SUFFIX;
import static org.sjmvc.config.Configuration.CONTROLLER_PREFIX;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Configuration} class.
 * 
 * @author Ignasi Barrera
 */
public class ConfigurationTest
{

    @Test
    public void testIsConfigProperty()
    {
        assertTrue(Configuration.isControllerProperty(CONTROLLER_PREFIX + CONTROLLER_PATH_SUFFIX));
        assertTrue(Configuration.isControllerProperty(CONTROLLER_PREFIX + " "
            + CONTROLLER_PATH_SUFFIX));
        assertTrue(Configuration.isControllerProperty(CONTROLLER_PREFIX + "test"
            + CONTROLLER_PATH_SUFFIX));
        assertTrue(Configuration.isControllerProperty(CONTROLLER_PREFIX + "."
            + CONTROLLER_PATH_SUFFIX));

        assertFalse(Configuration.isControllerProperty(""));
        assertFalse(Configuration.isControllerProperty("test"));
        assertFalse(Configuration.isControllerProperty(CONTROLLER_PREFIX));
        assertFalse(Configuration.isControllerProperty(CONTROLLER_PATH_SUFFIX));
        assertFalse(Configuration.isControllerProperty(CONTROLLER_PREFIX + "test"));
        assertFalse(Configuration.isControllerProperty("test" + CONTROLLER_PATH_SUFFIX));
    }
}
