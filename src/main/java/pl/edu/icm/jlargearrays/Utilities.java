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

import java.lang.reflect.Field;

/**
*
* Utilities.
*
* @author Piotr Wendykier (p.wendykier@icm.edu.pl)
*/
public class Utilities {
    
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
        } catch (Exception e) {
            exception = e;
        }
        UNSAFE = (sun.misc.Unsafe) theUnsafe;
        if (UNSAFE == null) {
            throw new Error("Could not obtain access to sun.misc.Unsafe", exception);
        }
    }

    private Utilities() {
    }
    
    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(BooleanLargeArray src, long srcPos, BooleanLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setBoolean(j, src.getBoolean(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(ByteLargeArray src, long srcPos, ByteLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setByte(j, src.getByte(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(ShortLargeArray src, long srcPos, ShortLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setShort(j, src.getShort(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(IntLargeArray src, long srcPos, IntLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setInt(j, src.getInt(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(LongLargeArray src, long srcPos, LongLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setLong(j, src.getLong(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(FloatLargeArray src, long srcPos, FloatLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setFloat(j, src.getFloat(i));
        }
    }

    /**
     * Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
     * 
     * @param      src      the source array.
     * @param      srcPos   starting position in the source array.
     * @param      dest     the destination array.
     * @param      destPos  starting position in the destination data.
     * @param      length   the number of array elements to be copied.
     */
    public static void arraycopy(DoubleLargeArray src, long srcPos, DoubleLargeArray dest, long destPos, long length) {
        for (long i = srcPos, j = destPos; i < srcPos + length; i++, j++) {
            dest.setDouble(j, src.getDouble(i));
        }
    }
 }
