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
package space.arim.api.server.sponge;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.user.UserStorageService;

import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;
import space.arim.api.uuid.PlayerNotFoundException;
import space.arim.api.uuid.UUIDResolver;

/**
 * A default implementation of {@link UUIDResolver} on the Sponge platform.
 * Simply checks against existing players for uuid to name or name to uuid lookups.
 * 
 * @author A248
 *
 */
public class DefaultUUIDResolver extends SpongeRegistrable implements UUIDResolver {

	/**
	 * Creates the instance. See {@link SpongeRegistrable#SpongeRegistrable(PluginContainer)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 */
	public DefaultUUIDResolver(PluginContainer plugin) {
		super(plugin);
	}
	
	@Override
	public UUID resolveName(String name, boolean query) throws PlayerNotFoundException {
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
