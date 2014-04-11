/* ***** BEGIN LICENSE BLOCK *****
 * 
 * JLargeArrays
 * Copyright (C) 2013 onward University of Warsaw, ICM
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
    public JLargeArraysTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(JLargeArraysTest.class);
    }

    public void testBitLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        BitLargeArray a = new BitLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setBoolean(idx, val);
        assertEquals(val, a.getBoolean(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).booleanValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new BitLargeArray(10);
        a.setBoolean(idx, val);
        assertEquals(val, a.getBoolean(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).booleanValue());

    }

    public void testBitLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        BitLargeArray a = new BitLargeArray(10);
        long idx = 5;
        boolean val = true;
        a.setToNative(idx, val);
        assertEquals(val, (boolean) a.getFromNative(idx));
    }

    public void testBitLargeArrayGetData()
    {
        boolean[] data = new boolean[]{true, false, false, false, true, true, true, false, true, true};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        BitLargeArray a = new BitLargeArray(data);
        boolean[] res = a.getBooleanData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new BitLargeArray(data);
        res = a.getBooleanData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testBitArraycopy()
    {
        boolean[] data = new boolean[]{true, false, false, false, true, true, true, false, true, true};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        BitLargeArray a = new BitLargeArray(data);
        BitLargeArray b = new BitLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        b = new BitLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new BitLargeArray(data);
        b = new BitLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
        b = new BitLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getBoolean(i));
        }
    }

    public void testByteLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ByteLargeArray a = new ByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setByte(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).byteValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ByteLargeArray(10);
        a.setByte(idx, val);
        assertEquals(val, a.getByte(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).byteValue());
    }

    public void testByteLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ByteLargeArray a = new ByteLargeArray(10);
        long idx = 5;
        byte val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (byte) a.getFromNative(idx));
    }

    public void testByteLargeArrayGetData()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ByteLargeArray a = new ByteLargeArray(data);
        byte[] res = a.getByteData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ByteLargeArray(data);
        res = a.getByteData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testByteArraycopy()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ByteLargeArray a = new ByteLargeArray(data);
        ByteLargeArray b = new ByteLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        b = new ByteLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ByteLargeArray(data);
        b = new ByteLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
        b = new ByteLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getByte(i));
        }
    }

    public void testShortLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ShortLargeArray a = new ShortLargeArray(10);
        long idx = 5;
        short val = -100;
        a.setShort(idx, val);
        assertEquals(val, a.getShort(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new ShortLargeArray(10);
        a.setShort(idx, val);
        assertEquals(val, a.getShort(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).shortValue());
    }

    public void testShortLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        ShortLargeArray a = new ShortLargeArray(10);
        long idx = 5;
        short val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (short) a.getFromNative(idx));
    }

    public void testShortLargeArrayGetData()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ShortLargeArray a = new ShortLargeArray(data);
        short[] res = a.getShortData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ShortLargeArray(data);
        res = a.getShortData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testShortArraycopy()
    {
        short[] data = new short[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        ShortLargeArray a = new ShortLargeArray(data);
        ShortLargeArray b = new ShortLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        b = new ShortLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new ShortLargeArray(data);
        b = new ShortLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
        b = new ShortLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getShort(i));
        }
    }

    public void testIntLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        IntLargeArray a = new IntLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setInt(idx, val);
        assertEquals(val, a.getInt(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).intValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new IntLargeArray(10);
        a.setInt(idx, val);
        assertEquals(val, a.getInt(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).intValue());
    }

    public void testIntLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        IntLargeArray a = new IntLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (int) a.getFromNative(idx));
    }

    public void testIntLargeArrayGetData()
    {
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        IntLargeArray a = new IntLargeArray(data);
        int[] res = a.getIntData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new IntLargeArray(data);
        res = a.getIntData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testIntArraycopy()
    {
        int[] data = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        IntLargeArray a = new IntLargeArray(data);
        IntLargeArray b = new IntLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        b = new IntLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new IntLargeArray(data);
        b = new IntLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
        b = new IntLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getInt(i));
        }
    }

    public void testLongLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LongLargeArray a = new LongLargeArray(10);
        long idx = 5;
        int val = -100;
        a.setLong(idx, val);
        assertEquals(val, a.getLong(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).longValue());
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new LongLargeArray(10);
        a.setLong(idx, val);
        assertEquals(val, a.getLong(idx));
        idx = 6;
        a.set(idx, val);
        assertEquals(val, (a.get(idx)).longValue());
    }

    public void testLongLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        LongLargeArray a = new LongLargeArray(10);
        long idx = 5;
        long val = -100;
        a.setToNative(idx, val);
        assertEquals(val, (long) a.getFromNative(idx));
    }

    public void testLongLargeArrayGetData()
    {
        long[] data = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LongLargeArray a = new LongLargeArray(data);
        long[] res = a.getLongData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new LongLargeArray(data);
        res = a.getLongData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testLongArraycopy()
    {
        long[] data = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        LongLargeArray a = new LongLargeArray(data);
        LongLargeArray b = new LongLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        b = new LongLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new LongLargeArray(data);
        b = new LongLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
        b = new LongLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getLong(i));
        }
    }

    public void testFloatLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        FloatLargeArray a = new FloatLargeArray(10);
        long idx = 5;
        float val = 3.4f;
        a.setFloat(idx, val);
        assertEquals(val, a.getFloat(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new FloatLargeArray(10);
        a.setFloat(idx, val);
        assertEquals(val, a.getFloat(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
    }

    public void testFloatLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        FloatLargeArray a = new FloatLargeArray(10);
        long idx = 5;
        float val = 3.4f;
        a.setToNative(idx, val);
        assertEquals(val, (float) a.getFromNative(idx), 0.0);
    }

    public void testFloatLargeArrayGetData()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        FloatLargeArray a = new FloatLargeArray(data);
        float[] res = a.getFloatData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new FloatLargeArray(data);
        res = a.getFloatData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testFloatArraycopy()
    {
        float[] data = new float[]{1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f, 10.10f};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        FloatLargeArray a = new FloatLargeArray(data);
        FloatLargeArray b = new FloatLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        b = new FloatLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new FloatLargeArray(data);
        b = new FloatLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
        b = new FloatLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getFloat(i));
        }
    }

    public void testDoubleLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        DoubleLargeArray a = new DoubleLargeArray(10);
        long idx = 5;
        double val = 3.4;
        a.setDouble(idx, val);
        assertEquals(val, a.getDouble(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new DoubleLargeArray(10);
        a.setDouble(idx, val);
        assertEquals(val, a.getDouble(idx), 0.0);
        idx = 6;
        a.set(idx, val);
        assertEquals(val, a.get(idx));
    }

    public void testDoubleLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        DoubleLargeArray a = new DoubleLargeArray(10);
        long idx = 5;
        double val = 3.4;
        a.setToNative(idx, val);
        assertEquals(val, a.getFromNative(idx), 0.0);
    }

    public void testDoubleLargeArrayGetData()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        long startPos = 2;
        long endPos = 7;
        long step = 2;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        DoubleLargeArray a = new DoubleLargeArray(data);
        double[] res = a.getDoubleData(null, startPos, endPos, step);
        int idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new DoubleLargeArray(data);
        res = a.getDoubleData(null, startPos, endPos, step);
        idx = 0;
        for (long i = startPos; i < endPos; i += step) {
            assertEquals(data[(int) i], res[idx++]);
        }
    }

    public void testDoubleArraycopy()
    {
        double[] data = new double[]{1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        DoubleLargeArray a = new DoubleLargeArray(data);
        DoubleLargeArray b = new DoubleLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        b = new DoubleLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new DoubleLargeArray(data);
        b = new DoubleLargeArray(2 * data.length);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
        b = new DoubleLargeArray(2 * data.length);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.getDouble(i));
        }
    }

    public void testStringLargeArrayGetSet()
    {
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        StringLargeArray a = new StringLargeArray(10, 14);
        long idx = 5;
        String val1 = "test0123ąęćńżź";
        String val2 = "test";
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
        LargeArray.setMaxSizeOf32bitArray(1);
        a = new StringLargeArray(10, 14);
        a.set(idx, val1);
        assertEquals(val1, a.get(idx));
        a.set(idx, val2);
        assertEquals(val2, a.get(idx));
    }

    public void testStringLargeArrayGetSetNative()
    {
        LargeArray.setMaxSizeOf32bitArray(1);
        StringLargeArray a = new StringLargeArray(10, 14);
        long idx = 5;
        String val1 = "test0123ąęćńżź";
        String val2 = "test";
        a.setToNative(idx, val1);
        assertEquals(val1, a.getFromNative(idx));
        a.setToNative(idx, val2);
        assertEquals(val2, a.getFromNative(idx));
    }

    public void testStringArraycopy()
    {
        String[] data = new String[]{"a", "ab", "abc", "ąęć", "1234", "test string", "ANOTHER TEST STRING", "", "\n", "\r"};
        int startPos = 2;
        int length = 8;
        LargeArray.setMaxSizeOf32bitArray(1073741824);
        StringLargeArray a = new StringLargeArray(data);
        StringLargeArray b = new StringLargeArray(2 * data.length, 20);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new StringLargeArray(2 * data.length, 20);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        LargeArray.setMaxSizeOf32bitArray(data.length - 1);
        a = new StringLargeArray(data);
        b = new StringLargeArray(2 * data.length, 20);
        Utilities.arraycopy(a, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
        b = new StringLargeArray(2 * data.length, 20);
        Utilities.arraycopy(data, startPos, b, 0, length);
        for (int i = 0; i < length; i++) {
            assertEquals(data[startPos + i], b.get(i));
        }
    }

}
