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

import sun.misc.Cleaner;

/**
*
* An array of ints that can store up to 2^63 elements.
*
* @author Piotr Wendykier (p.wendykier@icm.edu.pl)
*/
public class IntLargeArray extends LargeArray {

	private static final long serialVersionUID = 86623276977976615L;
	private int[] data;

    public IntLargeArray(long length) {
        this.type = LargeArrayType.INT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(this.length * this.sizeof);
            zeroMemory();
            Cleaner.create(this, new Deallocator(this.ptr, this.length, this.sizeof));
            MemoryCounter.increaseCoutner(this.length * this.sizeof);
        } else {
            data = new int[(int) length];
        }
    }
    
    public IntLargeArray(int[] data) {
        this.type = LargeArrayType.INT;
        this.sizeof = 4;
        this.length = data.length;
        this.data = data;
    }

    @Override
    public boolean getBoolean(long i) {
        if (isLarge()) {
            return (Utilities.UNSAFE.getInt(ptr + sizeof * i)) == 0 ? false : true;
        } else {
            return data[(int) i] == 0 ? false : true;
        }
    }

    @Override
    public byte getByte(long i) {
        if (isLarge()) {
            return (byte) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (byte) data[(int) i];
        }
    }

    @Override
    public short getShort(long i) {
        if (isLarge()) {
            return (short) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (short) data[(int) i];
        }
    }

    @Override
    public int getInt(long i) {
        if (isLarge()) {
            return Utilities.UNSAFE.getInt(ptr + sizeof * i);
        } else {
            return data[(int) i];
        }
    }

    @Override
    public long getLong(long i) {
        if (isLarge()) {
            return (long) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (long) data[(int) i];
        }
    }

    @Override
    public float getFloat(long i) {
        if (isLarge()) {
            return (float) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (float) data[(int) i];
        }
    }

    @Override
    public double getDouble(long i) {
        if (isLarge()) {
            return (double) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (double) data[(int) i];
        }
    }

    @Override
    public boolean[] getBoolData() {
        if (isLarge()) {
            return null;
        } else {
            boolean[] res = new boolean[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = data[i] == 0 ? false : true;

            }
            return res;
        }
    }

    @Override
    public byte[] getBData() {
        if (isLarge()) {
            return null;
        } else {
            byte[] res = new byte[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (byte) data[i];

            }
            return res;
        }
    }

    @Override
    public short[] getSData() {
        if (isLarge()) {
            return null;
        } else {
            short[] res = new short[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (short) data[i];

            }
            return res;
        }
    }

    @Override
    public int[] getIData() {
        if (isLarge()) {
            return null;
        } else {
            return data;
        }
    }

    @Override
    public long[] getLData() {
        if (isLarge()) {
            return null;
        } else {
            long[] res = new long[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (long) data[i];

            }
            return res;
        }
    }

    @Override
    public float[] getFData() {
        if (isLarge()) {
            return null;
        } else {
            float[] res = new float[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (float) data[i];

            }
            return res;
        }
    }

    @Override
    public double[] getDData() {
        if (isLarge()) {
            return null;
        } else {
            double[] res = new double[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (double) data[i];

            }
            return res;
        }
    }

    @Override
    public void setBoolean(long i, boolean value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, value == true ? 1 : 0);
        } else {
            data[(int) i] = value == true ? 1 : 0;
        }
    }

    @Override
    public void setByte(long i, byte value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setShort(long i, short value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setInt(long i, int value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, value);
        } else {
            data[(int) i] = value;
        }
    }

    @Override
    public void setLong(long i, long value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setFloat(long i, float value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setDouble(long i, double value) {
        if (isLarge()) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }
}
