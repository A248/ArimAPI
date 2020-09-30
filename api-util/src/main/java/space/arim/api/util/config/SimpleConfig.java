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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Simple, yaml-based implementation of {@link Config}. Interally uses 2 HashMaps, for config values and defaults. <br>
 * <br>
 * It is intended that programmers place the config resource, call it <code>config.yml</code>,
 * in the JAR file. This JAR resource becomes the source of default values for the program's configuration.
 * A corresponding file, by the same name of the resource, in some configuration directory, will become
 * the source of configured values. If the file does not exist, it will be created, and the JAR resource
 * will be copied exactly to the file, copying not only the default values but also any comments associated
 * with the JAR file. <br>
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
 * @deprecated See {@link space.arim.api.util.config} (this entire framework is deprecated for removal)
 */
@Deprecated(forRemoval = true)
public class SimpleConfig extends AbstractYamlConfig {

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
	public SimpleConfig(File folder, String filename) {
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
	public SimpleConfig(File folder, String filename, Class<?> resourceClass) {
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
	public SimpleConfig(File configFile) {
		super(configFile);

		Class<?> runtimeClass = getClass();
		if (runtimeClass == SimpleConfig.class) {

			// Caller detected, option 2
			resourceClass = STACK_WALKER.walk((stream) -> {
				StackWalker.StackFrame callerFrame = stream
						.dropWhile((frame) -> frame.getClassName().equals(SimpleConfig.class.getName()))
						.limit(1).findFirst().get();
				return callerFrame.getDeclaringClass();
			});
		} else {
			// Subclassed, option 1
			resourceClass = runtimeClass;
		}
		// Must be called after class determined
		defaultValues = new HashMap<>(loadDefaults());
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
	public SimpleConfig(File configFile, Class<?> resourceClass) {
		super(configFile);
		this.resourceClass = resourceClass;
		// Must be called after class determined
		defaultValues = new HashMap<>(loadDefaults());
	}
	
	/**
	 * Creates from a configuration file. Identical in function to {@link #SimpleConfig(File)}
	 * except that a {@link Path} may be specified instead of a {@link File}. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configPath the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public SimpleConfig(Path configPath) {
		this(Objects.requireNonNull(configPath, "configPath must not be null").toFile());
	}
	
	/**
	 * Creates from a configuration file. Identical in function to {@link #SimpleConfig(File, Class)}
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
	public SimpleConfig(Path configPath, Class<?> resourceClass) {
		this(Objects.requireNonNull(configPath, "configPath must not be null").toFile(), resourceClass);
	}
	
	/*
	 * 
	 * Loading and reloading
	 * 
	 */
	
	@Override
	InputStream getDefaultResourceAsStream(String resourceName) {
		return resourceClass.getResourceAsStream('/' + resourceName);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void reloadConfig() {
		configValues = new HashMap<>(loadFromFile());
	}
	
	/*
	 * 
	 * Objects and values retrieval
	 * 
	 */
	
	/**
	 * Gets a configuration value from a nested map, or null if it does not exist
	 * or is not of type <code>T</code>.
	 * 
	 * @param <T> the type of the object to retrieve
	 * @param map the map of (nested) keys and values to search
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the object if it exists and is of the specified instance, <code>null</code> otherwise
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getFromNestedMap(Map<String, Object> map, String key, Class<T> clazz) {
		if (key.indexOf('.') == -1) {
			Object value = map.get(key);
			return (clazz.isInstance(value)) ? (T) value : null;
		}
		String[] keyParts = NODE_SEPARATOR_PATTERN.split(key);
		Map<?, ?> currentMap = map;
		boolean isMapSafe = true;

		int lastIndex = keyParts.length - 1;
		for (int n = 0; n < keyParts.length; n++) {

			String subKey = keyParts[n];
			@SuppressWarnings("unlikely-arg-type")
			Object subValue = (isMapSafe) ? currentMap.get(subKey) : getFromUnsafeMap(currentMap, subKey);

			if (n == lastIndex) {
				if (clazz.isInstance(subValue)) {
					return (T) subValue;
				}

			} else {
				if (!(subValue instanceof Map<?, ?>)) {
					return null;
				}
				currentMap = (Map<?, ?>) subValue;
				isMapSafe = checkKeysAreStrings(currentMap, false) != null;
			}
		}
		return null;
	}
	
	private static Object getFromUnsafeMap(Map<?, ?> map, String key) {
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (entry.getKey().toString().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	void ensureLoaded() {
		if (configValues == null) {
			throw new IllegalStateException("Configuration not yet loaded");
		}
	}
	
	@Override
	<T> T getConfiguredFromMap(String key, Class<T> clazz) {
		return getFromNestedMap(configValues, key, clazz);
	}

	@Override
	<T> T getDefaultFromMap(String key, Class<T> clazz) {
		return getFromNestedMap(defaultValues, key, clazz);
	}
	
	/*@Override
	public <T> T getObject(String key, Class<T> clazz) {
		return super.getObject(key, clazz);
	}*/
	
	/*@Override
	public <U> List<U> getList(String key, Class<U> elementClazz) {
		return super.getList(key, elementClazz);
	}*/
	
	/*@Override
	public <T> T getConfiguredObject(String key, Class<T> clazz) {
		return super.getConfiguredObject(key, clazz);
	}*/
	
	/*@Override
	public <T> T getDefaultObject(String key, Class<T> clazz) {
		return super.getDefaultObject(key, clazz);
	}*/
	
	/*
	 * 
	 * Key sets
	 * 
	 */
	
	@Override
	public Set<String> getConfiguredKeys() {
		ensureLoaded();

		return configValues.keySet();
	}
	
	@Override
	public Set<String> getConfiguredKeys(String key) {
		ensureLoaded();

		@SuppressWarnings("unchecked")
		Map<String, Object> subMap = getConfiguredFromMap(key, Map.class);
		return (subMap == null) ? null : subMap.keySet();
	}
	
	@Override
	public Set<String> getDefaultKeys() {
		return defaultValues.keySet();
	}
	
	@Override
	public Set<String> getDefaultKeys(String key) {
		@SuppressWarnings("unchecked")
		Map<String, Object> subMap = getDefaultFromMap(key, Map.class);
		if (subMap == null) {
			throw new ConfigDefaultValueNotSetException("No default subkeys found for " + key);
		}
		return subMap.keySet();
	}

	/*
	 * 
	 * Bean properties
	 * 
	 */
	
	@Override
	public String toString() {
		return "SimpleConfig [resourceClass=" + resourceClass + ", defaultValues=" + defaultValues + ", configValues="
				+ configValues + ", toString()=" + super.toString() + "]";
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
