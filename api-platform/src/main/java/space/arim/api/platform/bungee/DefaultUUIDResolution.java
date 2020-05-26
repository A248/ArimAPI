/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform.bungee;

import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import space.arim.api.platform.PlatformUUIDResolution;
import space.arim.api.uuid.UUIDResolution;

/**
 * A default implementation of {@link UUIDResolution} on the BungeeCord platform. <br>
 * <br>
 * Checks against existing players for uuid to name or name to uuid lookups. <br>
 * Falls back to the Ashcon API, and then to the Mojang API.
 * 
 * @author A248
 *
 * @deprecated The {@link UUIDResolution} interface is itself deprecated
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DefaultUUIDResolution extends PlatformUUIDResolution {

	private final Plugin plugin;
	
	/**
	 * Creates the instance
	 * 
	 * @param plugin the plugin to use
	 */
	public DefaultUUIDResolution(Plugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * The static equivalent of this implementation of fast, nonblocking resolution.
	 * Only checks the server's own knowledge cache. Does nothing else.
	 * 
	 * @param plugin the plugin to use
	 * @param name the player's name
	 * @return the player's uuid or <code>null</code> if not found
	 */
	public static UUID resolveFromCache(Plugin plugin, String name) {
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		return (player != null) ? player.getUniqueId() : null;
	}
	
	/**
	 * The static equivalent of this implementation of fast, nonblocking resolution.
	 * Only checks the server's own knowledge cache. Does nothing else.
	 * 
	 * @param plugin the plugin to use
	 * @param uuid the player's uuid
	 * @return the player's name or <code>null</code> if not found
	 */
	public static String resolveFromCache(Plugin plugin, UUID uuid) {
		ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
		return (player != null) ? player.getName() : null;
	}
	
	@Override
	protected UUID resolveFromCache(String name) {
		return resolveFromCache(plugin, name);
	}
	
	@Override
	protected String resolveFromCache(UUID uuid) {
		return resolveFromCache(plugin, uuid);
	}
	
}
