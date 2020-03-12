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
public class DynamicMapValues<K, V> extends DynamicMapCollectionHelper<V, K, V> implements CollectionContainsHelper<V>, CollectionToArrayHelper<V>, UnmodifiableByDefaultCollection<V> {
	
	public DynamicMapValues(Map<K, V> original) {
		super(original);
	}
	
	@Override
	public Iterator<V> iterator() {
		Map<K, V> original = getMap();
		return new ImmutableKeyMappingIterator<K, V>(original.keySet().iterator(), original::get);
	}

}
