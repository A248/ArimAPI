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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator with no elements. <br>
 * Use {@link #create()} for instantiation. <br>
 * <br>
 * <b>Specifications</b>: Same as that of an empty iterator <br>
 * * {@link #hasNext()} always returns <code>false</code>. <br>
 * * {@link #next()} throws <code>NoSuchElementException</code> as specified. <br>
 * * {@link #remove()} is unimplemented, meaning it throws <code>UnsupportedOperationException</code>.
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public interface EmptyIterator<E> extends Iterator<E> {
	
	/**
	 * Creates an empty iterator
	 * 
	 * @param <E> the element type
	 * @return an empty iterator
	 */
	static <E> EmptyIterator<E> create() {
		return new EmptyIterator<E>() {};
	}
	
	@Override
	default boolean hasNext() {
		return false;
	}
	
	@Override
	default E next() {
		throw new NoSuchElementException();
	}
	
}
