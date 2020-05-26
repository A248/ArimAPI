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
package space.arim.api.platform.spigot;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.api.platform.PlatformUUIDResolution;
import space.arim.api.uuid.UUIDResolution;

/**
 * A default implementation of {@link UUIDResolution} on the Bukkit platform. <br>
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

	private final JavaPlugin plugin;
	
	/**
	 * Creates the instance
	 * 
	 * @param plugin the plugin to use
	 */
	public DefaultUUIDResolution(JavaPlugin plugin) {
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
	public static UUID resolveFromCache(JavaPlugin plugin, String name) {
		Player player = plugin.getServer().getPlayer(name);
		if (player != null) {
			return player.getUniqueId();
		}
		for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
			if (offlinePlayer.getName().equalsIgnoreCase(name)) {
				return offlinePlayer.getUniqueId();
			}
		}
		return null;
	}
	
	/**
	 * The static equivalent of this implementation of fast, nonblocking resolution.
	 * Only checks the server's own knowledge cache. Does nothing else.
	 * 
	 * @param plugin the plugin to use
	 * @param uuid the player's uuid
	 * @return the player's name or <code>null</code> if not found
	 */
	public static String resolveFromCache(JavaPlugin plugin, UUID uuid) {
		Player player = plugin.getServer().getPlayer(uuid);
		if (player != null) {
			return player.getName();
		}
		for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
			if (offlinePlayer.getUniqueId().equals(uuid)) {
				return offlinePlayer.getName();
			}
		}
		return null;
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
