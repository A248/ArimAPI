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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.UniversalRegistry;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.FactoryOfTheFuture;
import space.arim.universal.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import space.arim.api.chat.Message;

/**
 * Implementation of {@link PlatformHandle} specifically for Velocity servers.
 * 
 * @author A248
 *
 */
public class VelocityPlatformHandle extends AbstractPlatformHandle {

	/**
	 * Creates from a PluginContainer and ProxyServer to use
	 * 
	 * @param plugin the plugin
	 * @param server the server
	 */
	public VelocityPlatformHandle(PluginContainer plugin, ProxyServer server) {
		super(PlatformType.VELOCITY, UniversalRegistry.get(), plugin, server);
	}
	
	VelocityPlatformHandle(Registry registry) {
		super(PlatformType.VELOCITY, registry);
	}

	/**
	 * Gets the plugin used
	 * 
	 * @return the plugin
	 */
	public PluginContainer getPlugin() {
		return (PluginContainer) pluginInfo.getPlugin();
	}
	
	/**
	 * Gets the server used
	 * 
	 * @return the server
	 */
	public ProxyServer getServer() {
		return (ProxyServer) pluginInfo.getServer();
	}
	
	@Override
	public void sendMessage(Object player, Message message) {
		sendMessage((Player) player, message);
	}
	
	public void sendMessage(Player player, Message message) {
		player.sendMessage(new KyoriTextConverter().convertFrom(message));
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> Supplier<Registration<T>> getDefaultServiceSupplier(Class<T> service) {
		if (service == FactoryOfTheFuture.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new IndifferentFactoryOfTheFuture(), "VelocityFactoryOfTheFuture");
			};
		}
		if (service == EnhancedExecutor.class) {
			return () -> {
				return new Registration<T>(DEFAULT_PRIORITY, (T) new VelocityEnhancedExecutor(getPlugin(), getServer()), "VelocityEnhancedExecutor");
			};
		}
		return null;
	}
	
}
