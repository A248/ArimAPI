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
package space.arim.api.env.velocity;

import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.AdventureTextConverter;
import space.arim.api.env.concurrent.VelocityEnhancedExecutor;
import space.arim.api.env.realexecutor.RealExecutorFinder;
import space.arim.api.env.realexecutor.VelocityRealExecutorFinder;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.identity.Identity;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import java.util.Objects;

/**
 * Implementation of {@link PlatformHandle} specifically for Velocity servers. <br>
 * <br>
 * <b>Subclassing or relying on the identity of {@code VelocityPlatformHandle} is deprecated.</b>
 * API users should declare references of type {@code PlatformHandle} and use the {@code create}
 * method instead of construction.
 * 
 * @author A248
 *
 */
public class VelocityPlatformHandle implements PlatformHandle {

	private final PluginContainer plugin;
	private final ProxyServer server;

	/**
	 * Creates from a {@code PluginContainer} and {@code ProxyServer} to use
	 * 
	 * @param plugin the plugin
	 * @param server the server
	 * @deprecated Use {@link #create(PluginContainer, ProxyServer)} which is not aware of
	 * the identity of the implementation. See the class javadoc for more info.
	 */
	@Deprecated
	public VelocityPlatformHandle(PluginContainer plugin, ProxyServer server) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
		this.server = Objects.requireNonNull(server, "server");
	}

	/**
	 * Creates from a plugin and proxy server to use
	 *
	 * @param plugin the plugin
	 * @param server the server
	 * @return the platform handle
	 */
	public static PlatformHandle create(PluginContainer plugin, ProxyServer server) {
		return new VelocityPlatformHandle(plugin, server);
	}

	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public PluginContainer getPlugin() {
		return (PluginContainer) getImplementingPluginInfo().getPlugin();
	}
	
	/**
	 * Gets the server used
	 * 
	 * @return the server
	 */
	public ProxyServer getServer() {
		return (ProxyServer) getImplementingPluginInfo().getServer();
	}

	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return new IndifferentFactoryOfTheFuture();
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new VelocityEnhancedExecutor(getPlugin(), getServer());
	}
	
	/**
	 * Sends a {@link SendableMessage} to a command sender based on this platform. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 */
	@Override
	public void sendMessage(@PlatformCommandSender Object recipient, SendableMessage message) {
		sendMessage((CommandSource) recipient, message);
	}
	
	/**
	 * Disconnects a player with a reason. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 */
	@Override
	public void disconnectUser(@PlatformPlayer Object user, SendableMessage reason) {
		disconnectUser((Player) user, reason);
	}
	
	/**
	 * Sends a message to a recipient. Same as {@link #sendMessage(Object, SendableMessage)} <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 */
	public void sendMessage(CommandSource recipient, SendableMessage message) {
		recipient.sendMessage(Identity.nil(), new AdventureTextConverter().convert(message));
	}
	
	/**
	 * Disconnects a player with a reason. Same as {@link #disconnectUser(Object, SendableMessage)}. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 * @param user the user to kick
	 * @param reason the kick message
	 */
	public void disconnectUser(Player user, SendableMessage reason) {
		user.disconnect(new AdventureTextConverter().convert(reason));
	}
	
	@Override
	public RealExecutorFinder getRealExecutorFinder() {
		return new VelocityRealExecutorFinder(getPlugin(), getServer());
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, server);
	}

	@Override
	public String getPlatformVersion() {
		return getServer().getVersion().getVersion();
	}
	
}
