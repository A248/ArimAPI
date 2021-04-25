/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.bungee;

import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;

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
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import java.util.Objects;

/**
 * Implementation of {@link PlatformHandle} specifically for BungeeCord proxies. <br>
 * <br>
 * <b>Subclassing or relying on the identity of {@code BungeePlatformHandle} is deprecated.</b>
 * API users should declare references of type {@code PlatformHandle} and use the {@code create}
 * method instead of construction.
 *
 * @author A248
 *
 */
public class BungeePlatformHandle implements PlatformHandle {

	private final Plugin plugin;

	/**
	 * Creates from a {@code Plugin} to use
	 * 
	 * @param plugin the plugin
	 * @deprecated Use {@link #create(Plugin)} which is not aware of
	 * the identity of the implementation. See the class javadoc for more info.
	 */
	@Deprecated
	public BungeePlatformHandle(Plugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
	}

	/**
	 * Creates from a plugin to use
	 *
	 * @param plugin the plugin
	 * @return the platform handle
	 */
	public static PlatformHandle create(Plugin plugin) {
		return new BungeePlatformHandle(plugin);
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return new IndifferentFactoryOfTheFuture();
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new BungeeEnhancedExecutor(getPlugin());
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

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, plugin.getProxy());
	}

	@Override
	public String getPlatformVersion() {
		return getPlugin().getProxy().getVersion();
	}

}
