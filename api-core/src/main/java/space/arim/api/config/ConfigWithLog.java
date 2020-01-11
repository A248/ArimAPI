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
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import space.arim.api.util.FilesUtil;

/**
 * A {@link Config} with logging of certain events enabled.
 * 
 * @author A248
 *
 */
public abstract class ConfigWithLog extends Config {
	
	protected ConfigWithLog(File folder, String filename, int version, String versionKey) {
		super(folder, filename, version, versionKey);
	}
	
	protected abstract void logEvent(ConfigEvent evt);
	
	@Override
	void reloadConfig() {
		logEvent(ConfigEvent.CONFIG_RELOAD);
		super.reloadConfig();
	}
	
	@Override
	File startVersionCheck(File source) {
		logEvent(ConfigEvent.VERSION_UPDATE_START);
		Integer ver = super.getObject(versionKey, Integer.class);
		if (ver == null || ver.intValue() != version) {
			logEvent(super.initVersionUpdate(source) ? ConfigEvent.VERSION_UPDATE_SUCCESS : ConfigEvent.VERSION_UPDATE_FAILED);
		}
		return source;
	}
	
	@Override
	File saveIfNotExist() {
		logEvent(ConfigEvent.FILE_SAVE_START);
		File target = new File(folder, filename);
		if (!target.exists()) {
			try (InputStream input = Config.class.getResourceAsStream(File.separator + filename)) {
				if (FilesUtil.saveFromStream(target, input)) {
					logEvent(ConfigEvent.FILE_SAVE_SUCCESS);
				} else {
					logEvent(ConfigEvent.FILE_SAVE_FAILED);
				}
			} catch (IOException ignored) {
				logEvent(ConfigEvent.FILE_SAVE_FAILED);
			}
		}
		return target;
	}
	
	@Override
	Map<String, Object> loadDefaults(Yaml yaml) {
		logEvent(ConfigEvent.DEFAULTS_LOAD_START);
		Map<String, Object> defaults = super.loadDefaults(yaml);
		logEvent(defaults.isEmpty() ? ConfigEvent.DEFAULTS_LOAD_FAILED : ConfigEvent.DEFAULTS_LOAD_START);
		return defaults;
	}
	
	@Override
	Map<String, Object> loadFile(File source, Yaml yaml) {
		logEvent(ConfigEvent.LOAD_FILE_START);
		Map<String, Object> values = super.loadFile(source, yaml);
		logEvent(values.isEmpty() ? ConfigEvent.LOAD_FILE_FAILED : ConfigEvent.LOAD_FILE_SUCCESS);
		return values;
	}
	
	public enum ConfigEvent {
		
		DEFAULTS_LOAD_START,
		DEFAULTS_LOAD_SUCCESS,
		DEFAULTS_LOAD_FAILED,
		FILE_SAVE_START,
		FILE_SAVE_SUCCESS,
		FILE_SAVE_FAILED,
		LOAD_FILE_START,
		LOAD_FILE_SUCCESS,
		LOAD_FILE_FAILED,
		VERSION_UPDATE_START,
		VERSION_UPDATE_SUCCESS,
		VERSION_UPDATE_FAILED,
		CONFIG_RELOAD
	}

}
