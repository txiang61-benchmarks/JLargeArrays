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

import sun.misc.Cleaner;

/**
 *
 * An array of bits (0 and 1) that can store up to 2<SUP>63</SUP> elements.
 * 
* @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class BitLargeArray extends LargeArray {
    private static final long serialVersionUID = -3499412355469845345L;
    private byte[] data;

    /**
     * Creates new instance of this class.
     * 
     * @param length number of elements
     */
    public BitLargeArray(long length) {
        this.type = LargeArrayType.BIT;
        this.sizeof = 1;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        long tmp = (length - 1) / 8;
        long dataSize = tmp + 1;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(dataSize * this.sizeof);
            zeroMemory();
            Cleaner.create(this, new LargeArray.Deallocator(this.ptr, dataSize, this.sizeof));
            MemoryCounter.increaseCounter(dataSize * this.sizeof);
        } else {
            data = new byte[(int) dataSize];
        }
    }

    /**
     * Creates new instance of this class.
     * 
     * @param data data array, this reference is not used internally.
     */
    public BitLargeArray(boolean[] data) {
        this(data.length);
        if (isLarge()) {
            for (int i = 0; i < data.length; i++) {
                int v = 0;
                if (data[i]) {
                    v = 1;
                }
                long index = i / 8;
                long ii = i % 8;
                byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
                byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
                Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                int v = 0;
                if (data[i]) {
                    v = 1;
                }
                int index = i / 8;
                int ii = i % 8;
                byte oldV = this.data[index];
                oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
                byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
                this.data[index] = newV;
            }
        }
    }
    
    @Override
    public Boolean get(long i) {
        return getBoolean(i);
    }

    @Override
    public boolean getBoolean(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return value == 1;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return value == 1;
        }
    }

    @Override
    public byte getByte(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (byte) value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (byte) value;
        }
    }

    @Override
    public short getShort(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (short) value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (short) value;
        }
    }

    @Override
    public int getInt(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return value;
        }
    }

    @Override
    public long getLong(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (long) value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (long) value;
        }
    }

    @Override
    public float getFloat(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (float) value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (float) value;
        }
    }

    @Override
    public double getDouble(long i) {
        if (isLarge()) {
            long index = i / 8;
            byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            long ii = i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (double) value;
        } else {
            int index = (int) i / 8;
            byte v = data[index];
            int ii = (int) i % 8;
            int value = v >> (8 - (ii + 1)) & 0x0001;
            return (double) value;
        }
    }
    
    
    @Override
    public byte[] getData() {
        if (isLarge()) {
            return null;
        } else {
           return data;
        }
    }
    
    @Override
    public boolean[] getBooleanData() {
        if (isLarge()) {
            return null;
        } else {
            boolean[] out = new boolean[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                int value = v >> (8 - (ii + 1)) & 0x0001;
                out[i] = value == 1;
            }
            return out;
        }
    }

    @Override
    public boolean[] getBooleanData(boolean[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            boolean[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new boolean[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    int value = v >> (8 - (ii + 1)) & 0x0001;
                    out[idx++] = value == 1;
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    int value = v >> (8 - (ii + 1)) & 0x0001;
                    out[idx++] = value == 1;
                }
            }
            return out;
        }
    }

    @Override
    public byte[] getByteData() {
        if (isLarge()) {
            return null;
        } else {
            byte[] out = new byte[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = (byte) (v >> (8 - (ii + 1)) & 0x0001);
            }
            return out;
        }
    }
    
    @Override
    public byte[] getByteData(byte[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            byte[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new byte[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (byte) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (byte) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }

    @Override
    public short[] getShortData() {
        if (isLarge()) {
            return null;
        } else {
            short[] out = new short[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = (short) (v >> (8 - (ii + 1)) & 0x0001);
            }
            return out;
        }
    }
    
   @Override
    public short[] getShortData(short[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            short[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new short[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (short) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (short) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }

    @Override
    public int[] getIntData() {
        if (isLarge()) {
            return null;
        } else {
            int[] out = new int[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = v >> (8 - (ii + 1)) & 0x0001;
            }
            return out;
        }
    }
    
    @Override
    public int[] getIntData(int[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            int[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new int[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (int) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (int) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }

    @Override
    public long[] getLongData() {
        if (isLarge()) {
            return null;
        } else {
            long[] out = new long[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = (long) (v >> (8 - (ii + 1)) & 0x0001);
            }
            return out;
        }
    }
    
    @Override
    public long[] getLongData(long[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            long[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new long[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (long) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (long) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }

    @Override
    public float[] getFloatData() {
        if (isLarge()) {
            return null;
        } else {
            float[] out = new float[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = (float) (v >> (8 - (ii + 1)) & 0x0001);
            }
            return out;
        }
    }
    
    @Override
    public float[] getFloatData(float[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            float[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new float[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (float) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (float) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }


    @Override
    public double[] getDoubleData() {
        if (isLarge()) {
            return null;
        } else {
            double[] out = new double[(int) length];
            byte v;
            int ii;
            for (int i = 0; i < out.length; i++) {
                v = data[i / 8];
                ii = i % 8;
                out[i] = (double) (v >> (8 - (ii + 1)) & 0x0001);
            }
            return out;
        }
    }
    
    @Override
    public double[] getDoubleData(double[] a, long startPos, long endPos, long step) {
        if(startPos < 0 || startPos >= length) {
            throw new IllegalArgumentException("startPos < 0 || startPos >= length");
        }
        if(endPos < 0 || endPos >= length || endPos < startPos) {
            throw new IllegalArgumentException("endPos < 0 || endPos >= length || endPos < startPos");
        }
        if(step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long)Math.ceil((endPos - startPos) / (double)step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            double[] out;
            if(a != null && a.length >= len) {
                out = a;
            }
            else {
                out = new double[(int) len];
            }
            int idx = 0;
            if (isLarge()) {
                for (long i = startPos; i < endPos; i+=step) {
                    long index = i / 8;
                    byte v = Utilities.UNSAFE.getByte(ptr + sizeof * index);
                    long ii = i % 8;
                    out[idx++] = (double) (v >> (8 - (ii + 1)) & 0x0001);
                }
            } else {
                for (long i = startPos; i < endPos; i+=step) {
                    int index = (int) i / 8;
                    byte v = data[index];
                    int ii = (int) i % 8;
                    out[idx++] = (double) (v >> (8 - (ii + 1)) & 0x0001);
                }
            }
            return out;
        }
    }

    

    @Override
    public void setBoolean(long i, boolean value) {
        if (isLarge()) {
            int v = 0;
            if (value) {
                v = 1;
            }
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int v = 0;
            if (value) {
                v = 1;
            }
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setByte(long i, byte value) {
        if(value < 0 || value > 1)
            throw new IllegalArgumentException("value has to be 0 or 1");
        if (isLarge()) {
            int v = (int)(value & 0xFF);
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int v = (int)(value & 0xFF);
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setShort(long i, short value) {
        if(value < 0 || value > 1)
            throw new IllegalArgumentException("value has to be 0 or 1");
        if (isLarge()) {
            int v = (int)((byte)value & 0xFF);
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int v = (int)((byte)value & 0xFF);
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setInt(long i, int value) {
        if(value < 0 || value > 1)
            throw new IllegalArgumentException("value has to ne 0 or 1");
        if (isLarge()) {
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((value << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((value << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setLong(long i, long value) {
        if(value < 0 || value > 1)
            throw new IllegalArgumentException("value has to be 0 or 1");
        int v = (int) value;
        if (isLarge()) {
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setFloat(long i, float value) {
        if(value != 0.0 && value != 1.0)
            throw new IllegalArgumentException("value has to be 0 or 1");
        int v = (int) value;
        if (isLarge()) {
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }

    @Override
    public void setDouble(long i, double value) {
        if(value != 0.0 && value != 1.0)
            throw new IllegalArgumentException("value has to be 0 or 1");
        int v = (int) value;
        if (isLarge()) {
            long index = i / 8;
            long ii = i % 8;
            byte oldV = Utilities.UNSAFE.getByte(ptr + sizeof * index);
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            Utilities.UNSAFE.putByte(ptr + sizeof * index, newV);
        } else {
            int index = (int)i / 8;
            int ii = (int)i % 8;
            byte oldV = this.data[index];
            oldV = (byte) (((0xFF7F >> ii) & oldV) & 0x00FF);
            byte newV = (byte) ((v << (8 - (ii + 1))) | oldV);
            this.data[index] = newV;
        }
    }
}
