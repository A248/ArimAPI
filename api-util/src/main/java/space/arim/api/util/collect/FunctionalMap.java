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
import java.util.function.Predicate;

import space.arim.api.util.collect.helper.UnmodifiableByDefaultMap;

/**
 * Represents a map which takes arbitrary keys and maps them dynamically to a value when {@link #get(Object)} is called. <br>
 * <br>
 * <b>Principles</b>: <br>
 * 1 Any object of the correct key type is a valid key. The key set is therefore unbounded. <br>
 * 2 The mapped values cannot be computed in the aggregate because the key set is infinite; the values collection is thus undefined. <br>
 * 3 An entry is contained within the map if the entry key maps to the entry value. <br>
 * 4 Infinity cannot be reduced in number or size. Subtraction has no effect. <br>
 * <br>
 * The specifications follow from the principles. <br>
 * <b>Specifications</b>: <br>
 * * The map is presumed to have an infinite key set. {@link #keySet()} returns an {@link InfinitySet}. (principle 1) <br>
 * * {@link #size()} returns <code>Integer.MAX_VALUE</code> and {@link #isEmpty()} returns <code>false</code>. (principle 1) <br>
 * * {@link #instanceCheck(Object)} is used to check the type of key objects, as a workaround
 * * {@link #containsKey(Object)} returns <code>true</code> if and only if the key object is valid. (principle 1) <br>
 * * {@link #get(Object)} will return a mapped result, as specified by a mapping function provided at construction, for any valid key. (principle 1) <br>
 * * {@link #keySet()} returns an {@link InfinitySet}. (principle 1) <br>
 * * {@link #containsValue(Object)} always returns <code>false</code>. (principle 2) <br>
 * * {@link #values()} returns an empty immutable collection. (principle 2) <br>
 * * {@link #entrySet()} returns a special set, see {@link FunctionalMapEntrySet} for details. (principle 3) <br>
 * * The map's modification methods all throw <code>UnsupportedOperationException</code>. (principle 4)
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class FunctionalMap<K, V> implements UnmodifiableByDefaultMap<K, V> {
	
	private final Function<K, V> mappingFunction;
	private final Predicate<Object> keyInstanceCheck;
	
	private volatile Set<K> keySet;
	private volatile Collection<V> values;
	private volatile Set<Entry<K, V>> entrySet;
	
	/**
	 * Creates a map based on the given mapping function. <br>
	 * The function used is when {@link #get(Object)} is called. Note that <code>#get()</code> may be called internally.
	 * 
	 * @param mappingFunction the mapping function
	 */
	@SuppressWarnings("unchecked")
	public FunctionalMap(Function<K, V> mappingFunction) {
		this(mappingFunction, FunctionalMap.<K>getGenericKeyType()::isInstance);
	}
	
	/**
	 * Creates a map based on the given mapping function and instanceof check. <br>
	 * The function used is when {@link #get(Object)} is called. Note that <code>#get()</code> may be called internally. <br>
	 * The instance checking predicate should return truthfully, or programmers will endure <code>ClassCastException</code>.
	 * 
	 * @param mappingFunction the mapping function
	 * @param keyInstanceCheck used to evaluate whether a key object is of the correct type
	 */
	public FunctionalMap(Function<K, V> mappingFunction, Predicate<Object> keyInstanceCheck) {
		this.mappingFunction = mappingFunction;
		this.keyInstanceCheck = keyInstanceCheck;
	}
	
	Set<K> instantiateKeySet() {
		return InfinitySet.create();
	}
	
	Collection<V> instantiateValues() {
		return Collections.emptySet();
	}
	
	Set<Entry<K, V>> instantiateEntrySet() {
		return new FunctionalMapEntrySet<K, V>(this);
	}
	
	@SuppressWarnings("unchecked")
	private static <K> Class<K> getGenericKeyType(K...ignore) {
		return (Class<K>) ignore.getClass().getComponentType();
	}
	
	protected boolean instanceCheck(Object key) {
		return keyInstanceCheck.test(key);
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
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@Override
	public V get(Object key) {
		return containsKey(key) ? mappingFunction.apply((K) key) : null;
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
