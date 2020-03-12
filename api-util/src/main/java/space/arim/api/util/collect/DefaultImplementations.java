/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

class DefaultImplementations {
	
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	static boolean equalsWithNullCheck(Object obj1, Object obj2) {
		return (obj1 == null) ? obj2 == null : obj1.equals(obj2);
	}
	
	static <E> boolean iteratorContains(Iterator<E> it, Object element) {
		while (it.hasNext()) {
			if (equalsWithNullCheck(element, it.next())) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	static <E> boolean iteratorContainsAll(Iterator<E> it, Collection<?> elements) {
		HashSet<?> checkFor = new HashSet<>(elements);
		while (it.hasNext()) {
			checkFor.remove(it.next());
		}
		return checkFor.isEmpty();
	}
	
	static <E> Object[] toArray(Collection<E> c) {
		// Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[c.size()];
        Iterator<E> it = c.iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
	}
	
	@SuppressWarnings("unchecked")
	static <E, T> T[] toArray(Collection<E> c, T[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
        int size = c.size();
		T[] r = a.length >= size ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = c.iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    return Arrays.copyOf(r, i);
                } else {
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T) it.next();
        }
        // more elements than expected
        return it.hasNext() ? DefaultImplementations.finishToArray(r, it) : r;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
		int i = r.length;
		while (it.hasNext()) {
			int cap = r.length;
			if (i == cap) {
				int newCap = cap + (cap >> 1) + 1;
				// overflow-conscious code
				if (newCap - MAX_ARRAY_SIZE > 0) {
					newCap = hugeCapacity(cap + 1);
				}
				r = Arrays.copyOf(r, newCap);
			}
			r[i++] = (T) it.next();
		}
		// trim if overallocated
		return (i == r.length) ? r : Arrays.copyOf(r, i);
	}
	
	private static int hugeCapacity(int minCapacity) {
		if (minCapacity < 0) { // overflow
			throw new OutOfMemoryError("Required array size too large");
		}
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}

}
