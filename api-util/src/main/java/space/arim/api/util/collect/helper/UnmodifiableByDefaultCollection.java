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
 * Used as a helper for creating unmodifiable collections. <br>
 * <b>Do not assume that instances of this interface are themselves immutable! Methods can be overriden!</b> <br>
 * <br>
 * Modification operations throw <code>UnsupportedOperationException</code> by default: <br>
 * * {@link #add(Object)} <br>
 * * {@link #remove(Object)} <br>
 * * {@link #addAll(Collection)} <br>
 * * {@link #retainAll(Collection)} <br>
 * * {@link #removeAll(Collection)} <br>
 * * {@link #clear()} <br>
 * <br>
 * <b>NOTICE:</b> {@link #iterator()} is not implemented by default as unmodifiable!
 * Programmers implementing this interface must create their own unmodifiable iterator implementations.
 * 
 * @author A248
 *
 * @param <E> the element type
 * 
 * @deprecated See deprecation of {@link space.arim.api.util.collect.helper}
 */
@Deprecated(forRemoval = true)
public interface UnmodifiableByDefaultCollection<E> extends Collection<E> {
	
	@Override
	default boolean add(E e) {throw new UnsupportedOperationException();}
	@Override
	default boolean remove(Object o) {throw new UnsupportedOperationException();}
	@Override
	default boolean addAll(Collection<? extends E> c) {throw new UnsupportedOperationException();}
	@Override
	default boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override
	default boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override
	default void clear() {throw new UnsupportedOperationException();}
	
}
