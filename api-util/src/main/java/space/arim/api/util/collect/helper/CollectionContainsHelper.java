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

/**
 * Adds default implementations of {@link #contains(Object)} and {@link #containsAll(Collection)},
 * both of which rely on {@link #iterator()} to transverse the collection and check for matches.
 * 
 * @author A248
 *
 * @param <E> the element type
 * 
 * @deprecated See deprecation of {@link space.arim.api.util.collect.helper}
 */
@Deprecated(forRemoval = true)
public interface CollectionContainsHelper<E> extends Collection<E> {
	
	@Override
	default boolean contains(Object o) {
		return DefaultImplementations.iteratorContains(iterator(), o);
	}
	
	@Override
	default boolean containsAll(Collection<?> c) {
		return DefaultImplementations.iteratorContainsAll(iterator(), c);
	}
	
}
