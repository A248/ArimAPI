/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple default implementations of getX(String) methods, such as {@link #getString(String)} and {@link #getInt(String)}. <br>
 * <br>
 * This interface is designed for implementation with ({@link Config}) and its subclasses. <br>
 * As such, the only required methods are {@link #reload()} and {@link #getObject(String, Class)},
 * which are already implemented in <code>Config</code>.
 * 
 * @author A248
 *
 * @deprecated The config framework represented by {@link Config} is deprecated. See {@link space.arim.api.config}
 * for more information.
 */
@Deprecated
public interface SimpleConfigFramework {

	/**
	 * Reloads the configuration
	 * 
	 */
	void reload();
	
	/**
	 * Gets the config object at the specific key
	 * if it's an instance of the class provided,
	 * else <code>null</code>
	 * 
	 * @param <T> the type of the object
	 * @param key the key
	 * @param type the class of the type
	 * @return the object of the desired type or <code>null</code>
	 */
	<T> T getObject(String key, Class<T> type);
	
	/**
	 * Gets the config objects at the specific key
	 * if they comprise a list of the type provided,
	 * else <code>null</code>
	 * 
	 * @param <T> the type of the objects
	 * @param key the key
	 * @param type the class of the type
	 * @return a list of objects of the desired type or <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	default <T> List<T> getObjects(String key, Class<T> type) {
		List<?> obj = getObject(key, List.class);
		return obj.isEmpty() || type.isInstance(obj.get(0)) ? (List<T>) obj : null;
	}
	
	/**
	 * Gets the object at the specific key if it's a string,
	 * else <code>null</code>
	 * 
	 * @param key the key
	 * @return the string or <code>null</code>
	 */
	default String getString(String key) {
		return getObject(key, String.class);
	}
	
	/**
	 * Gets the object at the specific key if it's an integer,
	 * else <code>null</code>
	 * 
	 * @param key the key
	 * @return the integer or <code>null</code>
	 */
	default Integer getInt(String key) {
		return getObject(key, Integer.class);
	}
	
	/**
	 * Gets the object at the specific key if it's a boolean,
	 * else <code>null</code>
	 * 
	 * @param key the key
	 * @return the boolean or <code>null</code>
	 */
	default Boolean getBoolean(String key) {
		return getObject(key, Boolean.class);
	}
	
	/**
	 * Gets a list of strings at the specific key if they exist,
	 * else <code>null</code>
	 * 
	 * @param key the key
	 * @return the list or <code>null</code>
	 */
	default List<String> getStrings(String key) {
		return getObjects(key, String.class);
	}
	
	/**
	 * Gets a list of integers at the specific key if they exist,
	 * else <code>null</code>
	 * 
	 * @param key the key
	 * @return the list or <code>null</code>
	 */
	default List<Integer> getInts(String key) {
		return getObjects(key, Integer.class);
	}
	
	/**
	 * Gets the configuration keys within a specify config section
	 * if they exist, else <code>null</code>. <br>
	 * These keys may be used normally just like you would any other key string.
	 * 
	 * @param key the key
	 * @return the keys within the config section, or <code>null</code>
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	default Set<String> getKeys(String key) {
		Map<?, ?> map = getObject(key, Map.class);
		return (map != null) ? SimpleConfig.prependFullKeyPath(key, ((Map<String, Object>) map).keySet()) : null;
	}

}
