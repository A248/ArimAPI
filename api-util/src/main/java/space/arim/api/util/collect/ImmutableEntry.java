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

import java.util.Map;

/**
 * An immutable implementation of {@link Map.Entry}. <br>
 * {@link #setValue(Object)} throws <code>UnsupportedOperationException</code>.
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ImmutableEntry<K, V> implements Map.Entry<K, V> {
	
	private final K key;
	private final V value;
	
	/**
	 * Creates an entry based on a key and value
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public ImmutableEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException();
	}
	
}
