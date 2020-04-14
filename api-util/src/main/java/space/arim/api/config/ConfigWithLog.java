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
import java.util.Map;

/**
 * A {@link Config} with logging of certain events enabled. <br>
 * <br>
 * <b>Usage:</b> Extend this class.
 * 
 * @author A248
 *
 */
public abstract class ConfigWithLog extends Config {
	
	protected ConfigWithLog(File folder, String filename, String versionKey) {
		super(folder, filename, versionKey);
	}
	
	protected abstract void logEvent(ConfigEvent evt);
	
	@Override
	void reloadConfig() {
		logEvent(ConfigEvent.CONFIG_RELOAD);
		super.reloadConfig();
	}
	
	@Override
	boolean initVersionUpdate(File source) {
		logEvent(ConfigEvent.VERSION_UPDATE_START);
		boolean success = super.initVersionUpdate(source);
		logEvent((success) ? ConfigEvent.VERSION_UPDATE_SUCCESS : ConfigEvent.VERSION_UPDATE_FAILED);
		return success;
	}
	
	@Override
	boolean saveTo(File target) {
		logEvent(ConfigEvent.FILE_SAVE_START);
		boolean success = super.saveTo(target);
		logEvent((success) ? ConfigEvent.FILE_SAVE_SUCCESS : ConfigEvent.FILE_SAVE_FAILED);
		return success;
	}
	
	@Override
	Map<String, Object> loadDefaults() {
		logEvent(ConfigEvent.DEFAULTS_LOAD_START);
		Map<String, Object> defaults = super.loadDefaults();
		logEvent(!defaults.isEmpty() ? ConfigEvent.DEFAULTS_LOAD_SUCCESS : ConfigEvent.DEFAULTS_LOAD_FAILED);
		return defaults;
	}
	
	@Override
	Map<String, Object> loadFile(File source) {
		logEvent(ConfigEvent.VALUES_LOAD_START);
		Map<String, Object> values = super.loadFile(source);
		logEvent(!values.isEmpty() ? ConfigEvent.VALUES_LOAD_SUCCESS : ConfigEvent.VALUES_LOAD_FAILED);
		return values;
	}
	
	public enum ConfigEvent {
		
		DEFAULTS_LOAD_START,
		DEFAULTS_LOAD_SUCCESS,
		DEFAULTS_LOAD_FAILED,
		VALUES_LOAD_START,
		VALUES_LOAD_SUCCESS,
		VALUES_LOAD_FAILED,
		FILE_SAVE_START,
		FILE_SAVE_SUCCESS,
		FILE_SAVE_FAILED,
		VERSION_UPDATE_START,
		VERSION_UPDATE_SUCCESS,
		VERSION_UPDATE_FAILED,
		CONFIG_RELOAD
	}

}
