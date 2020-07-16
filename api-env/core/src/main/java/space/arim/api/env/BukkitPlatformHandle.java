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

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.BungeeComponentConverter;

/**
 * Implementation of {@link PlatformHandle} specifically for Bukkit/Spigot servers.
 * 
 * @author A248
 *
 */
public class BukkitPlatformHandle extends AbstractPlatformHandle {
	
	/**
	 * Creates from a JavaPlugin to use
	 * 
	 * @param plugin the plugin
	 */
	public BukkitPlatformHandle(JavaPlugin plugin) {
		super(PlatformType.BUKKIT, plugin.getServer().getServicesManager().load(Registry.class), plugin, plugin.getServer());
	}
	
	BukkitPlatformHandle(Registry registry) {
		super(PlatformType.BUKKIT, registry);
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return (JavaPlugin) pluginInfo.getPlugin();
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
	<T> Supplier<Registration<T>> getDefaultServiceSupplier(Class<T> service) {
		if (service == FactoryOfTheFuture.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new BukkitFactoryOfTheFuture(getPlugin()), "BukkitFactoryOfTheFutire");
			};
		}
		if (service == EnhancedExecutor.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new BukkitEnhancedExecutor(getPlugin()), "BukkitEnhancedExecutor");
			};
		}
		if (service == PlatformScheduler.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new BukkitPlatformScheduler(getPlugin()), "BukkitPlatformScheduler");
			};
		}
		return null;
	}

}
