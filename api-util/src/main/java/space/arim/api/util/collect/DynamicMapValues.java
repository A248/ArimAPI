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
import java.util.Iterator;
import java.util.Map;

/**
 * A value collection based on a backing map. <br>
 * Relies on {@link Map#keySet()} and {@link Map#get(Object)} to dynamically generate the collection. <br>
 * <br>
 * Specifications: <br>
 * * The collection automatically reflects the state of the backing map. Values are fetched on call. <br>
 * * The collection is an unmodifiable view. Writes are not permitted. <br>
 * * The iterator returned by {@link #iterator()} is immutable. <br>
 * <br>
 * Notes: <br>
 * * {@link #size()} and {@link #isEmpty()} redirect to the backing map. <br>
 * * {@link #containsAll(Collection)} does not rely on {@link #contains(Object)}. Both rely on {@link #iterator()}. One or the other may be overriden.
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DynamicMapValues<K, V> implements CollectionContainsHelper<V>, CollectionToArrayHelper<V>, UnmodifiableByDefaultCollection<V> {
	
	private final Map<K, V> original;
	
	public DynamicMapValues(Map<K, V> original) {
		this.original = original;
	}
	
	@Override
	public int size() {
		return original.size();
	}
	
	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}
	
	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {
			
			private final Iterator<K> keyIterator = original.keySet().iterator();
			
			@Override
			public boolean hasNext() {
				return keyIterator.hasNext();
			}
			
			@Override
			public V next() {
				return original.get(keyIterator.next());
			}
			
		};
	}

}
