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

/**
 * The base class for all large arrays. 
 * All implementations of this abstract class can store up to 2^63 elements of primitive data types.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public abstract class LargeArray implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 7921589398878016801L;
    protected LargeArrayType type;
    protected long length;
    protected long sizeof;
    protected long ptr = 0;
    /**
     * Largest array size for which a regular 1D Java array is used to store the
     * data.
     */
    protected static int LARGEST_32BIT_INDEX = 1073741824; //2^30;

    /**
     * Returns the length of an array.
     *
     * @return the length of an array
     */
    public long length() {
        return length;
    }

    /**
     * Returns the type of an array.
     *
     * @return the type of an array
     */
    public LargeArrayType getType() {
        return type;
    }

    /**
     * Returns a boolean value at index i.
     * 
     * @param i an index
     * @return a boolean value at index i.
     */
    public abstract boolean getBoolean(long i);

    /**
     * Returns a byte value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract byte getByte(long i);

    /**
     * Returns a short value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract short getShort(long i);

    /**
     * Returns an int value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract int getInt(long i);

   /**
     * Returns a long value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract long getLong(long i);

    /**
     * Returns a float value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract float getFloat(long i);

    /**
     * Returns a double value at index i.
     * 
     * @param i an index
     * @return a value at index i.
     */
    public abstract double getDouble(long i);

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns boolean data. Otherwise, it returns null.
     * 
     * @return boolean data or null.
     */
    public abstract boolean[] getBooleanData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns byte data. Otherwise, it returns null.
     * 
     * @return byte data or null.
     */
    public abstract byte[] getByteData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns short data. Otherwise, it returns null.
     * 
     * @return short data or null.
     */
    public abstract short[] getShortData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns int data. Otherwise, it returns null.
     * 
     * @return int data or null.
     */
    public abstract int[] getIntData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns long data. Otherwise, it returns null.
     * 
     * @return long data or null.
     */
    public abstract long[] getLongData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns float data. Otherwise, it returns null.
     * 
     * @return float data or null.
     */
    public abstract float[] getFloatData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns double data. Otherwise, it returns null.
     * 
     * @return double data or null.
     */
    public abstract double[] getDoubleData();

    /**
     * Sets a boolean value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setBoolean(long i, boolean value);

    /**
     * Sets a byte value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setByte(long i, byte value);

    /**
     * Sets a short value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setShort(long i, short value);

    /**
     * Sets an int value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setInt(long i, int value);

    /**
     * Sets a long value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setLong(long i, long value);

    /**
     * Sets a float value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setFloat(long i, float value);

    /**
     * Sets a double value at index i.
     * 
     * @param i index 
     * @param value value to set
     */
    public abstract void setDouble(long i, double value);

    /**
     * Returns true if the size od an array is larger than LARGEST_32BIT_INDEX.
     * @return true if the size od an array is larger than LARGEST_32BIT_INDEX, false otherwise.
     */
    public boolean isLarge() {
        return length > LARGEST_32BIT_INDEX && ptr != 0;
    }
    
    /**
     * Sets the maximal size of a 32-bit array. For arrays of the size larger than index, the data is stored in the memory allocated by sun.misc.Unsafe.allocateMemory().
     * @param index the maximal size of a 32-bit array.
     */
    public static void setMaxSizeOf32bitArray(int index) {
        if(index < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        LARGEST_32BIT_INDEX = index;
    }

    /**
     * Returns the maximal size of a 32-bit array. 
     * @return the maximal size of a 32-bit array.
     */
    public static int getMaxSizeOf32bitArray() {
        return LARGEST_32BIT_INDEX;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException exc) {
            throw new InternalError(); // should never happen
        }
    }
    
    /**
     * Memory deallocator.
     */
    protected static class Deallocator implements Runnable {

        private long ptr;
        private final long length;
        private final long sizeof;

        public Deallocator(long ptr, long length, long sizeof) {
            this.ptr = ptr;
            this.length = length;
            this.sizeof = sizeof;
        }

        @Override
        public void run() {
            if (length > LARGEST_32BIT_INDEX && ptr != 0) {
                Utilities.UNSAFE.freeMemory(ptr);
                ptr = 0;
                MemoryCounter.decreaseCounter(length * sizeof);
            }
        }
    }

    /**
     * Initializes allocated memory to zero.
     */
    protected void zeroMemory() {
        if (isLarge()) {
            int nthreads = Runtime.getRuntime().availableProcessors();
            if (nthreads <= 2) {
                Utilities.UNSAFE.setMemory(ptr, length * sizeof, (byte) 0);
            } else {
                long k = length / nthreads;
                Thread[] threads = new Thread[nthreads];
                final long ptrf = ptr;
                for (int j = 0; j < nthreads; j++) {
                    final long firstIdx = j * k;
                    final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                    threads[j] = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (long k = firstIdx; k < lastIdx; k++) {
                                Utilities.UNSAFE.putByte(ptrf + sizeof * k, (byte) 0);
                            }
                        }
                    });
                    threads[j].start();
                }
                try {
                  for (int j = 0; j < nthreads; j++) {  
                    threads[j].join();
                    threads[j] = null;
                  }
                } catch (InterruptedException ex) {
                    Utilities.UNSAFE.setMemory(ptr, length * sizeof, (byte) 0);
                }
            }
        }
    }
}
