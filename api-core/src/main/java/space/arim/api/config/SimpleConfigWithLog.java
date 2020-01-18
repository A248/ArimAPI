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
import java.util.logging.Level;
import java.util.logging.Logger;

import space.arim.api.util.FilesUtil;

/**
 * A simple implementation of {@link ConfigWithLog}. <br>
 * <br>
 * <b>Usage:</b> Extend this class.
 * 
 * @author A248
 *
 */
public abstract class SimpleConfigWithLog extends ConfigWithLog {
	
	private final Logger logger;
	
	public SimpleConfigWithLog(Logger logger, File folder, String filename, String versionKey) {
		super(folder, filename, versionKey);
		this.logger = logger;
	}
	
	public SimpleConfigWithLog(Logger logger, File folder, String versionKey) {
		this(logger, folder, "config.yml", versionKey);
	}
	
	@Override
	protected void logEvent(ConfigEvent evt) {
		switch (evt) {
		case DEFAULTS_LOAD_START:
			logger.log(Level.FINE, "Attempting to load configuration defaults...");
			break;
		case DEFAULTS_LOAD_SUCCESS:
			logger.log(Level.INFO, "Success! Loaded configuration defaults.");
			break;
		case DEFAULTS_LOAD_FAILED:
			logger.log(Level.WARNING, "Error! Failed to load configuration defaults!");
			break;
		case FILE_SAVE_START:
			logger.log(Level.FINE, "Attempting to save configuration to file...");
			break;
		case FILE_SAVE_SUCCESS:
			logger.log(Level.INFO, "Success! Configuration saved to file.");
			break;
		case FILE_SAVE_FAILED:
			logger.log(Level.WARNING, "Error! Failed to save configuration file!");
			break;
		case LOAD_FILE_START:
			logger.log(Level.FINE, "Attempting to load configuration from file...");
			break;
		case LOAD_FILE_SUCCESS:
			logger.log(Level.INFO, "Success! Configuration loaded from file.");
			break;
		case LOAD_FILE_FAILED:
			logger.log(Level.WARNING, "Error! Failed to load configuration file!");
			break;
		case VERSION_UPDATE_START:
			logger.log(Level.CONFIG, "Detected outdated configuration version! Updating...");
			break;
		case VERSION_UPDATE_SUCCESS:
			logger.log(Level.INFO, "Success! Your configuration was updated to the latest version. A backup of your old configuration has also been saved.");
			break;
		case VERSION_UPDATE_FAILED:
			logger.log(Level.WARNING, "Error! Configuration version update failed!");
			break;
		case CONFIG_RELOAD:
			logger.log(Level.FINE, "Reloading configuration...");
			break;
		default:
			logger.log(Level.FINE, "Configuring...");
			break;
		}
	}
	
	@Override
	protected File getBackupLocation() {
		return FilesUtil.dateSuffixedFile(folder, filename + "-", "config-backups");
	}

}
