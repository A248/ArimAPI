/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

import java.util.function.Supplier;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.Message;

public class SpongePlatformHandle extends AbstractPlatformHandle {

	public SpongePlatformHandle(PluginContainer plugin) {
		super(PlatformType.SPONGE, Sponge.getServiceManager().provideUnchecked(Registry.class), plugin, Sponge.getGame());
	}
	
	SpongePlatformHandle(Registry registry) {
		super(PlatformType.SPONGE, registry);
	}
	
	public PluginContainer getPlugin() {
		return (PluginContainer) pluginInfo.getPlugin();
	}

	@Override
	public void sendMessage(Object player, Message message) {
		sendMessage((Player) player, message);
	}
	
	public void sendMessage(Player player, Message message) {
		player.sendMessage(new SpongeTextConverter().convertFrom(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<Registration<T>> getDefaultServiceSupplier(Class<T> service) {
		if (service == FactoryOfTheFuture.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new SpongeFactoryOfTheFuture(getPlugin()), "SpongeFactoryOfTheFuture");
			};
		}
		if (service == EnhancedExecutor.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new SpongeEnhancedExecutor(getPlugin()), "SpongeEnhancedExecutor");
			};
		}
		return null;
	}
	
}
