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
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import space.arim.api.util.collect.helper.UnmodifiableByDefaultMap;

public class FunctionalMap<K, V> implements UnmodifiableByDefaultMap<K, V> {
	
	private final Function<K, V> mappingFunction;
	
	volatile Set<K> keySet;
	private volatile Collection<V> values;
	private volatile Set<Entry<K, V>> entrySet;
	
	public FunctionalMap(Function<K, V> mappingFunction) {
		this.mappingFunction = mappingFunction;
	}
	
	protected Set<K> instantiateKeySet() {
		return new InfinitySet<K>();
	}
	
	protected Collection<V> instantiateValues() {
		return Collections.emptySet();
	}
	
	protected Set<Entry<K, V>> instantiateEntrySet() {
		return new InfinitySet<Entry<K, V>>();
	}
	
	@SuppressWarnings("unchecked")
	private Class<K> getKeyType(K...ignore) {
		return (Class<K>) ignore.getClass().getComponentType();
	}
	
	@SuppressWarnings("unchecked")
	protected boolean instanceCheck(Object key) {
		return getKeyType().isInstance(key);
	}
	
	@Override
	public int size() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean containsKey(Object key) {
		return instanceCheck(key);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		return instanceCheck(key) ? mappingFunction.apply((K) key) : null;
	}
	
	@Override
	public Set<K> keySet() {
		return (keySet != null) ? keySet : (keySet = instantiateKeySet());
	}
	
	@Override
	public Collection<V> values() {
		return (values != null) ? values : (values = instantiateValues());
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		return (entrySet != null) ? entrySet : (entrySet = instantiateEntrySet());
	}
	
}
