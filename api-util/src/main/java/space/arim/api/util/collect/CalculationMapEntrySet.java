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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import space.arim.api.util.collect.helper.SetContainsHelper;
import space.arim.api.util.collect.helper.SetToArrayHelper;
import space.arim.api.util.collect.helper.UnmodifiableByDefaultSet;

/**
 * A dynamic entry set based on a backing map. <br>
 * Relies on {@link Map#keySet()} and {@link Map#get(Object)} to dynamically compute the collection. <br>
 * <br>
 * Specifications: <br>
 * * The set automatically reflects the state of the backing map.
 * Keys are fetched on call and mapped to a value to form an entry. <br>
 * * The set is an unmodifiable view. Writes are not permitted. <br>
 * * The iterator returned by {@link #iterator()} is immutable. <br>
 * * The entries returned by the iterator are immutable. <br>
 * * {@link #size()} and {@link #isEmpty()} redirect to the backing map.
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class CalculationMapEntrySet<K, V> extends MapRelatedCollection<Entry<K, V>, K, V> implements SetContainsHelper<Entry<K, V>>, SetToArrayHelper<Entry<K, V>>, UnmodifiableByDefaultSet<Entry<K, V>> {
	
	/**
	 * Creates an entry set from an original map from which entries are generated. <br>
	 * The entries are generated on transversal. The map's {@link Map#keySet()} is used for iteration. <br>
	 * 
	 * @param original the original, backing map
	 */
	public CalculationMapEntrySet(Map<K, V> original) {
		super(original);
	}
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		Map<K, V> original = getMap();
		return new ImmutableCalculationIterator<K, Entry<K, V>>(original.keySet().iterator(), (key) -> new ImmutableEntry<K, V>(key, original.get(key)));
	}

}
