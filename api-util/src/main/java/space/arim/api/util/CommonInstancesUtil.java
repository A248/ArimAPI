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
package space.arim.api.util;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;

/**
 * Provides common instances of thread safe objects which may be shared for greater efficiency.
 * 
 * @author A248
 *
 */
public final class CommonInstancesUtil {

	private CommonInstancesUtil() {}
	
	private static final LazySingleton<Gson> GSON = new LazySingleton<Gson>(Gson::new);
	
	private static final ThreadLocal<Yaml> SNAKE_YAML = ThreadLocal.withInitial(Yaml::new);
	
	/**
	 * Gets a common, shareable gson value.
	 * 
	 * @return a thread safe <code>Gson</code> instance
	 */
	public static Gson gson() {
		return GSON.get();
	}
	
	/**
	 * Gets a thread specific <code>Yaml</code> instance, from a thread local supplier for SnakeYAML instances.
	 * 
	 * @return a <code>Yaml</code> instance for the current thread
	 */
	public static Yaml snakeYaml() {
		return SNAKE_YAML.get();
	}
	
}
