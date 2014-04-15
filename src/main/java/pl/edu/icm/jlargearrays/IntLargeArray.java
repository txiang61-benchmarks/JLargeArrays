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
 * An array of ints that can store up to 2<SUP>63</SUP> elements.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class IntLargeArray extends LargeArray
{

    private static final long serialVersionUID = 86623276977976615L;
    private int[] data;

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     */
    public IntLargeArray(long length)
    {
        this(length, true);
    }

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     * @param zeroNativeMemory if true, then the native memory is zeroed.
     */
    public IntLargeArray(long length, boolean zeroNativeMemory)
    {
        this.type = LargeArrayType.INT;
        this.sizeof = 4;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value");
        }
        this.length = length;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(this.length * this.sizeof);
            if (zeroNativeMemory) {
                zeroNativeMemory();
            }
            Cleaner.create(this, new Deallocator(this.ptr, this.length, this.sizeof));
            MemoryCounter.increaseCounter(this.length * this.sizeof);
        } else {
            data = new int[(int) length];
        }
    }

    /**
     * Creates new instance of this class.
     *
     * @param data data array, this reference is used internally.
     */
    public IntLargeArray(int[] data)
    {
        this.type = LargeArrayType.INT;
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
    public IntLargeArray clone()
    {
        IntLargeArray v = new IntLargeArray(length, false);
        Utilities.arraycopy(this, 0, v, 0, length);
        return v;
    }

    @Override
    public Integer get(long i)
    {
        return getInt(i);
    }
    
    @Override
    public Integer getFromNative(long i)
    {
        return Utilities.UNSAFE.getInt(ptr + sizeof * i);
    }

    @Override
    public boolean getBoolean(long i)
    {
        if (ptr != 0) {
            return (Utilities.UNSAFE.getInt(ptr + sizeof * i)) != 0;
        } else {
            return data[(int) i] != 0;
        }
    }

    @Override
    public byte getByte(long i)
    {
        if (ptr != 0) {
            return (byte) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (byte) data[(int) i];
        }
    }

    @Override
    public short getShort(long i)
    {
        if (ptr != 0) {
            return (short) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (short) data[(int) i];
        }
    }

    @Override
    public int getInt(long i)
    {
        if (ptr != 0) {
            return Utilities.UNSAFE.getInt(ptr + sizeof * i);
        } else {
            return data[(int) i];
        }
    }

    @Override
    public long getLong(long i)
    {
        if (ptr != 0) {
            return (long) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (long) data[(int) i];
        }
    }

    @Override
    public float getFloat(long i)
    {
        if (ptr != 0) {
            return (float) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (float) data[(int) i];
        }
    }

    @Override
    public double getDouble(long i)
    {
        if (ptr != 0) {
            return (double) (Utilities.UNSAFE.getInt(ptr + sizeof * i));
        } else {
            return (double) data[(int) i];
        }
    }

    @Override
    public int[] getData()
    {
        if (ptr != 0) {
            return null;
        } else {
            return data;
        }
    }

    @Override
    public boolean[] getBooleanData()
    {
        if (ptr != 0) {
            return null;
        } else {
            boolean[] res = new boolean[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = data[i] != 0;

            }
            return res;
        }
    }

    @Override
    public boolean[] getBooleanData(boolean[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    int v = Utilities.UNSAFE.getInt(ptr + sizeof * i);
                    out[idx++] = v != 0;
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    int v = data[(int) i];
                    out[idx++] = v != 0;
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
            byte[] res = new byte[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (byte) data[i];

            }
            return res;
        }
    }

    @Override
    public byte[] getByteData(byte[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = (byte) Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (byte) data[(int) i];
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
            short[] res = new short[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (short) data[i];

            }
            return res;
        }
    }

    @Override
    public short[] getShortData(short[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = (short) Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (short) data[(int) i];
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
            return data.clone();
        }
    }

    @Override
    public int[] getIntData(int[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = data[(int) i];
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
            long[] res = new long[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (long) data[i];

            }
            return res;
        }
    }

    @Override
    public long[] getLongData(long[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = (long) Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (long) data[(int) i];
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
            float[] res = new float[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (float) data[i];

            }
            return res;
        }
    }

    @Override
    public float[] getFloatData(float[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = (float) Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (float) data[(int) i];
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
            double[] res = new double[(int) length];
            for (int i = 0; i < length; i++) {
                res[i] = (double) data[i];

            }
            return res;
        }
    }

    @Override
    public double[] getDoubleData(double[] a, long startPos, long endPos, long step)
    {
        if (startPos < 0 || startPos >= length) {
            throw new ArrayIndexOutOfBoundsException("startPos < 0 || startPos >= length");
        }
        if (endPos < 0 || endPos >= length || endPos < startPos) {
            throw new ArrayIndexOutOfBoundsException("endPos < 0 || endPos >= length || endPos < startPos");
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
                    out[idx++] = (double) Utilities.UNSAFE.getInt(ptr + sizeof * i);
                }
            } else {
                for (long i = startPos; i < endPos; i += step) {
                    out[idx++] = (double) data[(int) i];
                }
            }
            return out;
        }
    }
    
    @Override
    public void setToNative(long i, Object value)
    {
        Utilities.UNSAFE.putInt(ptr + sizeof * i, (Integer)value);
    }

    @Override
    public void setBoolean(long i, boolean value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, value == true ? 1 : 0);
        } else {
            data[(int) i] = value == true ? 1 : 0;
        }
    }

    @Override
    public void setByte(long i, byte value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setShort(long i, short value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setInt(long i, int value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, value);
        } else {
            data[(int) i] = value;
        }
    }

    @Override
    public void setLong(long i, long value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setFloat(long i, float value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }

    @Override
    public void setDouble(long i, double value)
    {
        if (ptr != 0) {
            Utilities.UNSAFE.putInt(ptr + sizeof * i, (int) value);
        } else {
            data[(int) i] = (int) value;
        }
    }
}
