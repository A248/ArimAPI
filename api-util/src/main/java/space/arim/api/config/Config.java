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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.yaml.snakeyaml.Yaml;

/**
 * A simple helper class for SnakeYAML configurations. <br>
 * <br>
 * <b>Usage:</b> Extend this class. <br>
 * <br>
 * <b>Reading is thread safe and may occur concurrently with reloading.</b>
 * Reloading is not itself thread safe and should only be performed by 1 thread at a time. <br>
 * However synchronisation will not be practical in most applications or plugins,
 * since it is not humanly possible to instruct a plugin to reload its config (e.g., with /plugin reload)
 * fast enough to trigger race conditions. <br>
 * <br>
 * Default values are set on initialisation.
 * 
 * @author A248
 *
 * @deprecated The config framework represented by this base class is deprecated. See {@link space.arim.api.config}
 * for more information.
 */
@Deprecated
public abstract class Config {
	
	protected final File folder;
	protected final String filename;
	protected final String versionKey;
	
	private final Map<String, Object> defaults;
	private final ConcurrentHashMap<String, Object> values = new ConcurrentHashMap<String, Object>();
	
	protected Config(File folder, String filename, String versionKey) {
		this.folder = folder;
		this.filename = filename;
		this.versionKey = versionKey;
		
		if (!folder.isDirectory() && !folder.mkdirs()) {
			throw new IllegalStateException("Config folder creation blocked!");
		}
		
		defaults = loadDefaults();
		values.putAll(defaults);
	}
	
	protected abstract File getBackupLocation();
	
	/**
	 * Allows programmers to log the precise exception when the config cannot be saved
	 * to the target file. <br>
	 * <br>
	 * The default implementation returns <code>null</code> to indicate no handler.
	 * 
	 * @return the exception handler for file save errors
	 */
	protected Consumer<IOException> getFileSaveErrorHandler() {
		return null;
	}
	
	public void reload() {
		reloadConfig();
	}
	
	private Yaml yaml() {
		return new Yaml();
	}
	
	void reloadConfig() {
		File file = new File(folder, filename);
		if (!file.exists()) {
			saveTo(file);
		}
		values.putAll(loadFile(file));
		if (!getFromMapRecursive(defaults, versionKey, Object.class).equals(getFromMapRecursive(values, versionKey, Object.class))) {
			initVersionUpdate(file);
		}
	}
	
	boolean initVersionUpdate(File source) {
		if (source.renameTo(getBackupLocation())) {
			saveTo(new File(folder, filename));
			return true;
		}
		return false;
	}
	
	boolean saveTo(File target) {
		try (InputStream input = getClass().getResourceAsStream(File.separator + filename); OutputStream output = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
            return true;
		} catch (IOException ex) {
			Consumer<IOException> errorHandler = getFileSaveErrorHandler();
			if (errorHandler != null) {
				errorHandler.accept(ex);
			}
		}
		return false;
	}
	
	Map<String, Object> loadDefaults() {
		try (InputStream stream = getClass().getResourceAsStream(File.separator + filename)) {
			return yaml().load(stream);
		} catch (IOException ignored) {}
		return Collections.emptyMap();
	}
	
	Map<String, Object> loadFile(File source) {
		try (FileReader reader = new FileReader(source)) {
			return yaml().load(reader);
		} catch (IOException ex) {}
		return Collections.emptyMap();
	}
	
	/**
	 * Recursively retrieves a specified type of object from a Map of potentially nested maps. <br>
	 * Periods delineate a nested map.
	 * <br>
	 * This method is particularly useful for configuration loaded thorugh SnakeYAML. <br>
	 * Specifically, if one must retrieve the yaml value key1.subkey.value as an Integer from the map <code>configValues</code>,
	 * one should call use <code>getFromMapRecursive(configValues, "key1.subkey.value", Integer.class)</code> <br>
	 * 
	 * @param <T> the type to retrieve. If the object found is not this type, <code>null</code> is returned
	 * @param map the map from which to retrieve recursively
	 * @param key the key string
	 * @param type the type class
	 * @return the object if found, null if not
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getFromMapRecursive(Map<String, Object> map, String key, Class<T> type) {
		if (key == null) {
			return null;
		} else if (!key.contains(".")) {
			Object obj = map.get(key);
			return type.isInstance(obj) ? (T) obj : null;
		} else if (key.startsWith(".") || key.endsWith(".")) {
			throw new IllegalArgumentException("Cannot retrieve value for invalid key " + key);
		}
		return getFromMapRecursive((Map<String, Object>) map.get(key.substring(0, key.indexOf("."))), key.substring(key.indexOf(".") + 1), type);
	}
	
	public <T> T getObject(String key, Class<T> type) {
		T obj = getFromMapRecursive(values, key, type);
		return obj != null ? obj : getFromMapRecursive(defaults, key, type);
	}
	
}
