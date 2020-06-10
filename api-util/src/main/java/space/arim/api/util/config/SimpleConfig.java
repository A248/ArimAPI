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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.yaml.snakeyaml.Yaml;

/**
 * Main implementation of {@link Config} using 2 internal HashMaps, one for config values and one for defaults. <br>
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
 * is assumed as the resource name. This class is abstract because it internally relies on the runtime subclass
 * to retrieve the JAR resource. <br>
 * <br>
 * <b>Implementation Note</b> <br>
 * The implementation uses <code>null</code> to indicate to itself when a value could not be found. When there
 * are truly null configuration values present, the value will seem as if it was not found.
 * 
 * @author A248
 *
 */
public abstract class SimpleConfig implements Config {

	private final File configFile;
	private transient final ReadWriteLock fileLock = new ReentrantReadWriteLock();
	
	private final Map<String, Object> defaultValues;
	private volatile Map<String, Object> configValues;
	
	/*
	 * 
	 * Public constructors
	 * 
	 */
	
	/**
	 * Creates from a configuration folder and filename within that folder. <br>
	 * For example, <code>new Config(new File("plugins/Plugin/"), "config.yml")</code>
	 * would create a configuration using the file "/plugins/Plugin/config.yml".
	 * It is assumed that there is a JAR resource called "config.yml". <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource.
	 * If such operation fails, a {@link ConfigLoadDefaultsFromJarException}
	 * is thrown.
	 * 
	 * @param folder the folder of the configuration file
	 * @param filename the filename of the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public SimpleConfig(File folder, String filename) {
		this(new File(folder, filename));
	}
	
	/**
	 * Creates from a configuration file. <br>
	 * For example, new Config(new File("/plugins/Plugin/config.yml"))</code>
	 * would create a configuration using the file "/plugins/Plugin/config.yml".
	 * It is assumed that there is a JAR resource called "config.yml". <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource.
	 * If such operation fails, a {@link ConfigLoadDefaultsFromJarException}
	 * is thrown.
	 * 
	 * @param configFile the configuration file
	 * @throws ConfigLoadDefaultsFromJarException if the default values could not be loaded from the jar resource
	 */
	public SimpleConfig(File configFile) {
		this.configFile = Objects.requireNonNull(configFile, "configFile must not be null");
		defaultValues = new HashMap<>(loadDefaults());
	}
	
	/**
	 * Creates from a configuration file. Identical in function to {@link #SimpleConfig(File)}
	 * except that a {@link Path} may be specified instead of a {@link File}. <br>
	 * <br>
	 * At the moment, this is equal to the call <code>new SimpleConfig(configPath.toFile())</code>,
	 * but in the future, the implementation may change internally to use NIO.
	 * 
	 * @param configPath the configuration file
	 */
	public SimpleConfig(Path configPath) {
		this(Objects.requireNonNull(configPath, "configPath must not be null").toFile());
	}
	
	/*
	 * 
	 * IO operations
	 * 
	 */
	
	private InputStream getDefaultResourceAsStream(String resourceName) {
		return getClass().getResourceAsStream(File.separatorChar + resourceName);
	}
	
	private Map<String, Object> loadDefaults() {
		try (InputStream stream = getDefaultResourceAsStream(configFile.getName())) {
			return new Yaml().load(stream);
		} catch (IOException ex) {
			throw new ConfigLoadDefaultsFromJarException(ex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void saveDefaultConfig() {
		if (configFile.exists()) {
			return;
		}
		Lock writeLock = fileLock.writeLock();
		if (!writeLock.tryLock()) {
			return;
		}
		try (InputStream input = getDefaultResourceAsStream(configFile.getName());
				FileOutputStream output = new FileOutputStream(configFile)) {
			byte[] buffer = new byte[4096];
			int read;
			while ((read = input.read(buffer)) != -1) {
				output.write(buffer, 0, read);
			}
		} catch (IOException ex) {
			throw new ConfigSaveDefaultsToFileException("Could not save config from JAR to local filesystem", ex);
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void reloadConfig() {
		configValues = new HashMap<>(loadFromFile());
	}
	
	private Map<String, Object> loadFromFile() {
		Lock readLock = fileLock.readLock();
		readLock.lock();

		if (!configFile.exists()) {
			readLock.unlock();
			return Collections.emptyMap();
		}

		try (FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8)) {
			return new Yaml().load(reader);

		} catch (IOException ex) {
			throw new ConfigLoadValuesFromFileException(ex);
		} finally {
			readLock.unlock();
		}
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
		String[] keyParts = key.split(".");
		Map<String, Object> currentMap = map;

		int lastIndex = keyParts.length - 1;
		for (int n = 0; n < keyParts.length; n++) {
			String subKey = keyParts[n];
			Object subValue = currentMap.get(subKey);

			if (n == lastIndex) {
				if (clazz.isInstance(subValue)) {
					return (T) subValue;
				}

			} else {
				if (!(subValue instanceof Map<?, ?>)) {
					return null;
				}
				currentMap = (Map<String, Object>) subValue;
			}
		}
		return null;
	}
	
	private void ensureLoaded() {
		if (configValues == null) {
			throw new IllegalStateException("Configuration not yet loaded");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public <T> T getObject(String key, Class<T> clazz) {
		ensureLoaded();

		T result = getFromNestedMap(configValues, key, clazz);
		if (result != null) {
			return result;
		}
		// Fall back to default values
		result = getFromNestedMap(defaultValues, key, clazz);
		if (result != null) {
			return result;
		}
		// Nothing found
		throw new ConfigDefaultValueNotSetException(
				"Neither config value nor default value defined for " + key + " with type " + clazz);
	}
	
	private static boolean checkElementTypes(List<?> list, Class<?> elementClazz) {
		for (Object element : list) {
			if (!elementClazz.isInstance(element)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getList(String key, Class<U> elementClazz) {
		ensureLoaded();

		List<?> configuredList = getFromNestedMap(configValues, key, List.class);
		if (configuredList != null && checkElementTypes(configuredList, elementClazz)) {
			return (List<U>) configuredList;
		}
		List<?> defaultList = getFromNestedMap(defaultValues, key, List.class);
		if (defaultList != null && checkElementTypes(defaultList, elementClazz)) {
			return (List<U>) defaultList;
		}
		throw new ConfigDefaultValueNotSetException("Neither config value nor default value has list defined for " + key
				+ " with element type " + elementClazz);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public <T> T getConfiguredObject(String key, Class<T> clazz) {
		ensureLoaded();

		return getFromNestedMap(configValues, key, clazz);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<String> getConfiguredKeys() {
		ensureLoaded();

		return configValues.keySet();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<String> getConfiguredKeys(String key) {
		ensureLoaded();

		@SuppressWarnings("unchecked")
		Map<String, Object> subMap = getFromNestedMap(configValues, key, Map.class);
		return (subMap == null) ? null : subMap.keySet();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public <T> T getDefaultObject(String key, Class<T> clazz) {
		T result = getFromNestedMap(defaultValues, key, clazz);
		if (result != null) {
			return result;
		}
		throw new ConfigDefaultValueNotSetException("No default value defined for " + key + " with type " + clazz);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<String> getDefaultKeys() {
		return defaultValues.keySet();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<String> getDefaultKeys(String key) {
		@SuppressWarnings("unchecked")
		Map<String, Object> subMap = getFromNestedMap(configValues, key, Map.class);
		if (subMap == null) {
			throw new ConfigDefaultValueNotSetException("No default subkeys found for " + key);
		}
		return subMap.keySet();
	}

	@Override
	public String toString() {
		return "SimpleConfig [configFile=" + configFile + ", defaultValues=" + defaultValues + ", configValues="
				+ configValues + "]";
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object object) {
		return this == object;
	}
	
}
