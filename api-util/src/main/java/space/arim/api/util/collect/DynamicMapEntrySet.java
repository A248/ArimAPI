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
import java.util.Map.Entry;

/**
 * A dynamic entry set based on a backing map. <br>
 * Relies on {@link Map#keySet()} and {@link Map#get(Object)} to dynamically generate the collection. <br>
 * <br>
 * Specifications: <br>
 * * The set automatically reflects the state of the backing map. Keys are fetched on call. <br>
 * * The set is an unmodifiable view. Writes are not permitted. <br>
 * * The iterator returned by {@link #iterator()} is immutable. <br>
 * * The entries returned by the iterator are immutable. <br>
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
public class DynamicMapEntrySet<K, V> extends DynamicMapCollectionHelper<Entry<K, V>, K, V> implements SetContainsHelper<Entry<K, V>>, SetToArrayHelper<Entry<K, V>>, UnmodifiableByDefaultSet<Entry<K, V>> {
	
	/**
	 * Creates an entry set from an original map from which entries are generated. <br>
	 * The entries are generated on transversal. The map's {@link Map#keySet()} is used for iteration. <br>
	 * 
	 * @param original the original, backing map
	 */
	public DynamicMapEntrySet(Map<K, V> original) {
		super(original);
	}
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		Map<K, V> original = getMap();
		return new ImmutableKeyMappingIterator<K, Entry<K, V>>(original.keySet().iterator(), (key) -> new ImmutableEntry<K, V>(key, original.get(key)));
	}

}
