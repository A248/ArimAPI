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
import java.util.Set;
import java.util.function.Function;

/**
 * A map which takes from a fixed key set and maps them dynamically (i.e., <i>calculates</i>) to a value when {@link #get(Object)} is called. <br>
 * <br>
 * <b>Principles</b>: <br>
 * 1 There is a set of keys considered valid key objects. <br>
 * 2 The mapped values are computed at call time. <br>
 * 3 An entry is contained within the map if the entry key maps to the entry value. <br>
 * 4 The map is unmodifiable. <br>
 * <br>
 * The specifications follow from the principles. <br>
 * <b>Specifications</b>: <br>
 * * {@link #keySet()} returns the key set provided at construction. (principle 1) <br>
 * * {@link #size()} and {@link #isEmpty()} redirect to the key set's equivalents. (principle 1) <br>
 * * {@link #containsKey(Object)} returns <code>true</code> if and only if the key object
 * is contained in the key set according to {@link Set#contains(Object)}. (principle 1) <br>
 * * {@link #values()} returns a collection which dynamically reflects the state of this map
 * (See {@link CalculationMapValues} for more information). {@link #containsValue(Object)}
 * returns <code>true</code> if and only if the object is contained within this collection. (principle 2) <br>
 * * {@link #entrySet()} returns a set which dynamically reflects the state of this map
 * (See {@link CalculationMapEntrySet} for more information). (principles 2 and 3) <br>
 * * The map's modification methods all throw <code>UnsupportedOperationException</code>. (principle 4)
 * 
 * @author A248
 *
 * @param <K> the key type
 * @param <V> the value type
 * 
 * @deprecated See deprecation of {@link space.arim.api.util.collect}
 */
@Deprecated(forRemoval = true)
public class CalculationMap<K, V> extends FunctionalMap<K, V> {
	
	private final Set<K> fixedKeys;
	
	/**
	 * Creates a calculation map from a mapping function and a set of keys
	 * 
	 * @param mappingFunction the mapping function
	 * @param fixedKeys the fixed key set
	 * 
	 * @deprecated Relies on generics manipulation. Possibility of throwing ClassCastException at runtime.
	 * Use {@link #CalculationMap(Function, Class, Set)} and specify the key class explicitly.
	 */
	@SuppressWarnings({ "removal" })
	@Deprecated
	public CalculationMap(Function<K, V> mappingFunction, Set<K> fixedKeys) {
		super(mappingFunction);
		this.fixedKeys = fixedKeys;
	}
	
	public CalculationMap(Function<K, V> mappingFunction, Class<K> keyClass, Set<K> fixedKeys) {
		super(mappingFunction, keyClass);
		this.fixedKeys = fixedKeys;
	}
	
	@Override
	public Set<K> keySet() {
		return fixedKeys;
	}
	
	@Override
	Collection<V> instantiateValues() {
		return new CalculationMapValues<K, V>(this);
	}
	
	@Override
	Set<Entry<K, V>> instantiateEntrySet() {
		return new CalculationMapEntrySet<K, V>(this);
	}
	
	@Override
	public int size() {
		return keySet().size();
	}
	
	@Override
	public boolean isEmpty() {
		return keySet().isEmpty();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean containsKey(Object key) {
		return keySet().contains(key);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean containsValue(Object value) {
		return values().contains(value);
	}
	
}
