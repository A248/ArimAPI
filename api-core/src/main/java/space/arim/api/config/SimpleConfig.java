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
import space.arim.api.util.FilesUtil;

/**
 * A simple implementation of {@link Config}
 * 
 * @author A248
 *
 */
public class SimpleConfig extends Config implements SimpleConfigGetters {
	
	public SimpleConfig(File folder, String filename, int version, String versionKey) {
		super(folder, filename, version, versionKey);
	}
	
	public SimpleConfig(File folder, int version, String versionKey) {
		this(folder, "config.yml", version, versionKey);
	}
	
	@Override
	protected File getBackupLocation() {
		return FilesUtil.dateSuffixedFile(folder, filename + "-", "config-backups");
	}
	
}
