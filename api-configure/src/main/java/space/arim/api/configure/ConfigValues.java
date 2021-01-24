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
package space.arim.api.configure;

import java.util.List;

/**
 * Retrievable configuration values from configuration data or some sort of config data aggregator. <br>
 * <br>
 * When an object requested does not exist, the method may throw an exception or return {@code null}, depending
 * on its specific contract. <br>
 * <br>
 * All keys follow the convention where the path of a nested key is <i>section.subsection.key</i>. Key paths,
 * thus, cannot start or end with <code>'.'</code>, since such character is used to denote nested keys.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface ConfigValues {

	/**
	 * Gets a specific configuration object, of type {@code U} and at the specified key path. <br>
	 * <br>
	 * The key follows the convention where the path of a nested key is
	 * <i>section.subsection.key</i>. Key paths, thus, cannot start or end
	 * with <code>'.'</code>, since such character is used to denote nested keys.
	 * 
	 * @param <U> the type of the object to retrieve
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the object if it exists and is of the specified type
	 * @throws ConfigException a subclass thereof (optional), as specified by an override of this method
	 */
	<U> U getObject(String key, Class<U> clazz);
	
	/**
	 * Shortcut for {@code getObject(key, String.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the string if it exists
	 * @throws ConfigException optional, per {@link #getObject(String, Class)}
	 */
	default String getString(String key) {
		return getObject(key, String.class);
	}
	
	/**
	 * Shortcut for {@code getObject(key, Integer.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the integer if it exists
	 * @throws ConfigException optional, per {@link #getObject(String, Class)}
	 */
	default Integer getInteger(String key) {
		return getObject(key, Integer.class);
	}
	
	/**
	 * Shortcut for {@code getObject(key, Boolean.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the boolean if it exists
	 * @throws ConfigException optional, per {@link #getObject(String, Class)}
	 */
	default Boolean getBoolean(String key) {
		return getObject(key, Boolean.class);
	}
	
	/**
	 * Gets a list of values of a certain type at the specified key path if the elements in the list
	 * are of the specified type. <br>
	 * <br>
	 * A common sense implementation would be to find the list at the path and checks if it is empty.
	 * If empty, cast the list to {@code List<U>}. Otherwise, check the elements in the list to ensure
	 * they are all of the specified element type.
	 * 
	 * @param <U> the element type of objects in the list
	 * @param key the key path at which to retrieve the list
	 * @param elementClazz the class of the element type, used for instance checks
	 * @return the configured list if it exists and its elements are of the specified type
	 * @throws ConfigException a subclass thereof (optional), as specified by an override of this method
	 */
	<U> List<U> getList(String key, Class<U> elementClazz);
	
	/**
	 * Shortcut for {@code getList(key, String.class)}
	 * 
	 * @param key the key path at which to retrieve the string list
	 * @return the configured list if it exists and its elements are strings
	 * @throws ConfigException optional, per {@link #getList(String, Class)}
	 */
	default List<String> getStringList(String key) {
		return getList(key, String.class);
	}
	
	/**
	 * Shortcut for {@code getList(key, Integer.class)}
	 * 
	 * @param key the key path at which to retrieve the string list
	 * @return the configured list if it exists and its elements are integers
	 * @throws ConfigException optional, per {@link #getList(String, Class)}
	 */
	default List<Integer> getIntegerList(String key) {
		return getList(key, Integer.class);
	}
	
}
