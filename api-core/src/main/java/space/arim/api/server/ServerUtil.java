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
package space.arim.api.server;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple set of utilities for Minecraft servers.
 * 
 * @author A248
 *
 */
public final class ServerUtil {

	// Prevent instantiation
	private ServerUtil() {}
	
	/**
	 * Gets tab completions based on player names matching the command arguments, sorted alphabetically.
	 * 
	 * @param names the stream of player names
	 * @param args the command arguments
	 * @return a list of sorted matching names
	 */
	public static List<String> getPlayerNameTabComplete(Stream<String> names, String[] args) {
		return names.filter((name) -> name.toLowerCase().startsWith((args.length > 0) ? args[args.length - 1] : "")).sorted().collect(Collectors.toList());
	}
	
}
