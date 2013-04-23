/* ***** BEGIN LICENSE BLOCK *****
 * 
 * JLargeArrays is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * JLargeArrays is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this Module; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * ***** END LICENSE BLOCK ***** */

package pl.edu.icm.jlargearrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests.
 * 
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class JLargeArraysTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JLargeArraysTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JLargeArraysTest.class );
    }

    public void testBooleanLargeArrayGetSet()
    {
        BooleanLargeArray a = new BooleanLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        assertEquals(val, a.getBoolean(idx));
    }
    
    public void testByteLargeArrayGetSet()
    {
        ByteLargeArray a = new ByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setByte(idx, val);
        assertEquals(val, a.getByte(idx));
    }
    
    public void testShortLargeArrayGetSet()
    {
        ShortLargeArray a = new ShortLargeArray(10);
        long idx = 5;
        short val = -100;
        a.setShort(idx, val);
        assertEquals(val, a.getShort(idx));
    }
    
    public void testIntLargeArrayGetSet()
    {
        IntLargeArray a = new IntLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setInt(idx, val);
        assertEquals(val, a.getInt(idx));
    }
    
    public void testLongLargeArrayGetSet()
    {
        LongLargeArray a = new LongLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setLong(idx, val);
        assertEquals(val, a.getLong(idx));
    }
    
    public void testFloatLargeArrayGetSet()
    {
        FloatLargeArray a = new FloatLargeArray(10);
        long idx = 5;
        float val = 3.4f;
        a.setFloat(idx, val);
        assertEquals(val, a.getFloat(idx), 0.0);
    }
    
    public void testDoubleLargeArrayGetSet()
    {
        DoubleLargeArray a = new DoubleLargeArray(10);
        long idx = 5;
        double val = 3.4;
        a.setDouble(idx, val);
        assertEquals(val, a.getDouble(idx), 0.0);
    }
    
}
