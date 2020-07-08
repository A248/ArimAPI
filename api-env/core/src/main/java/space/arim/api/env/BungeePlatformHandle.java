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

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.UniversalRegistry;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.FactoryOfTheFuture;
import space.arim.universal.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import space.arim.api.chat.Message;
import space.arim.api.env.annote.PlatformPlayer;

/**
 * Implementation of {@link PlatformHandle} specifically for BungeeCord proxies.
 * 
 * @author A248
 *
 */
public class BungeePlatformHandle extends AbstractPlatformHandle {

	/**
	 * Creates from a Plugin to use
	 * 
	 * @param plugin the plugin
	 */
	public BungeePlatformHandle(Plugin plugin) {
		super(PlatformType.BUNGEE, UniversalRegistry.get(), plugin, plugin.getProxy());
	}
	
	BungeePlatformHandle(Registry registry) {
		super(PlatformType.BUNGEE, registry);
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public Plugin getPlugin() {
		return (Plugin) pluginInfo.getPlugin();
	}

	@Override
	public void sendMessage(@PlatformPlayer Object player, Message message) {
		sendMessage((ProxiedPlayer) player, message);
	}
	
	/**
	 * Sends a message to a player. When possible, should be preferred to {@link #sendMessage(Object, Message)}
	 * due to type safety.
	 * 
	 * @param player the recipient
	 * @param message the message
	 */
	public void sendMessage(ProxiedPlayer player, Message message) {
		player.sendMessage(new BungeeComponentConverter().convertFrom(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<Registration<T>> getDefaultServiceSupplier(Class<T> service) {
		if (service == FactoryOfTheFuture.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new IndifferentFactoryOfTheFuture(), "BungeeFactoryOfTheFuture");
			};
		}
		if (service == EnhancedExecutor.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new BungeeEnhancedExecutor(getPlugin()), "BungeeEnhancedExecutor");
			};
		}
		if (service == PlatformScheduler.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new BungeePlatformScheduler(getPlugin()), "BungeePlatformScheduler");
			};
		}
		return null;
	}

}
