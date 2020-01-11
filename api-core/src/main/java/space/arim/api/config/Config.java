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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yaml.snakeyaml.Yaml;

import space.arim.universal.util.UniversalUtil;

import space.arim.api.util.FilesUtil;

/**
 * A simple helper class for SnakeYAML configurations. <br>
 * <br>
 * <b>This class is not thread safe.</b>
 * 
 * @author A248
 *
 */
public abstract class Config {
	
	protected final File folder;
	protected final String filename;
	protected final int version;
	protected final String versionKey;
	
	private final Map<String, Object> defaults;
	private final ConcurrentHashMap<String, Object> values = new ConcurrentHashMap<String, Object>();
	
	protected Config(File folder, String filename, int version, String versionKey) {
		this.folder = folder;
		this.filename = filename;
		this.version = version;
		this.versionKey = versionKey;
		
		if (!folder.isDirectory() && !folder.mkdirs()) {
			throw new IllegalStateException("Config folder creation blocked!");
		}
		
		defaults = loadDefaults(yaml());
		values.putAll(defaults);
	}
	
	protected abstract File getBackupLocation();
	
	public final void reload() {
		reloadConfig();
	}
	
	private Yaml yaml() {
		return UniversalUtil.COMMON_SNAKE_YAML.get();
	}
	
	void reloadConfig() {
		File file = saveIfNotExist();
		values.putAll(loadFile(file, yaml()));
		startVersionCheck(file);
	}
	
	File startVersionCheck(File source) {
		Object ver = values.get(versionKey);
		if (!(ver instanceof Integer) || (Integer) ver != version) {
			initVersionUpdate(source);
		}
		return source;
	}
	
	boolean initVersionUpdate(File source) {
		if (source.renameTo(getBackupLocation())) {
			source = saveIfNotExist();
			return true;
		}
		return false;
	}
	
	File saveIfNotExist() {
		File target = new File(folder, filename);
		if (!target.exists()) {
			try (InputStream input = Config.class.getResourceAsStream(File.separator + filename)) {
				FilesUtil.saveFromStream(target, input);
			} catch (IOException ignored) {}
		}
		return target;
	}
	
	@SuppressWarnings("unchecked")
	Map<String, Object> loadDefaults(Yaml yaml) {
		try (InputStream stream = getClass().getResourceAsStream(File.separator + filename)) {
			return (Map<String, Object>) yaml.load(stream);
		} catch (IOException ignored) {}
		return Collections.emptyMap();
	}
	
	@SuppressWarnings("unchecked")
	Map<String, Object> loadFile(File source, Yaml yaml) {
		try (FileReader reader = new FileReader(source)) {
			return (Map<String, Object>) yaml.load(reader);
		} catch (IOException ex) {}
		return Collections.emptyMap();
	}
	
	private <T> T getFromMap(Map<String, Object> values, String key, Class<T> type) {
		return UniversalUtil.getFromMapRecursive(values, key, type);
	}
	
	public <T> T getObject(String key, Class<T> type) {
		T obj = getFromMap(values, key, type);
		return obj != null ? obj : getFromMap(defaults, key, type);
	}
	
}
