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

import java.util.Collection;
import java.util.Set;

/**
 * Same as {@link CollectionContainsHelper}, but for a <code>Set</code>. <br>
 * Prevents method conflicts.
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public interface SetContainsHelper<E> extends CollectionContainsHelper<E>, Set<E> {
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	default boolean contains(Object o) {
		return CollectionContainsHelper.super.contains(o);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	default boolean containsAll(Collection<?> c) {
		return CollectionContainsHelper.super.containsAll(c);
	}
	
}
