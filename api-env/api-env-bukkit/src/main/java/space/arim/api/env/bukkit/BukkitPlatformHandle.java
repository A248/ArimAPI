/*
 * ArimAPI
 * Copyright © 2023 Anand Beh
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

package space.arim.api.env.bukkit;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.api.env.PlatformHandle;
import space.arim.api.env.PlatformPluginInfo;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import java.util.Objects;

/**
 * Implementation of {@link PlatformHandle} specifically for Bukkit servers.
 * 
 * @author A248
 *
 */
public final class BukkitPlatformHandle {

	private BukkitPlatformHandle() { }

	/**
	 * Creates from a plugin to use
	 *
	 * @param plugin the plugin
	 * @return the platform handle
	 */
	public static PlatformHandle create(JavaPlugin plugin) {
		return new BukkitPlatformHandleImpl(plugin);
	}

}

final class BukkitPlatformHandleImpl implements PlatformHandle {

	private final JavaPlugin plugin;

	BukkitPlatformHandleImpl(JavaPlugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
	}
	
	@Override
	public FactoryOfTheFuture createFuturesFactory() {
		return BukkitFactoryOfTheFuture.create(plugin);
	}

	@Override
	public EnhancedExecutor createEnhancedExecutor() {
		return new BukkitEnhancedExecutor(
				new MorePaperLib(plugin).scheduling().asyncScheduler()
		);
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return new PlatformPluginInfo(plugin, plugin.getServer());
	}

	@Override
	public String getPlatformVersion() {
		Server server = plugin.getServer();
		return server.getVersion() + " (API " + server.getBukkitVersion() + ")";
	}

}
