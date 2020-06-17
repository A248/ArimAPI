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

	@Override
	public String toString() {
		return "AbstractYamlConfig [configFile=" + configFile + "]";
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
	static Map<String, Object> ensureStringKeys(Map<?, ?> sourceMap, boolean keyConversion) {
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
	
	static HashMap<String, Object> asHashMap(Map<String, Object> map) {
		if (map instanceof HashMap<?, ?>) {
			return (HashMap<String, Object>) map;
		}
		return new HashMap<>(map);
	}
	
}
