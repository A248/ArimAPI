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

import java.util.Collection;

/**
 * An unmodifiable version of {@link InfinitySet}. <br>
 * The only difference is that modification methods throw <code>UnsupportedOperationException</code>
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public interface UnmodifiableInfinitySet<E> extends InfinitySet<E> {
	
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
