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
	
	/**
	 * Since Gson is thread safe, this is a common Gson for any class to use. <br>
	 * <br>
	 * <b>Gson is an optional dependency of UniversalUtil. <br>
	 * If using this field, please be sure Gson is on the classpath!
	 */
	public static final Gson GSON = new Gson();
	
	/**
	 * Since SnakeYAML is not thread safe, this is a thread local supplier for SnakeYAML instances. <br>
	 * <br>
	 * <b>SnakeYAML is an optional dependency of UniversalUtil. <br>
	 * If using this field, please be sure SnakeYAML is on the classpath!
	 */
	public static final ThreadLocal<Yaml> SNAKE_YAML = ThreadLocal.withInitial(() -> new Yaml());
	
}
