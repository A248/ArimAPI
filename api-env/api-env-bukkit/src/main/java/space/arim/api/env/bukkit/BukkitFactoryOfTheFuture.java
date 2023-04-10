/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
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

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.api.env.concurrent.ClosableFactoryOfTheFuture;
import space.arim.managedwaits.LightSleepManagedWaitStrategy;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

/**
 * An implementation of {@link FactoryOfTheFuture} for the Bukkit platform, using a specified
 * plugin to execute synchronous tasks.
 *
 */
public final class BukkitFactoryOfTheFuture {

	private BukkitFactoryOfTheFuture() {}

	/**
	 * Creates from a plugin to use, with the default wait strategy
	 *
	 * @param plugin the plugin
	 * @return the futures factory, which should be closed when disposed of
	 */
	public static ClosableFactoryOfTheFuture create(JavaPlugin plugin) {
		return create(plugin, new LightSleepManagedWaitStrategy());
	}

	/**
	 * Creates from a plugin and {@link ManagedWaitStrategy} to use for managed waits
	 *
	 * @param plugin the plugin
	 * @param waitStrategy the managed wait strategy
	 * @return the futures factory, which should be closed when disposed of
	 */
	public static ClosableFactoryOfTheFuture create(Plugin plugin, ManagedWaitStrategy waitStrategy) {
		return BukkitFactoryOfTheFutureImpl.create(plugin, waitStrategy);
	}

}
