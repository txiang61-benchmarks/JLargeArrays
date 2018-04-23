/* ***** BEGIN LICENSE BLOCK *****
 * JLargeArrays
 * Copyright (C) 2013 onward University of Warsaw, ICM
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ***** END LICENSE BLOCK ***** */
package pl.edu.icm.jlargearrays;

import sun.misc.Cleaner;

import units.qual.*;

/**
 *
 * An array of floats that can store up to 2<SUP>63</SUP> elements.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class FloatLargeArray extends LargeArray
{

    private static final long serialVersionUID = -8342458159338079576L;
    private @UnknownUnits float[] data;

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     */
    public FloatLargeArray(long length)
    {
        this(length, true);
    }

    /**
     * Creates new instance of this class.
     *
     * @param length           number of elements
     * @param zeroNativeMemory if true, then the native memory is zeroed.
     */
    public FloatLargeArray(long length, boolean zeroNativeMemory)
    {
        this.type = LargeArrayType.FLOAT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(this.length * this.sizeof);
            if (zeroNativeMemory) {
                zeroNativeMemory(length);
            }
            Cleaner.create(this, new Deallocator(this.ptr, this.length, this.sizeof));
            MemoryCounter.increaseCounter(this.length * this.sizeof);
        } else {
            data = new float[(int) length];
        }
    }

    /**
     * Creates a constant array.
     * <p>
     * @param length        number of elements
     * @param constantValue value
     */
    public FloatLargeArray(long length, @UnknownUnits float constantValue)
    {
        this.type = LargeArrayType.FLOAT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        this.isConstant = true;
        this.data = new float[]{constantValue};
    }

    /**
     * Creates new instance of this class.
     *
     * @param data data array, this reference is used internally.
     */
    public FloatLargeArray(@UnknownUnits float[] data)
    {
        this.type = LargeArrayType.FLOAT;
        this.sizeof = 4;
        this.length = data.length;
        this.data = data;
    }

    /**
     * Returns a deep copy of this instance. (The elements themselves are copied.)
     *
     * @return a clone of this instance
     */
    @Override
    public FloatLargeArray clone()
    {
        if (isConstant()) {
            return new FloatLargeArray(length, getFloat(0));
        } else {
            FloatLargeArray v = new FloatLargeArray(length, false);
            Utilities.arraycopy(this, 0, v, 0, length);
            return v;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (super.equals(o)) {
            FloatLargeArray la = (FloatLargeArray) o;
            return this.data == la.data;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return 29 * super.hashCode() + (this.data != null ? this.data.hashCode() : 0);
    }

    @Override
    public @UnknownUnits Float get(long i)
    {
        return getFloat(i);
    }

    @Override
    public Float getFromNative(long i)
    {
        return Utilities.UNSAFE.getFloat(ptr + sizeof * i);
    }

    @Override
    public boolean getBoolean(long i)
    {
        if (ptr != 0) {
            return (Utilities.UNSAFE.getFloat(ptr + sizeof * i)) != 0;
        } else {
            if (isConstant()) {
                return data[0] != 0;
            } else {
                return data[(int) i] != 0;
            }
        }
    }

    @Override
    public @UnknownUnits byte getByte(long i)
    {
        if (ptr != 0) {
            return (byte) (Utilities.UNSAFE.getFloat(ptr + sizeof * i));
        } else {
            if (isConstant()) {
                return (byte) data[0];
            } else {
                return (byte) data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits short getShort(long i)
    {
        if (ptr != 0) {
            return (short) (Utilities.UNSAFE.getFloat(ptr + sizeof * i));
        } else {
            if (isConstant()) {
                return (short) data[0];
            } else {
                return (short) data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits int getInt(long i)
    {
        if (ptr != 0) {
            return (int) (Utilities.UNSAFE.getFloat(ptr + sizeof * i));
        } else {
            if (isConstant()) {
                return (int) data[0];
            } else {
                return (int) data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits long getLong(long i)
    {
        if (ptr != 0) {
            return (long) (Utilities.UNSAFE.getFloat(ptr + sizeof * i));
        } else {
            if (isConstant()) {
                return (long) data[0];
            } else {
                return (long) data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits float getFloat(long i)
    {
        if (ptr != 0) {
            return (Utilities.UNSAFE.getFloat(ptr + sizeof * i));
        } else {
            if (isConstant()) {
                return data[0];
            } else {
                return data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits double getDouble(long i)
    {
        if (ptr != 0) {
            return (double) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
        } else {
            if (isConstant()) {
                return (double) data[0];
            } else {
                return (double) data[(int) i];
            }
        }
    }

    @Override
    public @UnknownUnits float[] getData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits float[] out = new @UnknownUnits float[(int) length];
                for (int i = 0; i < length; i++) {
                    out[i] = data[0];
                }
                return out;
            } else {
                return data;
            }
        }
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
                    res[i] = data[i] != 0;

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
                    float v = Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                    out[idx++] = v != 0;
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[0] != 0;
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        float v = data[(int) i];
                        out[idx++] = v != 0;
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits byte[] getByteData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits byte[] out = new @UnknownUnits byte[(int) length];
                byte elem = (byte) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                @UnknownUnits byte[] res = new @UnknownUnits byte[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (byte) data[i];

                }
                return res;
            }
        }
    }

    @Override
    public @UnknownUnits byte[] getByteData(byte[] a, long startPos, long endPos, long step)
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
            @UnknownUnits byte[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits byte[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (byte) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (byte) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (byte) data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits short[] getShortData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits short[] out = new @UnknownUnits short[(int) length];
                short elem = (short) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                @UnknownUnits short[] res = new @UnknownUnits short[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (short) data[i];

                }
                return res;
            }
        }
    }

    @Override
    public @UnknownUnits short[] getShortData(short[] a, long startPos, long endPos, long step)
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
            @UnknownUnits short[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits short[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (short) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (short) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (short) data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits int[] getIntData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits int[] out = new @UnknownUnits int[(int) length];
                int elem = (int) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                @UnknownUnits int[] res = new @UnknownUnits int[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (int) data[i];

                }
                return res;
            }
        }
    }

    @Override
    public @UnknownUnits int[] getIntData(int[] a, long startPos, long endPos, long step)
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
            @UnknownUnits int[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits int[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (int) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (int) data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (int) data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits long[] getLongData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits long[] out = new @UnknownUnits long[(int) length];
                long elem = (long) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                @UnknownUnits long[] res = new @UnknownUnits long[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (long) data[i];

                }
                return res;
            }
        }
    }

    @Override
    public @UnknownUnits long[] getLongData(long[] a, long startPos, long endPos, long step)
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
            @UnknownUnits long[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits long[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (long) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (long) data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (long) data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits float[] getFloatData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits float[] out = new @UnknownUnits float[(int) length];
                float elem = (float) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                return data.clone();
            }
        }
    }

    @Override
    public @UnknownUnits float[] getFloatData(float[] a, long startPos, long endPos, long step)
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
            @UnknownUnits float[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits float[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[0];
                    }
                } else {

                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public @UnknownUnits double[] getDoubleData()
    {
        if (ptr != 0) {
            return null;
        } else {
            if (isConstant()) {
                if (length > getMaxSizeOf32bitArray()) return null;
                @UnknownUnits double[] out = new @UnknownUnits double[(int) length];
                double elem = (double) data[0];
                for (int i = 0; i < length; i++) {
                    out[i] = elem;
                }
                return out;
            } else {
                @UnknownUnits double[] res = new @UnknownUnits double[(int) length];
                for (int i = 0; i < length; i++) {
                    res[i] = (double) data[i];

                }
                return res;
            }
        }
    }

    @Override
    public @UnknownUnits double[] getDoubleData(double[] a, long startPos, long endPos, long step)
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
            @UnknownUnits double[] out;
            if (a != null && a.length >= len) {
                out = a;
            } else {
                out = new @UnknownUnits double[(int) len];
            }
            int idx = 0;
            if (ptr != 0) {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (double) Utilities.UNSAFE.getFloat(ptr + sizeof * i);
                }
            } else {
                if (isConstant()) {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (double) data[0];
                    }
                } else {
                    for (long i = startPos; i < endPos; i += step) {
                        out[idx++] = (double) data[(int) i];
                    }
                }
            }
            return out;
        }
    }

    @Override
    public void setToNative(long i, Object value)
    {
        Utilities.UNSAFE.putFloat(ptr + sizeof * i, (Float) value);
    }

    @Override
    public void setBoolean(long i, boolean value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, value == true ? 1.0f : 0.0f);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = value == true ? 1.0f : 0.0f;
        }
    }

    @Override
    public void setByte(long i, @UnknownUnits byte value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = (float) value;
        }
    }

    @Override
    public void setShort(long i, @UnknownUnits short value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = (float) value;
        }
    }

    @Override
    public void setInt(long i, @UnknownUnits int value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = (float) value;
        }
    }

    @Override
    public void setLong(long i, @UnknownUnits long value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = (float) value;
        }
    }

    @Override
    public void setFloat(long i, @UnknownUnits float value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = value;
        }
    }

    @Override
    public void setDouble(long i, @UnknownUnits double value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putFloat(ptr + sizeof * i, (float) value);
        } else {
            if (isConstant()) {
                throw new IllegalAccessError("Constant arrays cannot be modified.");
            }
            data[(int) i] = (float) value;
        }
    }
}
