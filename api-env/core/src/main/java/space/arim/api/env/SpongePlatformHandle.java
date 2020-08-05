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
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.SpongeTextConverter;
import space.arim.api.env.concurrent.SpongeEnhancedExecutor;
import space.arim.api.env.concurrent.SpongeFactoryOfTheFuture;
import space.arim.api.env.realexecutor.RealExecutorFinder;
import space.arim.api.env.realexecutor.SpongeRealExecutorFinder;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * Implementation of {@link PlatformHandle} specifically for Sponge servers
 * 
 * @author A248
 *
 */
public class SpongePlatformHandle extends AbstractPlatformHandle {
	
	/**
	 * Creates from a {@code PluginContainer} to use
	 * 
	 * @param plugin the plugin
	 */
	public SpongePlatformHandle(PluginContainer plugin) {
		super(PlatformType.SPONGE, plugin, Sponge.getGame());
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
	 * <b>This method is not thread safe.</b>
	 * 
	 */
	@Override
	public void disconnectUser(@PlatformPlayer Object user, SendableMessage reason) {
		disconnectUser((Player) user, reason);
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
		recipient.sendMessage(new SpongeTextConverter().convertFrom(message));
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
		user.kick(new SpongeTextConverter().convertFrom(reason));
	}
	
	@Override
	public RealExecutorFinder getRealExecutorFinder() {
		return new SpongeRealExecutorFinder(getPlugin());
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<ResourceInfo<T>> getResourceDefaultImplProvider(Class<T> resource) {
		if (resource == FactoryOfTheFuture.class) {
			return () -> {
				var factory = new SpongeFactoryOfTheFuture(getPlugin());
				return new ResourceInfo<T>("SpongeFactoryOfTheFuture", (T) factory, ShutdownHandlers.ofAutoClosable(factory));
			};
		}
		if (resource == EnhancedExecutor.class) {
			return () -> {
				var executor = new SpongeEnhancedExecutor(getPlugin());
				return new ResourceInfo<T>("SpongeEnhancedExecutor", (T) executor, ShutdownHandlers.ofStoppableService(executor));
			};
		}
		return null;
	}
	
}
