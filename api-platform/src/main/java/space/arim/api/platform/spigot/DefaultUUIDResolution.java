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

/**
 * A default implementation of {@link UUIDResolution} on the Bukkit platform. <br>
 * <br>
 * Checks against existing players for uuid to name or name to uuid lookups. <br>
 * Falls back to the Ashcon API, and then to the Mojang API.
 * 
 * @author A248
 *
 */
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
	
	@Override
	public UUID resolveFromCache(String name) {
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
	
	@Override
	public String resolveFromCache(UUID uuid) {
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
	
}
