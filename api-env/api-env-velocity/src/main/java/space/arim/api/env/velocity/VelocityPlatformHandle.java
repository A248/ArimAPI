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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import java.util.Objects;

/**
 * Implementation of {@link PlatformHandle} specifically for Velocity proxies.
 * 
 * @author A248
 *
 */
public final class VelocityPlatformHandle {

	private VelocityPlatformHandle() { }

	/**
	 * Creates from a plugin and proxy server to use
	 *
	 * @param plugin the plugin
	 * @param server the server
	 * @return the platform handle
	 */
	public static PlatformHandle create(PluginContainer plugin, ProxyServer server) {
		return new VelocityPlatformHandleImpl(plugin, server);
	}

}
final class VelocityPlatformHandleImpl implements PlatformHandle {

	private final PluginContainer plugin;
	private final ProxyServer server;

	VelocityPlatformHandleImpl(PluginContainer plugin, ProxyServer server) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
		this.server = Objects.requireNonNull(server, "server");
	}

	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return new IndifferentFactoryOfTheFuture();
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new VelocityEnhancedExecutor(plugin, server);
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, server);
	}

	@Override
	public String getPlatformVersion() {
		return server.getVersion().getVersion();
	}
	
}
