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
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.realexecutor.RealExecutorFinder;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * Implementation of {@link PlatformHandle} specifically for Sponge servers
 * 
 * @author A248
 *
 * @deprecated Unfortunately, the Sponge platform relies on internals changed by
 *             JPMS in JDK 9 and beyond and is therefore incompatible with Java
 *             11, which ArimAPI requires. It is highly unlikely an
 *             implementation of Sponge API 7 will have compatibility. The
 *             maintenance burden of platform handles and adapters for Sponge
 *             API 7 is therefore not justified.
 */
@Deprecated(forRemoval = true)
public class SpongePlatformHandle implements PlatformHandle {
	
	/**
	 * Creates from a {@code PluginContainer} to use
	 * 
	 * @param plugin the plugin
	 */
	public SpongePlatformHandle(PluginContainer plugin) {
		throw uoe();
	}
	
	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		throw uoe();
	}
	
	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public PluginContainer getPlugin() {
		throw uoe();
	}

	/**
	 * Sends a {@link SendableMessage} to a command sender based on this platform. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 */
	@Override
	public void sendMessage(@PlatformCommandSender Object recipient, SendableMessage message) {
		throw uoe();
	}
	
	/**
	 * Disconnects a player with a reason. <br>
	 * <br>
	 * <b>This method is not thread safe.</b>
	 * 
	 */
	@Override
	public void disconnectUser(@PlatformPlayer Object user, SendableMessage reason) {
		throw uoe();
	}
	
	/**
	 * Sends a message to a recipient. Same as {@link #sendMessage(Object, SendableMessage)}. <br>
	 * <br>
	 * This method is thread safe.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 */
	public void sendMessage(CommandSource recipient, SendableMessage message) {
		throw uoe();
	}
	
	/**
	 * Disconnects a player with a reason. Same as {@link #disconnectUser(Object, SendableMessage)}. <br>
	 * <br>
	 * <b>This method is not thread safe.</b>
	 * 
	 * @param user the user to kick
	 * @param reason the kick message
	 */
	public void disconnectUser(Player user, SendableMessage reason) {
		throw uoe();
	}
	
	@Override
	public RealExecutorFinder getRealExecutorFinder() {
		throw uoe();
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		throw uoe();
	}
	
	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		throw uoe();
	}
	
	/**
	 * Creates an {@link UnsupportedOperationException} indicating Sponge support was removed
	 * 
	 * @return an unsupported operation exception
	 */
	public static UnsupportedOperationException uoe() {
		String message = "Not implemented - adapters and handles for Sponge API 7 are removed. See deprecation of SpongePlatformHandle";
		return new UnsupportedOperationException(message);
	}
	
}
