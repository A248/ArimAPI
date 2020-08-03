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
import java.util.Map;
import java.util.Set;

/**
 * Configuration data loaded from a specific file or resource, whose raw data is represented by a map. <br>
 * <br>
 * When an object requested, per {@link ConfigValues} methods, does not exist, the method will return {@code null}. <br>
 * <br>
 * All keys follow the convention where the path of a nested key is <i>section.subsection.key</i>. Key paths,
 * thus, cannot start or end with <code>'.'</code>, since such character is used to denote nested keys.
 * 
 * @author A248
 *
 */
public interface ConfigData extends ConfigValues {

	/**
	 * Gets a specific configuration object, of type {@code U} and at the specified key path,
	 * otherwise {@code null} if these conditions are not met. <br>
	 * <br>
	 * The key follows the convention where the path of a nested key is
	 * <i>section.subsection.key</i>. Key paths, thus, cannot start or end
	 * with <code>'.'</code>, since such character is used to denote nested keys.
	 * 
	 * @param <U> the type of the object to retrieve
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the object if it exists and is of the specified type, otherwise {@code null}
	 */
	@Override
	<U> U getObject(String key, Class<U> clazz);
	
	/**
	 * Shortcut for {@code getObject(key, String.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the string if it exists, else {@code null}
	 */
	@Override
	default String getString(String key) {
		return getObject(key, String.class);
	}
	
	/**
	 * Shortcut for {@code getObject(key, Integer.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the integer if it exists, else {@code null}
	 */
	@Override
	default Integer getInteger(String key) {
		return getObject(key, Integer.class);
	}
	
	/**
	 * Shortcut for {@code getObject(key, Boolean.class)}
	 * 
	 * @param key the key path at which to retrieve the object
	 * @return the boolean if it exists, else {@code null}
	 */
	@Override
	default Boolean getBoolean(String key) {
		return getObject(key, Boolean.class);
	}
	
	/**
	 * Gets a list of values of a certain type at the specified key path if the elements in the list
	 * are of the specified type, else {@code null}. <br>
	 * <br>
	 * A common sense implementation would be to find the list at the path and checks if it is empty.
	 * If empty, cast the list to {@code List<U>}. Otherwise, check the elements in the list to ensure
	 * they are all of the specified element type.
	 * 
	 * @param <U> the element type of objects in the list
	 * @param key the key path at which to retrieve the list
	 * @param elementClazz the class of the element type, used for instance checks
	 * @return the configured list if it exists and its elements are of the specified type, else {@code null}
	 */
	@Override
	<U> List<U> getList(String key, Class<U> elementClazz);
	
	/**
	 * Shortcut for {@code getList(key, String.class)}
	 * 
	 * @param key the key path at which to retrieve the string list
	 * @return the configured list if it exists and its elements are strings, else {@code null}
	 */
	@Override
	default List<String> getStringList(String key) {
		return getList(key, String.class);
	}
	
	/**
	 * Shortcut for {@code getList(key, Integer.class)}
	 * 
	 * @param key the key path at which to retrieve the string list
	 * @return the configured list if it exists and its elements are integers, else {@code null}
	 */
	@Override
	default List<Integer> getIntegerList(String key) {
		return getList(key, Integer.class);
	}
	
	/**
	 * Gets all keys at the base path. Does not include nested keys.
	 * 
	 * @return the configured keys at the base path, never {@code null}
	 */
	Set<String> getKeys();
	
	/**
	 * Gets all keys at the specified path. Does not include nested keys. <br>
	 * If the path does not exist or there are no subkeys at the path, an empty is returned.
	 * 
	 * @param key the key path at which to find subkeys
	 * @return the keys at the specified path, or an empty set, never {@code null}
	 */
	Set<String> getKeys(String key);
	
	/**
	 * Gets a raw map of this config data's keys and values. Child values are denoted by nested maps. <br>
	 * <b>This method is provided for implementers of config serialisers. Most users will not need it. </b> <br>
	 * <br>
	 * The map should be safe to iterate across concurrently. If need be, the caller may create a copy through such iteration.
	 * The caller should never modify the map.
	 * 
	 * @return a map of this configuration's data
	 */
	Map<String, Object> getValuesMap();
	
	/**
	 * Gets the comments of this config data, as a map of fully qualified keys to {@link ConfigComment}s. <br>
	 * <b>This method is provided for implementers of config serialisers. Most users will not need it. </b> <br>
	 * <br>
	 * Unlike {@link #getValuesMap()}, the structure of the returned map is not that of a hierarchical map. Rather, it is
	 * of a flattened map of full key paths, separated by {@literal '.'}, to lists of comments. <br>
	 * <br>
	 * The comments at a certain key come before that key is defined in a document. The empty string key corresponds to the
	 * final comments of the document, which come after all keys. <br>
	 * <br>
	 * The map should be safe to iterate across concurrently. If need be, the caller may create a copy through such iteration.
	 * The caller should never modify the map nor its lists.
	 * 
	 * @return a map of this config data's comments
	 */
	Map<String, List<ConfigComment>> getCommentsMap();
	
	/**
	 * Determines equality with the specified object, consistent with {@link #getValuesMap()} and {@link #getCommentsMap()}
	 * of this config data.
	 * 
	 * @param object the object to determine equality with
	 * @return true if equal, false otherwise
	 */
	@Override
	boolean equals(Object object);
	
}
