/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.server.bungee;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ProxyServer;

/**
 * Basic BungeeCord utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
public final class BungeeUtil {

	private BungeeUtil() {}
	
	/**
	 * Iterates across online players, finds players matching the first argument in the args parameter, and returns applicable player names.
	 * 
	 * @param args command arguments to tab complete
	 * @param proxy the proxy server, use {@link net.md_5.bungee.api.plugin.Plugin#getProxy() Plugin.getProxy()} for this parameter
	 * @return Set tab-completion set
	 */
	public static Set<String> getPlayerNameTabComplete(String[] args, ProxyServer proxy) {
		Set<String> playerNames = new HashSet<String>();
		if (args.length == 0) {
			proxy.getPlayers().forEach((player) -> {
				playerNames.add(player.getName());
			});
		} else if (args.length == 1) {
			proxy.getPlayers().forEach((player) -> {
				if (player.getName().toLowerCase().startsWith(args[0])) {
					playerNames.add(player.getName());
				}
			});
		}
		return playerNames;
	}
	
}
