/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Builder for easily creating {@link ValueTransformer}. Assumes each {@code ValueTransformer}
 * is intended to apply to a specific key. <br>
 * <br>
 * This class has no special meaning. It is intended for convenience.
 * 
 * @author A248
 *
 */
public abstract class SingleKeyValueTransformer implements ValueTransformer {

	private final String key;
	
	/**
	 * Creates, from the singular key used in this value transformer
	 * 
	 * @param key the single key to use
	 * @throws NullPointerException if {@code key} is null
	 */
	protected SingleKeyValueTransformer(String key) {
		this.key = Objects.requireNonNull(key, "key");
	}
	
	/**
	 * Gets the single key of this single key value transformer.
	 * 
	 * @return the single key used
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Transforms the value according to {@link ValueTransformer#transform(String, Object)}
	 * 
	 * @param value the value
	 * @return the new object, {@code value} to make no changes, or {@code null} to remove the entry
	 */
	protected abstract Object transform(Object value);
	
	@Override
	public Object transform(String key, Object value) {
		if (!this.key.equalsIgnoreCase(key))
			return value;
		return transform(value);
	}
	
	/**
	 * Creates a value transformer from a {@code UnaryOperator} applying to object at the specified key
	 * 
	 * @param key the single key of the transformer
	 * @param operator the operator used to apply changes
	 * @return a value transformer
	 * @throws NullPointerException if either paramater is null
	 */
	public static SingleKeyValueTransformer create(String key, UnaryOperator<Object> operator) {
		Objects.requireNonNull(operator, "operator");
		return new SingleKeyValueTransformer(key) {
			@Override
			protected Object transform(Object value) {
				return operator.apply(value);
			}
		};
	}
	
	/**
	 * Creates a value transformer from a {@code Predicate} applying to object at the specified key and
	 * determining whether to remove the value. <br>
	 * <br>
	 * If the predicate returns {@code true} the value is removed.
	 * 
	 * @param key the single key of the transformer
	 * @param removeIf the predicate used to determine whether to remove the entry
	 * @return a value transformer
	 * @throws NullPointerException if either paramater is null
	 */
	public static SingleKeyValueTransformer createPredicate(String key, Predicate<Object> removeIf) {
		Objects.requireNonNull(removeIf, "removeIf");
		return new SingleKeyValueTransformer(key) {
			@Override
			protected Object transform(Object value) {
				if (removeIf.test(value))
					return null;
				return value;
			}
		};
	}
	
}
