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
import space.arim.omnibus.resourcer.ShutdownHandler;
import space.arim.omnibus.resourcer.ShutdownHandlers;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.BungeeComponentConverter;
import space.arim.api.env.concurrent.BungeeEnhancedExecutor;
import space.arim.api.env.realexecutor.BungeeRealExecutorFinder;
import space.arim.api.env.realexecutor.RealExecutorFinder;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Implementation of {@link PlatformHandle} specifically for BungeeCord proxies.
 * 
 * @author A248
 *
 */
public class BungeePlatformHandle extends AbstractPlatformHandle {

	/**
	 * Creates from a {@code Plugin} to use
	 * 
	 * @param plugin the plugin
	 */
	public BungeePlatformHandle(Plugin plugin) {
		super(PlatformType.BUNGEE, plugin, plugin.getProxy());
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public Plugin getPlugin() {
		return (Plugin) getImplementingPluginInfo().getPlugin();
	}

	/**
	 * Sends a {@link SendableMessage} to a command sender based on this platform. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 */
	@Override
	public void sendMessage(@PlatformCommandSender Object recipient, SendableMessage message) {
		sendMessage((CommandSender) recipient, message);
	}
	
	/**
	 * Disconnects a player with a reason. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 */
	@Override
	public void disconnectUser(@PlatformPlayer Object user, SendableMessage reason) {
		disconnectUser((ProxiedPlayer) user, reason);
	}
	
	/**
	 * Sends a message to a recipient. Same as {@link #sendMessage(Object, SendableMessage)}. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 */
	public void sendMessage(CommandSender recipient, SendableMessage message) {
		recipient.sendMessage(new BungeeComponentConverter().convert(message).toArray(TextComponent[]::new));
	}
	
	/**
	 * Disconnects a player with a reason. Same as {@link #disconnectUser(Object, SendableMessage)}. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 * @param user the user to kick
	 * @param reason the kick message
	 */
	public void disconnectUser(ProxiedPlayer user, SendableMessage reason) {
		user.disconnect(new BungeeComponentConverter().convert(reason).toArray(TextComponent[]::new));
	}
	
	@Override
	public RealExecutorFinder getRealExecutorFinder() {
		return new BungeeRealExecutorFinder(getPlugin());
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<ResourceInfo<T>> getResourceDefaultImplProvider(Class<T> resource) {
		if (resource == FactoryOfTheFuture.class) {
			return () -> {
				var factory = new IndifferentFactoryOfTheFuture();
				return new ResourceInfo<T>("BungeeFactoryOfTheFuture", (T) factory, ShutdownHandler.none());
			};
		}
		if (resource == EnhancedExecutor.class) {
			return () -> {
				var executor = new BungeeEnhancedExecutor(getPlugin());
				return new ResourceInfo<T>("BungeeEnhancedExecutor", (T) executor, ShutdownHandlers.ofStoppableService(executor));
			};
		}
		return null;
	}

}
