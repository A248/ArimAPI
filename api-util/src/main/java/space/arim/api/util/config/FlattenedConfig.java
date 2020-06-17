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

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Yaml-based implementation of {@link Config} as an alternate to {@link SimpleConfig}. <br>
 * <br>
 * This implementation functions similarly to {@code SimpleConfig} with regards to how
 * it loads config values and defaults. The config resource must be placed in the jar file
 * and have the same name as the file. <br>
 * <br>
 * However, it retains loaded values in memory differently.
 * Instead of maintaining a hierarchal structure of values, composed of maps and submaps, whose nested objects
 * are retrieved when required, {@code FlattenedConfig} "flattens" all of the maps and submaps into a single map
 * when the values are loaded. Thus, values are transferred to a single, flat map where keys are separated
 * by periods ('{@literal .}') in accordance with the yaml convention. <br>
 * <br>
 * Such design allows for faster retrieval of objects in heavily nested pathnames (e.g. "basekey.subkey.deeper.deepest")
 * since only 1 map lookup is required. However, this comes at the expense of increasing load times, since the entire
 * config map must be loaded and flattened before it can be used. Furthermore, when a key set is requested,
 * it must be rebuilt from the flattened keys. Moreover, since no map objects are retained, but rather converted
 * to the single flat map, maps cannot be specifically retrieved from {@code FlattenedConfig} without somehow
 * rebuilding the maps from the flattened map. <br>
 * <br>
 * For this reason, getting {@code Map} objects (or subinterfaces/subclasses thereof) is wholly unsupported.
 * Attempts to retrieve map objects will throw {@code UnsupportedOperationException}. <br>
 * <br>
 * <b>Usage</b> <br>
 * Either of the public constructors may be used to specify both the configuration file and the resource
 * name. There is no support for resource names not equal to the filename; that is, {@link File#getName()}
 * is assumed as the resource name. For example, <code>new SimpleConfig(new File("plugins/Plugin/", "config.yml"))</code>
 * would create a configuration using the file "/plugins/Plugin/config.yml", with the assumption there is
 * a JAR resource called "config.yml". <br>
 * <br>
 * {@link Class#getResourceAsStream(String)} is relied upon to retrieve the JAR resource. Therefore,
 * a {@code Class} must be somehow passed to the constructor. There are 3 ways to do this: <br>
 * 1. Subclassing. The subclass will be automatically detected and the subclass will be used. <br>
 * <code>Config config = new SimpleConfig(File) {}</code> <br>
 * 2. Call one of the constructors <i>without</i> a {@code Class} parameter, and have the caller class (your class)
 * be detected and used. <br>
 * <code>Config config = new SimpleConfig(File)</code><br>
 * 3. Explicitly pass a {@code Class} object to a constructor which accepts one. <br>
 * <code>Config config = new SimpleConfig(File, Class)</code>
 * 
 * @author A248
 *
 * @see SimpleConfig
 */
public class FlattenedConfig extends AbstractYamlConfig {

	private final Class<?> resourceClass;
	
	private final Map<String, Object> defaultValues;
	private volatile Map<String, Object> configValues;
	
	/*
	 * 
	 * Public constructors
	 * 
	 */
	
	/**
	 * Creates from a configuration folder and filename within that folder. The resource class
	 * is detected. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param folder the folder of the configuration file
	 * @param filename the filename of the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(File folder, String filename) {
		this(new File(folder, filename));
	}
	
	/**
	 * Creates from a configuration folder and filename within that folder, with the resource
	 * class explicitly passed. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param folder the folder of the configuration file
	 * @param filename the filename of the configuration file
	 * @param resourceClass the class from which to retrieve the JAR resource
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(File folder, String filename, Class<?> resourceClass) {
		this(new File(folder, filename), resourceClass);
	}
	
	/**
	 * Creates from a configuration file. The resource class is detected. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configFile the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(File configFile) {
		super(configFile);

		Class<?> runtimeClass = getClass();
		if (runtimeClass == FlattenedConfig.class) {

			// Caller detected, option 2
			resourceClass = STACK_WALKER.walk((stream) -> {
				StackWalker.StackFrame callerFrame = stream
						.dropWhile((frame) -> frame.getClassName().equals(FlattenedConfig.class.getName()))
						.limit(1).findFirst().get();
				return callerFrame.getDeclaringClass();
			});
		} else {
			// Subclassed, option 1
			resourceClass = runtimeClass;
		}
		// Must be called after class determined
		defaultValues = flatten(loadDefaults());
	}
	
	/**
	 * Creates from a configuration file, with the resource class explicitly passed. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configFile the configuration file
	 * @param resourceClass the class from which to retrieve the JAR resource
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(File configFile, Class<?> resourceClass) {
		super(configFile);
		this.resourceClass = resourceClass;
		// Must be called after class determined
		defaultValues = flatten(loadDefaults());
	}
	
	/**
	 * Creates from a configuration file. Identical in function to {@link #FlattenedConfig(File)}
	 * except that a {@link Path} may be specified instead of a {@link File}. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configPath the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(Path configPath) {
		this(Objects.requireNonNull(configPath, "configPath must not be null").toFile());
	}
	
	/**
	 * Creates from a configuration file. Identical in function to {@link #FlattenedConfig(File, Class)}
	 * except that a {@link Path} may be specified instead of a {@link File}. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configPath the configuration file
	 * @param resourceClass the class from which to retrieve the JAR resource
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public FlattenedConfig(Path configPath, Class<?> resourceClass) {
		this(Objects.requireNonNull(configPath, "configPath must not be null").toFile(), resourceClass);
	}
	
	/*
	 * 
	 * Flattening utilities
	 * 
	 */
	
	private static Map<String, Object> flatten(Map<String, Object> sourceMap) {
		Map<String, Object> result = new HashMap<>();
		fillWithFlattenedValues(result, "", sourceMap);
		return result;
	}
	
	private static void fillWithFlattenedValues(Map<String, Object> targetMap, String baseKey, Map<String, Object> sourceMap) {
		for (Map.Entry<String, Object> sourceEntry : sourceMap.entrySet()) {

			Object value = sourceEntry.getValue();
			if (value instanceof Map<?, ?>) {
				fillWithFlattenedValues(targetMap, baseKey + sourceEntry.getKey() + '.',
						checkKeysAreStrings((Map<?, ?>) value, true));

			} else {
				targetMap.put(baseKey + sourceEntry.getKey(), value);
			}
		}
	}

	/*
	 * 
	 * Loading and reloading
	 * 
	 */
	
	@Override
	InputStream getDefaultResourceAsStream(String resourceName) {
		return resourceClass.getResourceAsStream(File.separatorChar + resourceName);
	}
	
	@Override
	public void reloadConfig() {
		configValues = flatten(loadFromFile());
	}
	
	/*
	 * 
	 * Objects and values retrieval
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	private static <T> T getObjectAs(Map<String, Object> map, String key, Class<T> clazz) {
		Object value = map.get(key);
		if (clazz.isInstance(value)) {
			return (T) value;
		}
		return null;
	}
	
	private void checkNotMap(Class<?> clazz) {
		if (Map.class.isAssignableFrom(clazz)) {
			throw new UnsupportedOperationException("FlattenedConfig should not and cannot be used to retrieve Maps");
		}
	}
	
	@Override
	void ensureLoaded() {
		if (configValues == null) {
			throw new IllegalStateException("Configuration not yet loaded");
		}
	}
	
	@Override
	<T> T getConfiguredFromMap(String key, Class<T> clazz) {
		return getObjectAs(configValues, key, clazz);
	}

	@Override
	<T> T getDefaultFromMap(String key, Class<T> clazz) {
		return getObjectAs(defaultValues, key, clazz);
	}

	@Override
	public <T> T getObject(String key, Class<T> clazz) {
		checkNotMap(clazz);
		return super.getObject(key, clazz);
	}

	@Override
	public <T> T getConfiguredObject(String key, Class<T> clazz) {
		checkNotMap(clazz);
		return super.getConfiguredObject(key, clazz);
	}
	
	@Override
	public <T> T getDefaultObject(String key, Class<T> clazz) {
		checkNotMap(clazz);
		return super.getDefaultObject(key, clazz);
	}
	
	/*
	 * 
	 * Key sets
	 * 
	 */
	
	@Override
	public Set<String> getConfiguredKeys() {
		ensureLoaded();

		return buildRootKeySet(configValues);
	}

	@Override
	public Set<String> getConfiguredKeys(String key) {
		ensureLoaded();

		return buildKeySet(configValues, key);
	}

	@Override
	public Set<String> getDefaultKeys() {
		return buildRootKeySet(defaultValues);
	}

	@Override
	public Set<String> getDefaultKeys(String key) {
		Set<String> keySet = buildKeySet(defaultValues, key);
		if (keySet == null) {
			throw new ConfigDefaultValueNotSetException("No default subkeys found for " + key);
		}
		return keySet;
	}
	
	/*
	 * 
	 * Key set building
	 * 
	 */
	
	private static Set<String> buildRootKeySet(Map<String, Object> flatMap) {
		Set<String> result = new HashSet<>();
		for (String flatKey : flatMap.keySet()) {
			String[] keyParts = NODE_SEPARATOR_PATTERN.split(flatKey);
			result.add(keyParts[0]);
		}
		return result;
	}
	
	private static Set<String> buildKeySet(Map<String, Object> flatMap, String baseKey) {
		Set<String> result = null;
		int length = baseKey.length();

		for (String flatKey : flatMap.keySet()) {
			if (flatKey.startsWith(baseKey)) {

				String remainder = flatKey.substring(length);
				if (!remainder.isEmpty() && remainder.charAt(0) == '.') {

					if (result == null) {
						result = new HashSet<>();
					}
					String[] keyParts = NODE_SEPARATOR_PATTERN.split(remainder);
					result.add(keyParts[1]);
				}
			}
		}
		return result;
	}
	
	/*
	 * 
	 * Bean properties
	 * 
	 */
	
	@Override
	public String toString() {
		return "FlattenedConfig [defaultValues=" + defaultValues + ", configValues=" + configValues + ", toString()="
				+ super.toString() + "]";
	}
	
	/*@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object object) {
		return this == object;
	}*/

}
