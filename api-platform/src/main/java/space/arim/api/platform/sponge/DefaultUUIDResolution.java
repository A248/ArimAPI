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
package space.arim.api.platform.sponge;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.user.UserStorageService;

import space.arim.api.platform.PlatformUUIDResolution;

/**
 * A default implementation of {@link UUIDResolution} on the Sponge platform. <br>
 * <br>
 * Checks against existing players for uuid to name or name to uuid lookups. <br>
 * Falls back to the Ashcon API, and then to the Mojang API.
 * 
 * @author A248
 *
 */
public class DefaultUUIDResolution extends PlatformUUIDResolution {

	/**
	 * Creates the instance
	 * 
	 * @param plugin the plugin to use
	 */
	public DefaultUUIDResolution(PluginContainer plugin) {
		
	}
	
	@Override
	public UUID resolveFromCache(String name) {
		Optional<Player> player = Sponge.getServer().getPlayer(name);
		if (player.isPresent()) {
			return player.get().getUniqueId();
		}
		Optional<UserStorageService> storage = Sponge.getServiceManager().provide(UserStorageService.class);
		if (storage.isPresent()) {
			Optional<User> offlinePlayer = storage.get().get(name);
			if (offlinePlayer.isPresent()) {
				return offlinePlayer.get().getUniqueId();
			}
		}
		return null;
	}
	
	@Override
	public String resolveFromCache(UUID uuid) {
		Optional<Player> player = Sponge.getServer().getPlayer(uuid);
		if (player.isPresent()) {
			return player.get().getName();
		}
		Optional<UserStorageService> storage = Sponge.getServiceManager().provide(UserStorageService.class);
		if (storage.isPresent()) {
			Optional<User> offlinePlayer = storage.get().get(uuid);
			if (offlinePlayer.isPresent()) {
				return offlinePlayer.get().getName();
			}
		}
		return null;
	}
	
}
