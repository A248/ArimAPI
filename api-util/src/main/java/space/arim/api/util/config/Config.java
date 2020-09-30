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
package space.arim.api.util.config;

import java.util.List;
import java.util.Set;

/**
 * A thread safe configuration framework replacing the need to specify defaults directly in source code. <br>
 * <br>
 * Typical usage, given an implementation, begins with saving the default config to the filesystem if it does not exist
 * ({@link #saveDefaultConfig()}), and then loading config values for the first time with {@link #reloadConfig()}.
 * Thereafter, config values may be retrieved with {@link #getObject(String, Class)}, which will either return
 * the configured value or fall back to the default value, which is intended for use with hard-coded keys.
 * To get only a configured value, use {@link #getConfiguredObject(String, Class)}. To get only a default value,
 * use {@link #getDefaultObject(String, Class)}. <br>
 * <br>
 * <b>Thread Safety</b> <br>
 * All methods are thread safe. However, config values and defaults, such as mutable collections, should never be modified.
 * The behaviour of implementations is undefined if keys or values are modified; thread safety may fall to pieces.
 * Modification may throw exceptions. This rule also applies to config keys, e.g. from {@link #getConfiguredKeys()} and
 * {@link #getConfiguredKeys(String)}. <br>
 * <br>
 * <b>Configuration Format</b> <br>
 * There is no requirement for a specific format. Although YAML is common, this interface may be implemented
 * to support any variety of configuration format. <br>
 * <br>
 * <b>Null Values</b> <br>
 * This interface sometimes uses {@code null} to indicate a configured object of some kind was not found. Consequently,
 * null values should not be used as meaningful values in configurations. Nulls are treated as if the object did not
 * exist at all. Using {@code null} to indicate anything else is a mistake not supported by this interface.
 * 
 * @author A248
 * 
 * @see SimpleConfig
 * @see FlattenedConfig
 *
 * @deprecated See {@link space.arim.api.util.config} (this entire framework is deprecated for removal)
 */
@Deprecated(forRemoval = true)
public interface Config {

	/**
	 * Saves the default configuration to the file, copying it from the JAR resource, if the file does not exist. <br>
	 * If the config file already exists, this is a no-op.
	 * 
	 * @throws ConfigSaveDefaultsToFileException if the default configuration could not be saved to file
	 */
	void saveDefaultConfig();
	
	/**
	 * Loads or reloads the configuration values from the file. <br>
	 * Must be called at least once before any config objects are retrieved. <br>
	 * <br>
	 * If for some reason the config values could not be loaded, {@link ConfigLoadValuesFromFileException} is
	 * thrown. Implementations are encouraged to throw more specific subclasses where such information is known;
	 * for example, {@link ConfigParseValuesFromFileException} if malformed config syntax is detected.
	 * 
	 * @throws ConfigLoadValuesFromFileException if the config values could not be loaded from the file
	 */
	void reloadConfig();
	
	/**
	 * Gets a specific configuration object, falling back to the default value,
	 * if it is of type <code>T</code> at the specified key path. <br>
	 * The configuration values must have been loaded, at least once, with {@link #reloadConfig()},
	 * prior to this call. <br>
	 * <br>
	 * If the object at the specified key path in the configuration file does not exist or is not
	 * of type <code>T</code>, the default value is returned. <br>
	 * <br>
	 * The key follows the yaml convention where the path of a nested key is
	 * <i>section.subsection.key</i>. Key paths, thus, cannot start or end
	 * with <code>'.'</code>, since such character is used to denote nested keys.
	 * 
	 * @param <T> the type of the object to retrieve
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the object if it exists and is of the specified type, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the key of the correct type
	 */
	<T> T getObject(String key, Class<T> clazz);
	
	/**
	 * Shortcut for <code>getObject(key, String.class)</code>
	 * 
	 * @param key the key path at which to retrieve the string
	 * @return the string if it exists, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the key of the correct type
	 */
	default String getString(String key) {
		return getObject(key, String.class);
	}
	
	/**
	 * Shortcut for <code>getObject(key, Integer.class)</code>
	 * 
	 * @param key the key path at which to retrieve the int
	 * @return the int if it exists, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the key of the correct type
	 */
	default int getInteger(String key) {
		return getObject(key, Integer.class);
	}
	
	/**
	 * Shortcut for <code>getObject(key, Boolean.class)</code>
	 * 
	 * @param key the key path at which to retrieve the boolean
	 * @return the boolean if it exists, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the key of the correct type
	 */
	default boolean getBoolean(String key) {
		return getObject(key, Boolean.class);
	}
	
	/**
	 * Gets a list of values of a certain type at the specified key path if the elements in the list
	 * are of the specified type. <br>
	 * <br>
	 * A common sense implementation would be to find the list at the path and checks if it is empty.
	 * If empty, cast the list to {@code List<U>}. Otherwise, it checks the elements in the list to ensure
	 * they are all of the specified element type.
	 * 
	 * @param <U> the element type of objects in the list
	 * @param key the key path at which to retrieve the list
	 * @param elementClazz the class of the element type, used interanlly for instance checks
	 * @return the configured list if it exists and its elements are of the specified type, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the list with the correct type of elements
	 */
	<U> List<U> getList(String key, Class<U> elementClazz);
	
	/**
	 * Shortcut for <code>getList(key, String.class)</code>
	 * 
	 * @param key the key path at which to retrieve the string list
	 * @return the configured list if it exists and its elements are strings, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the string list
	 */
	default List<String> getStringList(String key) {
		return getList(key, String.class);
	}
	
	/**
	 * Shortcut for <code>getList(key, Integer.class)</code>
	 * 
	 * @param key the key path at which to retrieve the integers list
	 * @return the configured list if it exists and its elements are integers, the default value otherwise
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the string list
	 */
	default List<Integer> getIntegerList(String key) {
		return getList(key, Integer.class);
	}
	
	/**
	 * Gets a specific configuration object, if it is of type <code>T</code> and at
	 * the specified key path. <br>
	 * The configuration values must have been loaded, at least once, with {@link #reloadConfig()},
	 * prior to this call. <br>
	 * <br>
	 * If the config object at the path does not exist or is of the wrong type,
	 * <code>null</code> is returned. <br>
	 * <br>
	 * The key follows the yaml convention where the path of a nested key is
	 * <i>section.subsection.key</i>. Key paths, thus, cannot start or end
	 * with <code>'.'</code>, since such character is used to denote nested keys.
	 * 
	 * @param <T> the type of the object to retrieve
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 * @return the object if it exists and is of the specified type, <code>null</code> otherwise
	 */
	<T> T getConfiguredObject(String key, Class<T> clazz);
	
	/**
	 * Gets all configured keys at the base path. Does not include nested keys. <br>
	 * <br>
	 * Per the class javadoc, the set should be read but not written to.
	 * 
	 * @return the configured keys at the base path, never <code>null</code>
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 */
	Set<String> getConfiguredKeys();
	
	/**
	 * Gets all configured subkeys at the specified key path. Does not include nested keys. <br>
	 * If the path does not exist or there are no subkeys at the path, <code>null</code> is returned.
	 * <br>
	 * Per the class javadoc, the set should be read but not written to.
	 * 
	 * @param key the key path at which to find subkeys
	 * @return the configured subkeys at the specified key path, or <code>null</code> if not found
	 * @throws IllegalStateException if the config values have never been loaded with {@link #reloadConfig()}
	 */
	Set<String> getConfiguredKeys(String key);
	
	/**
	 * Gets a default value, if it is of type <code>T</code> and at
	 * the specified key path. <br>
	 * <br>
	 * If the default object at the path does not exist or is of the wrong type,
	 * {@link ConfigDefaultValueNotSetException} is thrown.
	 * 
	 * @param <T> the type of the object to retrieve
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the default value at the key path
	 * @throws ConfigDefaultValueNotSetException if there is no default value for the key of the correct type
	 */
	<T> T getDefaultObject(String key, Class<T> clazz);
	
	/**
	 * Gets all default keys at the base path. Does not include nested keys. <br>
	 * <br>
	 * Per the class javadoc, the set should be read but not written to.
	 * 
	 * @return the default keys at the base path, never <code>null</code>
	 */
	Set<String> getDefaultKeys();
	
	/**
	 * Gets all configured subkeys at the specified key path. Does not include nested keys. <br>
	 * If the path does not exist or there are no subkeys at the path, {@link ConfigDefaultValueNotSetException}
	 * is thrown.
	 * <br>
	 * Per the class javadoc, the set should be read but not written to.
	 * 
	 * @param key the key path at which to find subkeys
	 * @return the default subkeys at the specified key path
	 * @throws ConfigDefaultValueNotSetException if the subkeys were not found at the path
	 */
	Set<String> getDefaultKeys(String key);
	
}
