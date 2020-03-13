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
package space.arim.api.util.collect.helper;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Adds default implementations of {@link #toArray()} and {@link #toArray(Object[])}
 * which simply return blank arrays of the relevant types.
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public interface CollectionToEmptyArrayHelper<E> extends Collection<E> {
	
	@Override
	default Object[] toArray() {
		return new Object[] {};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	default <T> T[] toArray(T[] a) {
		return (T[]) Array.newInstance(a.getClass().getComponentType(), 0);
	}
	
}
