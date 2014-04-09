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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Benchmarks.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class Benchmark 
{

    public static void writeToFile(long[] sizes, int[] nthreads, double[][] results, String file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version"));
            writer.newLine();
            writer.write(System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
            writer.newLine();
            writer.write("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
            writer.newLine();
            writer.write("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
            writer.newLine();
            writer.write("Number of threads: {");
            for(int th = 0; th < nthreads.length; th++) {
                if(th < nthreads.length - 1) {
                    writer.write(nthreads[th] + ",");
                }
                else {
                    writer.write(nthreads[nthreads.length - 1] + "}");
                }
            }
            writer.newLine();
            writer.write("Sizes: {");
            for(int i = 0; i < sizes.length; i++) {
                if(i < sizes.length - 1) {
                    writer.write(sizes[i] + ",");
                }
                else {
                    writer.write(sizes[sizes.length - 1] + "}");
                }
            }
            writer.newLine();
            writer.write("Timings: {");
            for(int th = 0; th < nthreads.length; th++) {
                writer.write("{");
                if(th < nthreads.length - 1) {
                    for(int i = 0; i < sizes.length; i++) {
                        if(i < sizes.length - 1) {
                            writer.write(results[th][i] + ",");
                        }
                        else {
                            writer.write(results[th][i] + "},");
                        }
                    }
                    writer.newLine();
                }
                else {
                    for(int i = 0; i < sizes.length; i++) {
                        if(i < sizes.length - 1) {
                            writer.write(results[th][i] + ",");
                        }
                        else {
                            writer.write(results[th][i] + "}}");
                        }
                    }
                }
            }

        } catch (IOException ex){
            ex.printStackTrace();
        }  
    }
    
    public static double[][] benchmarkJavaArraysByteSequential(long[] sizes, int[] nthreads, int iters, String file) {
        for(int i = 0; i < sizes.length; i++) {
            if(sizes[i] > Integer.MAX_VALUE - 4) {
                return null;
            }
        }
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        System.out.println("Benchmarking java arrays (bytes, sequentual)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final byte[] a = new byte[(int)sizes[i]];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final int firstIdx = (int)(j * k);
                        final int lastIdx = (int)((j == nt - 1) ? sizes[i] : firstIdx + k);
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int k = firstIdx; k < lastIdx; k++) {
                                    a[k] = 1;
                                    a[k] += 1;
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    
    public static double[][] benchmarkJavaArraysDoubleSequential(long[] sizes, int[] nthreads, int iters, String file) {
        for(int i = 0; i < sizes.length; i++) {
            if(sizes[i] > Integer.MAX_VALUE - 4) {
                return null;
            }
        }
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        System.out.println("Benchmarking java arrays (doubles, sequentual)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final double[] a = new double[(int)sizes[i]];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final int firstIdx = (int)(j * k);
                        final int lastIdx = (int)((j == nt - 1) ? sizes[i] : firstIdx + k);
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int k = firstIdx; k < lastIdx; k++) {
                                    a[k] = 1;
                                    a[k] += 1;
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    public static double[][] benchmarkJavaArraysByteRandom(long[] sizes, int[] nthreads, int iters, String file) {
        for(int i = 0; i < sizes.length; i++) {
            if(sizes[i] > Integer.MAX_VALUE - 4) {
                return null;
            }
        }

        final int[] randIdx = new int[(int)sizes[sizes.length - 1]];
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        Random r = new Random(0);
        System.out.println("generating random indices.");
        int max = (int)sizes[sizes.length - 1];
        for(int i = 0; i < max; i++) {
            randIdx[i] = (int)(r.nextDouble()*(max-1));
        }
        
        System.out.println("Benchmarking java arrays (bytes, random)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final byte[] a = new byte[(int)sizes[i]];
                final long size = sizes[i];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final int firstIdx = (int)(j * k);
                        final int lastIdx = (int)((j == nt - 1) ? size : firstIdx + k);
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int k = firstIdx; k < lastIdx; k++) {
                                    int idx = (int)(randIdx[k] % size);
                                    a[idx] = 1;
                                    a[idx] += 1;
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    
    public static double[][] benchmarkJavaArraysDoubleRandom(long[] sizes, int[] nthreads, int iters, String file) {
        for(int i = 0; i < sizes.length; i++) {
            if(sizes[i] > Integer.MAX_VALUE - 4) {
                return null;
            }
        }

        final int[] randIdx = new int[(int)sizes[sizes.length - 1]];
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        Random r = new Random(0);
        System.out.println("generating random indices.");
        int max = (int)sizes[sizes.length - 1];
        for(int i = 0; i < max; i++) {
            randIdx[i] = (int)(r.nextDouble()*(max-1));
        }
        
        System.out.println("Benchmarking java arrays (double, random)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final double[] a = new double[(int)sizes[i]];
                final long size = sizes[i];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final int firstIdx = (int)(j * k);
                        final int lastIdx = (int)((j == nt - 1) ? size : firstIdx + k);
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int k = firstIdx; k < lastIdx; k++) {
                                    int idx = (int)(randIdx[k] % size);
                                    a[idx] = 1.;
                                    a[idx] += 1.;
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    public static double[][] benchmarkJLargeArraysByteSequentual(long[] sizes, int[] nthreads, int iters, String file) {
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        System.out.println("Benchmarking JLargeArrays (bytes, sequentual)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final ByteLargeArray a = new ByteLargeArray(sizes[i]);
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final long firstIdx = j * k;
                        final long lastIdx = (j == nt - 1) ? sizes[i] : firstIdx + k;
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (long k = firstIdx; k < lastIdx; k++) {
                                    a.setByte(k, (byte)1);
                                    a.setByte(k, (byte)(a.getByte(k) + 1));
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    public static double[][] benchmarkJLargeArraysDoubleSequentual(long[] sizes, int[] nthreads, int iters, String file) {
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        System.out.println("Benchmarking JLargeArrays (doubles, sequentual)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final DoubleLargeArray a = new DoubleLargeArray(sizes[i]);
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final long firstIdx = j * k;
                        final long lastIdx = (j == nt - 1) ? sizes[i] : firstIdx + k;
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (long k = firstIdx; k < lastIdx; k++) {
                                    a.setDouble(k, 1.);
                                    a.setDouble(k, (a.getDouble(k) + 1.));
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
    public static double[][] benchmarkJLargeArraysByteRandom(long[] sizes, int[] nthreads, int iters, String file) {
        final int[] randIdx = new int[(int)sizes[sizes.length - 1]];
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        Random r = new Random(0);
        System.out.println("generating random indices.");
        int max = (int)sizes[sizes.length - 1];
        for(int i = 0; i < max; i++) {
            randIdx[i] = (int)(r.nextDouble()*(max-1));
        }
        System.out.println("Benchmarking JLargeArrays (bytes, random)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final ByteLargeArray a = new ByteLargeArray(sizes[i]);
                final int size = (int)sizes[i];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final long firstIdx = j * k;
                        final long lastIdx = (j == nt - 1) ? sizes[i] : firstIdx + k;
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (long k = firstIdx; k < lastIdx; k++) {
                                    long idx = randIdx[(int)k] % size;
                                    a.setByte(idx, (byte)1);
                                    a.setByte(idx, (byte)(a.getByte(idx) + 1));
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    
     public static double[][] benchmarkJLargeArraysDoubleRandom(long[] sizes, int[] nthreads, int iters, String file) {
        final int[] randIdx = new int[(int)sizes[sizes.length - 1]];
        double[][] results = new double[nthreads.length][sizes.length];
        long k;
        Random r = new Random(0);
        System.out.println("generating random indices.");
        int max = (int)sizes[sizes.length - 1];
        for(int i = 0; i < max; i++) {
            randIdx[i] = (int)(r.nextDouble()*(max-1));
        }
        System.out.println("Benchmarking JLargeArrays (doubles, random)");
        for(int th = 0; th < nthreads.length; th++) {
            int nt = nthreads[th];
            Thread[] threads = new Thread[nt];
            System.out.println("\tNumber of threads = " + nt);
            for(int i = 0; i < sizes.length; i++) {
                System.out.print("\tSize = " + sizes[i]);
                final DoubleLargeArray a = new DoubleLargeArray(sizes[i]);
                final int size = (int)sizes[i];
                double t = System.nanoTime();
                for(int it = 0; it < iters; it++) {
                    k = sizes[i] / nt;
                    for (int j = 0; j < nt; j++) {
                        final long firstIdx = j * k;
                        final long lastIdx = (j == nt - 1) ? sizes[i] : firstIdx + k;
                        threads[j] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (long k = firstIdx; k < lastIdx; k++) {
                                    long idx = randIdx[(int)k] % size;
                                    a.setDouble(idx, 1.);
                                    a.setDouble(idx, (a.getDouble(idx) + 1.));
                                }
                            }
                        });
                        threads[j].start();
                    }
                    try {
                      for (int j = 0; j < nt; j++) {  
                        threads[j].join();
                        threads[j] = null;
                      }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                results[th][i] = (System.nanoTime() - t) / 1000000000.0 / (double)iters;
                System.out.println(" : " + String.format("%.7f sec", results[th][i]));
            }
        }
        writeToFile(sizes, nthreads, results, file);
        return results;
    }
    

    public static void benchmarkByteSequential(long[] sizes, int[] nthreads, int iters, String directory) {
        benchmarkJavaArraysByteSequential(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "java_arrays_byte_sequential.txt");
        System.gc();
        benchmarkJLargeArraysByteSequentual(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "jlargearrays_byte_sequentual.txt");
    }
 
    public static void benchmarkDoubleSequential(long[] sizes, int[] nthreads, int iters, String directory) {
        benchmarkJavaArraysDoubleSequential(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "java_arrays_double_sequential.txt");
        System.gc();
        benchmarkJLargeArraysDoubleSequentual(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "jlargearrays_double_sequentual.txt");
    }
    
    public static void benchmarkByteRandom(long[] sizes, int[] nthreads, int iters, String directory) {
        benchmarkJavaArraysByteRandom(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "java_arrays_byte_random.txt");
        System.gc();
        benchmarkJLargeArraysByteRandom(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "jlargearrays_byte_random.txt");
    }

    public static void benchmarkDoubleRandom(long[] sizes, int[] nthreads, int iters, String directory) {
        benchmarkJavaArraysDoubleRandom(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "java_arrays_double_random.txt");
        System.gc();
        benchmarkJLargeArraysDoubleRandom(sizes, nthreads, iters, directory + System.getProperty("file.separator") + "jlargearrays_double_random.txt");
    }

    
    public static void main(String[] args) {
        final int smallSizesIters = 5;
        int initial_power_of_two_exp = 27;
        int final_power_of_two_exp = 30;
        int length = final_power_of_two_exp - initial_power_of_two_exp + 1;
        long[] smallSizes = new long[length];
        for(int i = 0; i < length; i++) {
            if(initial_power_of_two_exp+i == 31) {
                smallSizes[i] = (long)Math.pow(2, 31) - 4;
            }
            else {
                smallSizes[i] = (long)Math.pow(2, initial_power_of_two_exp+i);
            }
        }

        final int largeSizesIters = 1;
        initial_power_of_two_exp = 32;
        final_power_of_two_exp = 35;
        length = final_power_of_two_exp - initial_power_of_two_exp + 1;
        long[] largeSizes = new long[length];
        for(int i = 0; i < length; i++) {
            largeSizes[i] = (long)Math.pow(2, initial_power_of_two_exp+i); 
        }
        int[] threads = {1, 2, 4, 8, 16};
        
        LargeArray.setMaxSizeOf32bitArray(1);
        
        benchmarkByteSequential(smallSizes, threads, smallSizesIters, "/tmp/");
        benchmarkDoubleSequential(smallSizes, threads, smallSizesIters, "/tmp/");
//        benchmarkByteRandom(smallSizes, threads, smallSizesIters, "/tmp/");
//        benchmarkDoubleRandom(smallSizes, threads, smallSizesIters, "/tmp/");
    }

}