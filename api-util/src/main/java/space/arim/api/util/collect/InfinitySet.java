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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a set which contains all possible elements. <br>
 * <br>
 * <b>Principles</b>: <br>
 * * All elements are contained within infinity. <br>
 * 1 Because infinity cannot be quantified, attempts to select individual elements will always fail. <br>
 * 2 Because infinity, when added to, remains infinity, attempts to add elements always indicate an unchanged set. <br>
 * 3 Infinity cannot be reduced in number or size. Subtraction has no effect. <br>
 * <br>
 * The specifications follow from the principles. <br>
 * <b>Specifications</b>: <br>
 * * {@link #size()} == <code>Integer.MAX_VALUE</code>, {@link #isEmpty()} is always <code>false</code>. (principle 1) <br>
 * * {@link #contains(Object)} and {@link #containsAll(Collection)} always return <code>true</code>. (principle 1) <br>
 * * {@link #iterator()} returns an empty iterator. (principle 2) <br>
 * * {@link #toArray()} and {@link #toArray(Object[])} return empty arrays. (principle 2) <br>
 * * {@link #add(Object)} and {@link #addAll(Collection)} always return <code>false</code>. (principle 3) <br>
 * * {@link #remove(Object)}, {@link #removeAll(Collection)}, and {@link #retainAll(Collection)} always return <code>true</code>. (principles 1 and 4) <br>
 * * {@link #clear()} does nothing. (principle 4)
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public class InfinitySet<E> implements Set<E> {
	
	@Override
	public int size() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean contains(Object o) {
		return true;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new EmptyIterator<E>();
	}
	
	@Override
	public Object[] toArray() {
		return new Object[] {};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		return (T[]) Array.newInstance(a.getClass().getComponentType(), 0);
	}
	
	@Override
	public boolean add(E e) {
		return false;
	}
	
	@Override
	public boolean remove(Object o) {
		return true;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return true;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return true;
	}
	
	@Override
	public void clear() {
		
	}

}
