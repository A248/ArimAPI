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

/**
 * Implementation of {@link PlatformHandle} specifically for Velocity servers.
 * 
 * @author A248
 *
 */
public class VelocityPlatformHandle extends AbstractPlatformHandle {

	/**
	 * Creates from a {@code PluginContainer} and {@code ProxyServer} to use
	 * 
	 * @param plugin the plugin
	 * @param server the server
	 */
	public VelocityPlatformHandle(PluginContainer plugin, ProxyServer server) {
		super(plugin, server);
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
	
}
