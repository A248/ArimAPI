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
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import space.arim.api.util.CommonInstancesUtil;

/**
 * Simple functionality for loading an internal JAR resource. <br>
 * Not designed to be passed around. Merely adds functionality.
 *
 * @author A248
 *
 */
public interface YmlLoader {

	/**
	 * Loads an internal JAR resource using Snake YAML.
	 * 
	 * @param filename the name of the file, e.g. "config.yml"
	 * @return a configuration map
	 */
	@SuppressWarnings("unchecked")
	default Map<String, Object> loadResource(String filename) {
		try (InputStream stream = getClass().getResourceAsStream(File.separator + filename)) {
			return (Map<String, Object>) CommonInstancesUtil.SNAKE_YAML.get().load(stream);
		} catch (IOException ignored) {}
		return Collections.emptyMap();
	}
	
	/**
	 * Similar to {@link #loadResource(String)}, but with the additional functionality of handling exceptions.
	 * 
	 * @param filename the name of the file, e.g. "config.yml"
	 * @param exceptionHandler with which to handle exceptions
	 * @return a configuration map
	 */
	@SuppressWarnings("unchecked")
	default Map<String, Object> loadResource(String filename, Consumer<IOException> exceptionHandler) {
		try (InputStream stream = getClass().getResourceAsStream(File.separator + filename)) {
			return (Map<String, Object>) CommonInstancesUtil.SNAKE_YAML.get().load(stream);
		} catch (IOException ex) {
			exceptionHandler.accept(ex);
		}
		return Collections.emptyMap();
	}
	
}
