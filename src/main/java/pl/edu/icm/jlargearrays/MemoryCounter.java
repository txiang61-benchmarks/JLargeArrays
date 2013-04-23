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
 * Utility for counting the amount of memory used by large arrays.
 *
 * @author Piotr Wendykier (p.wendykier@icm.edu.pl)
 */
public class MemoryCounter {
    private static long counter = 0;
    
    private MemoryCounter() {}
    
    public static long getCounter() {
        return counter;
    }
    
    public static void increaseCoutner(long x) {
        counter += x;
    }
    
    public static void decreaseCoutner(long x) {
        counter -= x;
        if(counter < 0) counter = 0;
    }
}
