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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Abstract implementation for yaml-based {@link Config}s using Snake YAML.
 * 
 * @author A248
 *
 */
abstract class AbstractYamlConfig implements Config {

	static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	
	static final Pattern NODE_SEPARATOR_PATTERN = Pattern.compile(".", Pattern.LITERAL);
	
	private final File configFile;
	private transient final ReadWriteLock fileLock = new ReentrantReadWriteLock();
	
	/**
	 * Creates from a configuration file. <br>
	 * <br>
	 * The default values are immediately copied from the JAR resource. If such operation fails,
	 * a {@link ConfigLoadDefaultsFromJarException} is thrown. This exception will specifically
	 * be a {@link ConfigParseDefaultsFromJarException} if the default config's syntax is invalid.
	 * 
	 * @param configFile the configuration file
	 */
	AbstractYamlConfig(File configFile) {
		this.configFile = Objects.requireNonNull(configFile, "configFile must not be null");
	}
	
	/*
	 * 
	 * Loading and reloading
	 * 
	 */
	
	/**
	 * Gets the default configuration as a JAR resource
	 * 
	 * @param resourceName the name of the resource, e.g. "config.yml"
	 * @return the resource as a stream
	 */
	abstract InputStream getDefaultResourceAsStream(String resourceName);
	
	Map<String, Object> loadDefaults() {
		try (InputStream stream = getDefaultResourceAsStream(configFile.getName())) {
			return new Yaml().load(stream);

		} catch (IOException ex) {
			throw new ConfigReadDefaultsFromJarException(ex);
		} catch (YAMLException ex) {
			throw new ConfigParseDefaultsFromJarException(ex);
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
	
	Map<String, Object> loadFromFile() {
		Lock readLock = fileLock.readLock();
		readLock.lock();

		if (!configFile.exists()) {
			readLock.unlock();
			return Collections.emptyMap();
		}
		try (FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8)) {
			return new Yaml().load(reader);

		} catch (IOException ex) {
			throw new ConfigReadValuesFromFileException(ex);
		} catch (YAMLException ex) {
			throw new ConfigParseValuesFromFileException(ex);

		} finally {
			readLock.unlock();
		}
	}
	
	/*
	 * 
	 * Object values and retrieval
	 * 
	 */
	
	/**
	 * Ensures the config values have been loaded at least once with
	 * {@link #reloadConfig()}
	 * 
	 */
	abstract void ensureLoaded();
	
	/**
	 * Gets a configured value directly. Does not check preconditions.
	 * 
	 * @param <T> the type to retrieve
	 * @param key the key
	 * @param clazz the type class
	 * @return the configured value or null if not found
	 */
	abstract <T> T getConfiguredFromMap(String key, Class<T> clazz);
	
	/**
	 * Gets a default value directly. Does not check preconditions
	 * 
	 * @param <T> the type to retrieve
	 * @param key the key
	 * @param clazz the type class
	 * @return the default value or null if not found
	 */
	abstract <T> T getDefaultFromMap(String key, Class<T> clazz);
	
	@Override
	public <T> T getObject(String key, Class<T> clazz) {
		ensureLoaded();

		T value = getConfiguredFromMap(key, clazz);
		if (value != null) {
			return value;
		}
		value = getDefaultFromMap(key, clazz);
		if (value != null) {
			return value;
		}
		// Nothing found
		throw new ConfigDefaultValueNotSetException(
				"Neither config value nor default value defined for " + key + " with type " + clazz);
	}
	
	/**
	 * Checks the instance of each element in the list. <br>
	 * For an empty list this will always return {@code true}.
	 * 
	 * @param list the list to check
	 * @param elementClazz the element class used to check instances
	 * @return true if all the elements are isntances of the specified class, false otherwise.
	 */
	private static boolean checkElementTypes(List<?> list, Class<?> elementClazz) {
		for (Object element : list) {
			if (!elementClazz.isInstance(element)) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> getList(String key, Class<U> elementClazz) {
		ensureLoaded();

		List<?> configuredList = getConfiguredFromMap(key, List.class);
		if (configuredList != null && checkElementTypes(configuredList, elementClazz)) {
			return (List<U>) configuredList;
		}
		List<?> defaultList = getDefaultFromMap(key, List.class);
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

		return getConfiguredFromMap(key, clazz);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public <T> T getDefaultObject(String key, Class<T> clazz) {
		T result = getDefaultFromMap(key, clazz);
		if (result != null) {
			return result;
		}
		throw new ConfigDefaultValueNotSetException("No default value defined for " + key + " with type " + clazz);
	}
	
	/*
	 * 
	 * Utilities
	 * 
	 */
	
	/**
	 * Ensures the keys of a map are entirely strings. If the map is empty,
	 * or the map's keys are all {@code String}s, it is casted and returned. <br>
	 * <br>
	 * Otherwise, behaviour is determined by {@code keyConversionOption}. <br>
	 * If true, the map's keys will be converted to strings according to {@link Object#toString()}.
	 * The keys will be inserted into a new map, and the values copied, which will be returned. <br>
	 * If false, no key conversion is attempted, null is returned.
	 * 
	 * @param sourceMap the source map whose keys to check
	 * @param keyConversion whether to convert keys and make a new map if the keys are not strings
	 * @return the same map, replacement map, or null depending on what happened
	 */
	@SuppressWarnings("unchecked")
	static Map<String, Object> checkKeysAreStrings(Map<?, ?> sourceMap, boolean keyConversion) {
		boolean keysAreStrings = true;
		// Iteration handles empty maps
		for (Object key : sourceMap.keySet()) {
			if (!(key instanceof String)) {
				keysAreStrings = false;
				break;
			}
		}
		if (keysAreStrings) {
			// Casting a map where all the keys are strings is safe
			return (Map<String, Object>) sourceMap;
		}
		if (!keyConversion) {
			// As requested
			return null;
		}
		// Make a replacement map and copy values
		Map<String, Object> replacement = new HashMap<>();
		for (Map.Entry<?, ?> sourceEntry : sourceMap.entrySet()) {
			replacement.put(sourceEntry.getKey().toString(), sourceEntry.getValue());
		}
		return replacement;
	}
	
	/*
	 * 
	 * Bean properties
	 * 
	 */
	
	@Override
	public String toString() {
		return "AbstractYamlConfig [configFile=" + configFile + "]";
	}
	
}
