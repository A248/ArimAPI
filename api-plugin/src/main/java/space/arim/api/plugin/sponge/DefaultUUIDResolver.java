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
package space.arim.api.plugin.sponge;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.server.sponge.SpongeRegistrable;
import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;
import space.arim.api.uuid.PlayerNotFoundException;
import space.arim.api.uuid.UUIDResolver;

public class DefaultUUIDResolver extends SpongeRegistrable implements UUIDResolver {

	public DefaultUUIDResolver(PluginContainer plugin) {
		super(plugin);
	}
	
	@Override
	public UUID resolveName(String name, boolean query) throws PlayerNotFoundException {
		Optional<Player> player = Sponge.getServer().getPlayer(name);
		if (player.isPresent()) {
			return player.get().getUniqueId();
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
