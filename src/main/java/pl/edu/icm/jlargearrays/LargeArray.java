/* ***** BEGIN LICENSE BLOCK *****
 * 
 * JLargeArrays is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * JLargeArrays is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this Module; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
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
    public static final long LARGEST_32BIT_INDEX = 1073741824; //2^30;

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
    public abstract boolean[] getBoolData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns byte data. Otherwise, it returns null.
     * 
     * @return byte data or null.
     */
    public abstract byte[] getBData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns short data. Otherwise, it returns null.
     * 
     * @return short data or null.
     */
    public abstract short[] getSData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns int data. Otherwise, it returns null.
     * 
     * @return int data or null.
     */
    public abstract int[] getIData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns long data. Otherwise, it returns null.
     * 
     * @return long data or null.
     */
    public abstract long[] getLData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns float data. Otherwise, it returns null.
     * 
     * @return float data or null.
     */
    public abstract float[] getFData();

    /**
     * If the size of the array is smaller than LARGEST_32BIT_INDEX, then this method returns double data. Otherwise, it returns null.
     * 
     * @return double data or null.
     */
    public abstract double[] getDData();

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
        if (length > LARGEST_32BIT_INDEX && ptr != 0) {
            return true;
        } else {
            return false;
        }
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
        private long length;
        private long sizeof;

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
                MemoryCounter.decreaseCoutner(length * sizeof);
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
                } catch (Exception ex) {
                    Utilities.UNSAFE.setMemory(ptr, length * sizeof, (byte) 0);
                }
            }
        }
    }
}
