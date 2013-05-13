/* ***** BEGIN LICENSE BLOCK *****
 * 
 * JLargeArrays
 * Copyright (C) 2013 University of Warsaw, ICM
 *
 * This file is part of GNU Classpath.
 *
 * GNU Classpath is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GNU Classpath; see the file COPYING.  If not, write to the 
 * University of Warsaw, Interdisciplinary Centre for Mathematical and 
 * Computational Modelling, Pawinskiego 5a, 02-106 Warsaw, Poland. 
 * 
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 * 
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version. 
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
