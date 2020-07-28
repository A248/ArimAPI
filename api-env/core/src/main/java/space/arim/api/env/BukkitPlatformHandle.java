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

import space.arim.omnibus.resourcer.ResourceInfo;
import space.arim.omnibus.resourcer.ShutdownHandlers;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.BungeeComponentConverter;
import space.arim.api.env.concurrent.BukkitEnhancedExecutor;
import space.arim.api.env.concurrent.BukkitFactoryOfTheFuture;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Implementation of {@link PlatformHandle} specifically for Bukkit servers.
 * 
 * @author A248
 *
 */
public class BukkitPlatformHandle extends AbstractPlatformHandle {
	
	/**
	 * Creates from a {@code JavaPlugin} to use
	 * 
	 * @param plugin the plugin
	 */
	public BukkitPlatformHandle(JavaPlugin plugin) {
		super(PlatformType.BUKKIT, plugin, plugin.getServer());
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return (JavaPlugin) getImplementingPluginInfo().getPlugin();
	}

	@Override
	public void sendMessage(@PlatformPlayer Object player, SendableMessage message) {
		sendMessage((Player) player, message);
	}
	
	/**
	 * Sends a message to a player. When possible, should be preferred to {@link #sendMessage(Object, SendableMessage)}
	 * due to type safety.
	 * 
	 * @param player the recipient
	 * @param message the message
	 */
	public void sendMessage(Player player, SendableMessage message) {
		player.spigot().sendMessage(new BungeeComponentConverter().convertFrom(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<ResourceInfo<T>> getResourceDefaultImplProvider(Class<T> resource) {
		if (resource == FactoryOfTheFuture.class) {
			return () -> {
				var factory = new BukkitFactoryOfTheFuture(getPlugin());
				return new ResourceInfo<T>("BukkitFactoryOfTheFuture", (T) factory, ShutdownHandlers.ofAutoClosable(factory));
			};
		}
		if (resource == EnhancedExecutor.class) {
			return () -> {
				var executor = new BukkitEnhancedExecutor(getPlugin());
				return new ResourceInfo<T>("BukkitEnhancedExecutor", (T) executor, ShutdownHandlers.ofStoppableService(executor));
			};
		}
		return null;
	}

}
