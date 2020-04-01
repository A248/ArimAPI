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
 */
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
	
	@Override
	protected UUID resolveFromCache(String name) {
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		return (player != null) ? player.getUniqueId() : null;
	}
	
	@Override
	protected String resolveFromCache(UUID uuid) {
		ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
		return (player != null) ? player.getName() : null;
	}
	
}
