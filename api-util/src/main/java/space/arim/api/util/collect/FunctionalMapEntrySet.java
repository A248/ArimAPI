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
import java.util.Map.Entry;

import space.arim.api.util.collect.helper.SetEmptyHelper;
import space.arim.api.util.collect.helper.UnmodifiableByDefaultSet;

/**
 * The corresponding entry set for {@link FunctionalMap}. <br>
 * The specifications follow from the principles of a functional map. <br>
 * <br>
 * <b>Specifications</b>: <br>
 * * The set contains an object if and only if the object is an <code>Entry</code> and
 * <code>map.get(entry.getKey())</code> equals <code>entry.getValue</code> or both are null
 * (where <code>entry</code> is the object/entry in question and <code>map</code> is the map).
 * Essentially, the set contains an entry if and only if the key maps to the value according to the map. (principle 3) <br>
 * * {@link #iterator()} returns an empty iterator. (principle 2) <br>
 * * {@link #toArray()} and {@link #toArray(Object[])} return empty arrays. (principle 2)
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 * 
 * @deprecated See deprecation of {@link space.arim.api.util.collect}
 */
@Deprecated(forRemoval = true)
public class FunctionalMapEntrySet<K, V> extends MapRelatedCollection<Entry<K, V>, K, V> implements SetEmptyHelper<Entry<K, V>>, UnmodifiableByDefaultSet<Entry<K, V>> {
	
	FunctionalMapEntrySet(FunctionalMap<K, V> map) {
		super(map);
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof Entry<?, ?>) {
			Entry<?, ?> entry = (Entry<?, ?>) o;
			FunctionalMap<K, V> map = (FunctionalMap<K, V>) getMap();
			if (map.instanceCheck(entry.getKey())) {
				@SuppressWarnings("unchecked")
				K key = (K) entry.getKey();
				if (map.containsKey(key)) {
					Object value = map.get(key);
					return (value != null) ? value.equals(entry.getValue()) : entry.getValue() == null;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}
	
}
