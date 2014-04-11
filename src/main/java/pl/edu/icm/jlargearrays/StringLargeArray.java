/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.jlargearrays;

import java.io.UnsupportedEncodingException;
import static pl.edu.icm.jlargearrays.LargeArray.LARGEST_32BIT_INDEX;
import sun.misc.Cleaner;

/**
 *
 * An array of strings that can store up to 2<SUP>63</SUP> elements.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class StringLargeArray extends LargeArray
{

    private static final long serialVersionUID = -4096759496772248522L;
    private String[] data;
    private ShortLargeArray stringLengths;
    private int maxStringLength;
    private byte[] byteArray;
    private static final String CHARSET = "UTF-8";
    private static final int CHARSET_SIZE = 4; //UTF-8 uses between 1 and 4 bytes to encode a single character 

    /**
     * Creates new instance of this class. The maximal string length is set to 100.
     *
     * @param length number of elements
     */
    public StringLargeArray(long length)
    {
        this(length, true, 100);
    }

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     * @param maxStringLength maximal length of the string, it is ignored when number of elements is smaller than LARGEST_32BIT_INDEX
     */
    public StringLargeArray(long length, int maxStringLength)
    {
        this(length, true, maxStringLength);
    }

    /**
     * Creates new instance of this class.
     *
     * @param length number of elements
     * @param zeroNativeMemory if true, then the native memory is zeroed.
     * @param maxStringLength maximal length of the string, it is ignored when number of elements is smaller than LARGEST_32BIT_INDEX
     */
    public StringLargeArray(long length, boolean zeroNativeMemory, int maxStringLength)
    {
        this.type = LargeArrayType.STRING;
        this.sizeof = 1;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value.");
        }
        if (maxStringLength <= 0) {
            throw new IllegalArgumentException(maxStringLength + " is not a positive int value.");
        }
        this.length = length * (long) maxStringLength * (long) CHARSET_SIZE;
        this.maxStringLength = maxStringLength;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(this.length * this.sizeof);
            if (zeroNativeMemory) {
                zeroNativeMemory();
            }
            Cleaner.create(this, new Deallocator(this.ptr, this.length, this.sizeof));
            MemoryCounter.increaseCounter(this.length * this.sizeof);
            stringLengths = new ShortLargeArray(length);
            byteArray = new byte[maxStringLength * CHARSET_SIZE];
        } else {
            data = new String[(int) length];
        }
    }

    /**
     * Creates new instance of this class.
     *
     * @param data data array, this reference is used internally.
     */
    public StringLargeArray(String[] data)
    {
        this.type = LargeArrayType.STRING;
        this.sizeof = 1;
        this.length = data.length;
        this.data = data;
    }

    @Override
    public String get(long i)
    {
        if (ptr != 0) {
            short strLen = stringLengths.getShort(i);
            long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
            for (int j = 0; j < strLen; j++) {
                byteArray[j] = Utilities.UNSAFE.getByte(ptr + offset + sizeof * j);
            }
            try {
                return new String(byteArray, 0, strLen, CHARSET);
            } catch (UnsupportedEncodingException ex) {
                return null;
            }
        } else {
            return data[(int) i];
        }
    }

    @Override
    public String getFromNative(long i)
    {
        short strLen = stringLengths.getShort(i);
        long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
        for (int j = 0; j < strLen; j++) {
            byteArray[j] = Utilities.UNSAFE.getByte(ptr + offset + sizeof * j);
        }
        try {
            return new String(byteArray, 0, strLen, CHARSET);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    @Override
    public boolean getBoolean(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByte(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getShort(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloat(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDouble(long i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getData()
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean[] getBooleanData(boolean[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getByteData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getByteData(byte[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short[] getShortData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short[] getShortData(short[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getIntData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getIntData(int[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long[] getLongData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long[] getLongData(long[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float[] getFloatData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float[] getFloatData(float[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[] getDoubleData()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[] getDoubleData(double[] a, long startPos, long endPos, long step)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setToNative(long i, Object value)
    {
        String s = (String) value;
        if (s.length() > maxStringLength) {
            throw new IllegalArgumentException("String  " + s + " is too long.");
        }
        byte[] tmp;
        try {
            tmp = s.getBytes(CHARSET);
        } catch (UnsupportedEncodingException ex) {
            return;
        }
        int strLen = tmp.length;
        if (strLen > Short.MAX_VALUE) {
            throw new IllegalArgumentException("String  " + s + " is too long.");
        }
        stringLengths.setShort(i, (short) strLen);
        long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
        for (int j = 0; j < strLen; j++) {
            Utilities.UNSAFE.putByte(ptr + offset + sizeof * j, tmp[j]);
        }
    }

    @Override
    public void set(long i, Object o)
    {
        if (!(o instanceof String)) {
            throw new IllegalArgumentException(o + " is not a string.");
        }
        String s = (String) o;
        if (s.length() > maxStringLength) {
            throw new IllegalArgumentException("String  " + s + " is too long.");
        }
        if (ptr != 0) {
            byte[] tmp;
            try {
                tmp = s.getBytes(CHARSET);
            } catch (UnsupportedEncodingException ex) {
                return;
            }
            int strLen = tmp.length;
            if (strLen > Short.MAX_VALUE) {
                throw new IllegalArgumentException("String  " + s + " is too long.");
            }
            stringLengths.setShort(i, (short) strLen);
            long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
            for (int j = 0; j < strLen; j++) {
                Utilities.UNSAFE.putByte(ptr + offset + sizeof * j, tmp[j]);
            }
        } else {
            data[(int) i] = s;
        }
    }

    @Override
    public void set_safe(long i, Object o)
    {
        if (i < 0 || i >= length) {
            throw new ArrayIndexOutOfBoundsException(Long.toString(i));
        }
        if (!(o instanceof String)) {
            throw new IllegalArgumentException(o + " is not a string.");
        }
        String s = (String) o;
        if (s.length() > maxStringLength) {
            throw new IllegalArgumentException("String  " + s + " is too long.");
        }
        if (ptr != 0) {
            byte[] tmp;
            try {
                tmp = s.getBytes(CHARSET);
            } catch (UnsupportedEncodingException ex) {
                return;
            }
            int strLen = tmp.length;
            if (strLen > Short.MAX_VALUE) {
                throw new IllegalArgumentException("String  " + s + " is too long.");
            }
            stringLengths.setShort(i, (short) strLen);
            long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
            for (int j = 0; j < strLen; j++) {
                Utilities.UNSAFE.putByte(ptr + offset + sizeof * j, tmp[j]);
            }
        } else {
            data[(int) i] = s;
        }
    }

    @Override
    public void setBoolean(long i, boolean value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByte(long i, byte value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setShort(long i, short value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInt(long i, int value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLong(long i, long value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFloat(long i, float value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDouble(long i, double value)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
