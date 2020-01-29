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
package space.arim.api.server.bukkit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;
import space.arim.api.uuid.PlayerNotFoundException;
import space.arim.api.uuid.UUIDResolver;

/**
 * A default implementation of {@link UUIDResolver} on the Bukkit platform.
 * Simply checks against existing players for uuid to name or name to uuid lookups.
 * 
 * @author A248
 *
 */
public class DefaultUUIDResolver extends BukkitRegistrable implements UUIDResolver {

	/**
	 * Creates the instance. See {@link BukkitRegistrable#BukkitRegistrable(Plugin)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 */
	public DefaultUUIDResolver(Plugin plugin) {
		super(plugin);
	}
	
	@Override
	public UUID resolveName(String name, boolean query) throws PlayerNotFoundException {
		for (Player player : getPlugin().getServer().getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(name)) {
				return player.getUniqueId();
			}
		}
		if (query) {
			try {
				return FetcherUtil.ashconApi(name);
			} catch (FetcherException | HttpStatusException ex) {}
			try {
				return FetcherUtil.mojangApi(name);
			} catch (FetcherException | HttpStatusException ex) {}
		}
		throw new PlayerNotFoundException(name);
	}
	
	@Override
	public String resolveUUID(UUID uuid, boolean query) throws PlayerNotFoundException {
		for (Player player : getPlugin().getServer().getOnlinePlayers()) {
			if (player.getUniqueId().equals(uuid)) {
				return player.getName();
			}
		}
		if (query) {
			try {
				return FetcherUtil.ashconApi(uuid);
			} catch (FetcherException | HttpStatusException ex) {}
			try {
				return FetcherUtil.mojangApi(uuid);
			} catch (FetcherException | HttpStatusException ex) {}
		}
		throw new PlayerNotFoundException(uuid);
	}
	
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}
	
}
