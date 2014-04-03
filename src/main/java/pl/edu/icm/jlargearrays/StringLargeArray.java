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
 * @author piotrw
 */
public class StringLargeArray extends LargeArray
{
    
    private String[] data;
    private IntLargeArray stringLengths;
    private int maxStringLength;
    private byte[] byteArray;
    private static final String CHARSET = "UTF-8";
    private static final int CHARSET_SIZE = 4; //UTF-8 uses between 1 and 4 bytes to encode a single character 

    
    public StringLargeArray(long length, int maxStringLength) {
        this.type = LargeArrayType.STRING;
        this.sizeof = 1;
        if (length <= 0) {
            throw new IllegalArgumentException(length + " is not a positive long value.");
        }
        if (maxStringLength <= 0) {
            throw new IllegalArgumentException(maxStringLength + " is not a positive int value.");
        }
        this.length = length * (long)maxStringLength * (long)CHARSET_SIZE;
        this.maxStringLength = maxStringLength;
        if (length > LARGEST_32BIT_INDEX) {
            System.gc();
            this.ptr = Utilities.UNSAFE.allocateMemory(this.length * this.sizeof);
            zeroMemory();
            Cleaner.create(this, new Deallocator(this.ptr, this.length, this.sizeof));
            MemoryCounter.increaseCounter(this.length * this.sizeof);
            stringLengths = new IntLargeArray(length);
            byteArray = new byte[maxStringLength*CHARSET_SIZE];
        } else {
            data = new String[(int) length];
        }
    }

    public StringLargeArray(String[] data) {
        this.type = LargeArrayType.STRING;
        this.sizeof = 1;
        this.length = data.length;
        this.data = data;
    }
    
    @Override
    public String get(long i)
    {
        if (isLarge()) {
            int strLen = stringLengths.getInt(i);
            long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
            for(int j = 0; j < strLen; j++) {
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
    public Object getData()
    {
        if (isLarge()) {
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
    public void set(long i, Object o)
    {
        if(!(o instanceof String)) {
            throw new IllegalArgumentException(o + " is not a string.");
        }
        String s = (String)o;
        if(s.length() > maxStringLength) {
            throw new IllegalArgumentException("String  " + s + " is too long.");
        }
        if (isLarge()) {
            byte[] tmp;
            try {
                tmp = s.getBytes(CHARSET);
            } catch (UnsupportedEncodingException ex) {
                return;
            }
            int strLen = tmp.length;
            stringLengths.setInt(i, strLen);
            long offset = sizeof * i * maxStringLength * CHARSET_SIZE;
            for(int j = 0; j < strLen; j++) {
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
