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

import net.md_5.bungee.api.plugin.Plugin;
import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.IndifferentFactoryOfTheFuture;

import java.util.Objects;

/**
 * Implementation of {@link PlatformHandle} specifically for BungeeCord proxies.
 *
 * @author A248
 *
 */
public final class BungeePlatformHandle {

	private BungeePlatformHandle() { }

	/**
	 * Creates from a plugin to use
	 *
	 * @param plugin the plugin
	 * @return the platform handle
	 */
	public static PlatformHandle create(Plugin plugin) {
		return new BungeePlatformHandleImpl(plugin);
	}

}
final class BungeePlatformHandleImpl implements PlatformHandle {

	private final Plugin plugin;

	BungeePlatformHandleImpl(Plugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
	}

	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return new IndifferentFactoryOfTheFuture();
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new BungeeEnhancedExecutor(plugin);
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, plugin.getProxy());
	}

	@Override
	public String getPlatformVersion() {
		return plugin.getProxy().getVersion();
	}

}
