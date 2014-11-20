/*
 * Copyright (c) 2014, piotrw
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package pl.edu.icm.jlargearrays;

/* VisNow
 Copyright (C) 2006-2013 University of Warsaw, ICM

 This file is part of GNU Classpath.

 GNU Classpath is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 GNU Classpath is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with GNU Classpath; see the file COPYING.  If not, write to the 
 University of Warsaw, Interdisciplinary Centre for Mathematical and 
 Computational Modelling, Pawinskiego 5a, 02-106 Warsaw, Poland. 

 Linking this library statically or dynamically with other modules is
 making a combined work based on this library.  Thus, the terms and
 conditions of the GNU General Public License cover the whole
 combination.

 As a special exception, the copyright holders of this library give you
 permission to link this library with independent modules to produce an
 executable, regardless of the license terms of these independent
 modules, and to copy and distribute the resulting executable under
 terms of your choice, provided that you also meet, for each linked
 independent module, the terms and conditions of the license of that
 module.  An independent module is a module which is not derived from
 or based on this library.  If you modify this library, you may extend
 this exception to your version of the library, but you are not
 obligated to do so.  If you do not wish to do so, delete this
 exception statement from your version. */
import static pl.edu.icm.jlargearrays.LargeArray.LARGEST_32BIT_INDEX;
import sun.misc.Cleaner;

/**
 *
 * An array of complex numbers (single precision) that can store up to 2<SUP>63</SUP> elements.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class ComplexFloatLargeArray extends LargeArray
{

    private static final long serialVersionUID = 155390537810310407L;

    private float[] data;
    private long size;

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     */
    public ComplexFloatLargeArray(long length)
    {
        this(length, true);
    }

    /**
     * Creates new instance of this class.
     *
     * @param length           number of elements
     * @param zeroNativeMemory if true, then the native memory is zeroed.
     */
    public ComplexFloatLargeArray(long length, boolean zeroNativeMemory)
    {
        this.type = LargeArrayType.COMPLEX_FLOAT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        this.size = 2 * this.length;
        if (size > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(size * this.sizeof);
            if (zeroNativeMemory) {
                zeroNativeMemory(2 * length);
            }
            Cleaner.create(this, new Deallocator(this.ptr, size, this.sizeof));
            MemoryCounter.increaseCounter(size * this.sizeof);
        } else {
            data = new float[(int) size];
        }
    }

    /**
     * Creates a constant array.
     * <p>
     * @param length        number of elements
     * @param constantValue value
     */
    public ComplexFloatLargeArray(long length, float[] constantValue)
    {
        this.type = LargeArrayType.COMPLEX_FLOAT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        if (constantValue == null || constantValue.length != 2) {
            throw new IllegalArgumentException("constantValue == null || constantValue.length != 2");
        }

        this.length = length;
        this.size = 2 * this.length;
        this.isConstant = true;
        this.data = constantValue.clone();
    }

    /**
     * Creates new instance of this class.
     *
     * @param data data array, this reference is used internally.
     */
    public ComplexFloatLargeArray(float[] data)
    {
        this(new FloatLargeArray(data));
    }

    /**
     * Creates new instance of this class.
     *
     * @param data data array, this reference is used internally.
     */
    public ComplexFloatLargeArray(FloatLargeArray data)
    {
        if (data.length() % 2 != 0) {
            throw new IllegalArgumentException("The length of the data array must be even.");
        }
        if (data.length() <= 0) {
            throw new IllegalArgumentException(data.length() + " is not a positive long value");
        }
        this.type = LargeArrayType.COMPLEX_FLOAT;
        this.sizeof = 4;
        this.length = data.length / 2;
        this.size = data.length;
        this.ptr = data.ptr;
        this.isConstant = data.isConstant;
        this.data = data.getData();
    }

    /**
     * Creates new instance of this class.
     * <p>
     * @param dataRe real part
     * @param dataIm imaginary part
     */
    public ComplexFloatLargeArray(float[] dataRe, float[] dataIm)
    {
        this(new FloatLargeArray(dataRe), new FloatLargeArray(dataIm));
    }

    /**
     * Creates new instance of this class.
     * <p>
     * @param dataRe real part
     * @param dataIm imaginary part
     */
    public ComplexFloatLargeArray(FloatLargeArray dataRe, FloatLargeArray dataIm)
    {
        if (dataRe.length() != dataIm.length()) {
            throw new IllegalArgumentException("The length of the dataRe must be equal to the length of dataIm.");
        }
        if (dataRe.length() <= 0) {
            throw new IllegalArgumentException(dataRe.length() + " is not a positive long value");
        }
        this.type = LargeArrayType.COMPLEX_FLOAT;
        this.sizeof = 4;
        this.length = dataRe.length();
        this.size = 2 * dataRe.length();
        if (dataRe.isConstant && dataIm.isConstant) {
            this.isConstant = true;
            this.data = new float[]{dataRe.getFloat(0), dataIm.getFloat(0)};
        } else if (size > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(size * this.sizeof);
            Cleaner.create(this, new Deallocator(this.ptr, size, this.sizeof));
            MemoryCounter.increaseCounter(size * this.sizeof);
            for (long i = 0; i < this.length; i++) {
                Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, dataRe.getFloat(i));
                Utilities.UNSAFE.putFloat(ptr + sizeof * (2 * i + 1), dataIm.getFloat(i));
            }
        } else {
            data = new float[(int) size];
            for (int i = 0; i < this.length; i++) {
                data[2 * i] = dataRe.getFloat(i);
                data[2 * i + 1] = dataIm.getFloat(i);
            }
        }
    }

    /**
     * Returns a deep copy of this instance. (The elements themselves are copied.)
     *
     * @return a clone of this instance
     */
    @Override
    public ComplexFloatLargeArray clone()
    {
        if (isConstant()) {
            return new ComplexFloatLargeArray(length, data);
        } else {
            ComplexFloatLargeArray v = new ComplexFloatLargeArray(length, false);
            Utilities.arraycopy(this, 0, v, 0, length);
            return v;
        }
    }

    /**
     * Returns the real part of this array.
     * <p>
     * @return the real part.
     */
    public FloatLargeArray getRealArray()
    {
        FloatLargeArray re = new FloatLargeArray(length, false);
        float val;
        for (long i = 0; i < length; i++) {
            if (ptr != 0) {
                val = Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
            } else {
                if (isConstant()) {
                    val = data[0];
                } else {
                    val = data[(int) (2 * i)];
                }
            }
            re.setFloat(i, val);
        }
        return re;
    }

    /**
     * Returns the imaginary part of this array.
     * <p>
     * @return the imaginary part.
     */
    public FloatLargeArray getImaginaryArray()
    {
        FloatLargeArray im = new FloatLargeArray(length, false);
        float val;
        for (long i = 0; i < length; i++) {
            if (ptr != 0) {
                val = Utilities.UNSAFE.getFloat(ptr + sizeof * (2 * i + 1));
            } else {
                if (isConstant()) {
                    val = data[0];
                } else {
                    val = data[(int) (2 * i + 1)];
                }
            }
            im.setFloat(i, val);
        }
        return im;
    }

    /**
     * Returns the absolute value of this array.
     * <p>
     * @return the absolute value.
     */
    public FloatLargeArray getAbsArray()
    {
        FloatLargeArray out = new FloatLargeArray(length);
        for (long i = 0; i < length; i++) {
            float[] val = getComplex(i);
            out.setFloat(i, (float) Math.sqrt(val[0] * val[0] + val[1] * val[1]));
        }
        return out;
    }

    /**
     * Returns the argument of this array.
     * <p>
     * @return the argument
     */
    public FloatLargeArray getArgArray()
    {
        FloatLargeArray out = new FloatLargeArray(length);
        for (long i = 0; i < length; i++) {
            float[] val = getComplex(i);
            out.setFloat(i, (float) Math.atan2(val[1], val[0]));
        }
        return out;
    }

    @Override
    public float[] get(long i)
    {
        return getComplex(i);
    }

    @Override
    public float[] getFromNative(long i)
    {
        float re = Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        float im = Utilities.UNSAFE.getFloat(ptr + sizeof * (2 * i + 1));
        return new float[]{re, im};
    }

    @Override
    public boolean getBoolean(long i)
    {
        if (ptr != 0) {
            return Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i) != 0;
        } else {
            if (isConstant()) {
                return data[0] != 0;
            } else {
                return data[(int) (2 * i)] != 0;
            }
        }
    }

    @Override
    public byte getByte(long i)
    {
        if (ptr != 0) {
            return (byte) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return (byte) data[0];
            } else {
                return (byte) data[(int) (2 * i)];
            }
        }
    }

    @Override
    public short getShort(long i)
    {
        if (ptr != 0) {
            return (short) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return (short) data[0];
            } else {
                return (short) data[(int) (2 * i)];
            }
        }
    }

    @Override
    public int getInt(long i)
    {
        if (ptr != 0) {
            return (int) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return (int) data[0];
            } else {
                return (int) data[(int) (2 * i)];
            }
        }
    }

    @Override
    public long getLong(long i)
    {
        if (ptr != 0) {
            return (long) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return (long) data[0];
            } else {
                return (long) data[(int) (2 * i)];
            }
        }
    }

    @Override
    public float getFloat(long i)
    {
        if (ptr != 0) {
            return Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return data[0];
            } else {
                return data[(int) (2 * i)];
            }
        }
    }

    @Override
    public double getDouble(long i)
    {
        if (ptr != 0) {
            return (double) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
        } else {
            if (isConstant()) {
                return (double) data[0];
            } else {
                return (double) data[(int) (2 * i)];
            }
        }
    }

    /**
     * Returns a complex value at index i. Array bounds are not checked. Calling
     * this method with invalid index argument will cause JVM crash.
     *
     * @param i an index
     *
     * @return a value at index i ({re, im}).
     */
    public float[] getComplex(long i)
    {
        if (ptr != 0) {
            return new float[]{(Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i)), (Utilities.UNSAFE.getFloat(ptr + sizeof * (2 * i + 1)))};
        } else {
            if (isConstant()) {
                return new float[]{data[0], data[1]};
            } else {
                return new float[]{data[(int) (2 * i)], data[(int) (2 * i + 1)]};
            }
        }
    }

    @Override
    public float[] getData()
    {
        return getComplexData();
    }

    @Override
    public boolean[] getBooleanData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                boolean[] out = new boolean[(int) length];
                boolean elem = data[0] != 0;
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                boolean[] res = new boolean[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = data[2 * i] != 0;

                }
                return res;
            }
        }
    }

    @Override
    public boolean[] getBooleanData(boolean[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            boolean[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new boolean[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    float v = Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                    out[idx++] = v != 0;
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[0] != 0;
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        float v = data[(int) (2 * i)];
                        out[idx++] = v != 0;
                    }
                }
            }
            return out;
        }
    }

    @Override
    public byte[] getByteData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                byte[] out = new byte[(int) length];
                byte elem = (byte) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                byte[] res = new byte[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (byte) data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public byte[] getByteData(byte[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            byte[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new byte[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (byte) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (byte) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (byte) data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public short[] getShortData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                short[] out = new short[(int) length];
                short elem = (short) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                short[] res = new short[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (short) data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public short[] getShortData(short[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            short[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new short[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (short) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (short) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (short) data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public int[] getIntData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                int[] out = new int[(int) length];
                int elem = (int) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                int[] res = new int[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (int) data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public int[] getIntData(int[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            int[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new int[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (int) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (int) data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (int) data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public long[] getLongData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                long[] out = new long[(int) length];
                long elem = (long) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                long[] res = new long[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (long) data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public long[] getLongData(long[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            long[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new long[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (long) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (long) data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (long) data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public float[] getFloatData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                float[] out = new float[(int) length];
                float elem = (float) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                float[] res = new float[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public float[] getFloatData(float[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            float[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new float[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public double[] getDoubleData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                double[] out = new double[(int) length];
                double elem = (double) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                double[] res = new double[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (double) data[2 * i];

                }
                return res;
            }
        }
    }

    @Override
    public double[] getDoubleData(double[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            double[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new double[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (double) Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (double) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (double) data[(int) (2 * i)];
                    }
                }
            }
            return out;
        }
    }

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this
     * method returns complex data in the interleaved layout. Otherwise, it returns null.
     *
     * @return an array containing the elements of the list or null
     */
    public float[] getComplexData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (size > getMaxSizeOf32bitArray()) return null;
                float[] out = new float[(int) size];
                for (int i = 0; i < length; i++) {
                    out[2 * i] = data[0];
                    out[2 * i + 1] = data[1];
                }
                return out;
            } else {
                return data;
            }
        }
    }

    /**
     * If (endPos - startPos) / step is smaller than LARGEST_32BIT_INDEX, then
     * this method returns selected elements of an array. Otherwise, it returns
     * null. If 2 * ((endPos - startPos) / step) is smaller or equal to a.length, it
     * is returned therein. Otherwise, a new array is allocated and returned.
     * Array bounds are checked.
     *
     * @param a        the array into which the elements are to be stored, if it is big
     *                 enough; otherwise, a new array of is allocated for this purpose.
     * @param startPos starting position (included)
     * @param endPos   ending position (excluded)
     * @param step     step size
     *
     * @return an array containing the elements of the list or null
     */
    public float[] getComplexData(float[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos > length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos > length || endPos < startPos");
        }
        if (step < 1) {
            throw new IllegalArgumentException("step < 1");
        }

        long len = 2 * (long) Math.ceil((endPos - startPos) / (double) step);
        if (len > getMaxSizeOf32bitArray()) {
            return null;
        } else {
            float[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new float[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = Utilities.UNSAFE.getFloat(ptr + sizeof * 2 * i);
                    out[idx++] = Utilities.UNSAFE.getFloat(ptr + sizeof * (2 * i + 1));
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[0];
                        out[idx++] = data[1];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[(int) (2 * i)];
                        out[idx++] = data[(int) (2 * i + 1)];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public void setToNative(long i, Object value)
    {
        if (!(value instanceof float[])) {
            throw new IllegalArgumentException(value + " is not an array of floats.");
        }

        Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, ((float[]) value)[0]);
        Utilities.UNSAFE.putFloat(ptr + sizeof * (2 * i + 1), ((float[]) value)[1]);
    }

    @Override
    public void setBoolean(long i, boolean value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value == true ? 1 : 0);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value == true ? 1 : 0;
        }
    }

    @Override
    public void setByte(long i, byte value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value;
        }
    }

    @Override
    public void setShort(long i, short value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value;
        }
    }

    @Override
    public void setInt(long i, int value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value;
        }
    }

    @Override
    public void setLong(long i, long value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value;
        }
    }

    @Override
    public void setFloat(long i, float value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value;
        }
    }

    @Override
    public void setDouble(long i, double value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = (float) value;
        }
    }

    @Override
    public void set(long i, Object value)
    {
        if (!(value instanceof float[])) {
            throw new IllegalArgumentException(value + " is not an array of floats.");
        }
        setComplex(i, (float[]) value);
    }

    /**
     * Sets a complex value ({re, im}) at index i. Array bounds are not checked. Calling this
     * method with invalid index argument will cause JVM crash.
     *
     * @param i     index
     * @param value value to set
     */
    public void setComplex(long i, float[] value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * 2 * i, value[0]);
            Utilities.UNSAFE.putFloat(ptr + sizeof * (2 * i + 1), value[1]);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) (2 * i)] = value[0];
            data[(int) (2 * i + 1)] = value[1];
        }
    }

}
