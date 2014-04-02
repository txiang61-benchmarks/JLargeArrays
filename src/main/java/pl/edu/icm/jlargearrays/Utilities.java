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

import java.lang.reflect.Field;

/**
 *
 * Utilities.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class Utilities
{

    /**
     * An object for performing low-level, unsafe operations.
     */
    public static final sun.misc.Unsafe UNSAFE;

    static {
        Object theUnsafe = null;
        Exception exception = null;
        try {
            Class<?> uc = Class.forName("sun.misc.Unsafe");
            Field f = uc.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theUnsafe = f.get(uc);
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (IllegalArgumentException e) {
            exception = e;
        } catch (NoSuchFieldException e) {
            exception = e;
        } catch (SecurityException e) {
            exception = e;
        }
        UNSAFE = (sun.misc.Unsafe) theUnsafe;
        if (UNSAFE == null) {
            throw new Error("Could not obtain access to sun.misc.Unsafe", exception);
        }
    }

    private Utilities()
    {
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * Both arrays need to be of the same type. It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final LargeArray src, final long srcPos, final LargeArray dest, final long destPos, final long length)
    {
        if (src.getType() != dest.getType()) {
            throw new IllegalArgumentException("The type of source array is different than the type of destimation array.");
        }
        switch (src.getType()) {
            case BIT:
                arraycopy((BitLargeArray) src, srcPos, (BitLargeArray) dest, destPos, length);
                break;
            case BYTE:
                arraycopy((ByteLargeArray) src, srcPos, (ByteLargeArray) dest, destPos, length);
                break;
            case SHORT:
                arraycopy((ShortLargeArray) src, srcPos, (ShortLargeArray) dest, destPos, length);
                break;
            case INT:
                arraycopy((IntLargeArray) src, srcPos, (IntLargeArray) dest, destPos, length);
                break;
            case LONG:
                arraycopy((LongLargeArray) src, srcPos, (LongLargeArray) dest, destPos, length);
                break;
            case FLOAT:
                arraycopy((FloatLargeArray) src, srcPos, (FloatLargeArray) dest, destPos, length);
                break;
            case DOUBLE:
                arraycopy((DoubleLargeArray) src, srcPos, (DoubleLargeArray) dest, destPos, length);
                break;
            default:
                throw new IllegalArgumentException("Invalid array type.");
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final BitLargeArray src, final long srcPos, final BitLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setByte(j, src.getByte(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setByte(destPos + k, src.getByte(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setByte(j, src.getByte(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final boolean[] src, final int srcPos, final BitLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setBoolean(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setBoolean(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setBoolean(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final ByteLargeArray src, final long srcPos, final ByteLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setByte(j, src.getByte(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setByte(destPos + k, src.getByte(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setByte(j, src.getByte(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final byte[] src, final int srcPos, final ByteLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setByte(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setByte(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setByte(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final ShortLargeArray src, final long srcPos, final ShortLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setShort(j, src.getShort(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setShort(destPos + k, src.getShort(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setShort(j, src.getShort(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final short[] src, final int srcPos, final ShortLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setShort(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setShort(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setShort(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final IntLargeArray src, final long srcPos, final IntLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setInt(j, src.getInt(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setInt(destPos + k, src.getInt(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setInt(j, src.getInt(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final int[] src, final int srcPos, final IntLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setInt(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setInt(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setInt(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final LongLargeArray src, final long srcPos, final LongLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setLong(j, src.getLong(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setLong(destPos + k, src.getLong(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setLong(j, src.getLong(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final long[] src, final int srcPos, final LongLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setLong(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setLong(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setLong(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final FloatLargeArray src, final long srcPos, final FloatLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setFloat(j, src.getFloat(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setFloat(destPos + k, src.getFloat(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setFloat(j, src.getFloat(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final float[] src, final int srcPos, final FloatLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setFloat(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setFloat(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setFloat(j, src[i++]);
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final DoubleLargeArray src, final long srcPos, final DoubleLargeArray dest, final long destPos, final long length)
    {
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                dest.setDouble(j, src.getDouble(i));
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setDouble(destPos + k, src.getDouble(srcPos + k));
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
                for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
                    dest.setDouble(j, src.getDouble(i));
                }
            }
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * It does not check array bounds.
     *
     * @param src the source array.
     * @param srcPos starting position in the source array.
     * @param dest the destination array.
     * @param destPos starting position in the destination data.
     * @param length the number of array elements to be copied.
     */
    public static void arraycopy(final double[] src, final int srcPos, final DoubleLargeArray dest, final long destPos, final long length)
    {
        int i = srcPos;
        int nthreads = Runtime.getRuntime().availableProcessors();
        if (nthreads < 2) {
            for (long j = destPos; j < destPos + length; j++) {
                dest.setDouble(j, src[i++]);
            }
        } else {
            long k = length / nthreads;
            Thread[] threads = new Thread[nthreads];
            for (int j = 0; j < nthreads; j++) {
                final long firstIdx = j * k;
                final long lastIdx = (j == nthreads - 1) ? length : firstIdx + k;
                threads[j] = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (long k = firstIdx; k < lastIdx; k++) {
                            dest.setDouble(destPos + k, src[srcPos + (int) k]);
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
                for (long j = destPos; j < destPos + length; j++) {
                    dest.setDouble(j, src[i++]);
                }
            }
        }
    }
}
