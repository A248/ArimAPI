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
package space.arim.api.env.concurrent;

import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * An implementation of {@link FactoryOfTheFuture} for the Bukkit platform, using a specified
 * plugin to execute synchronous tasks. <br>
 * <br>
 * When no longer needed, {@link #close()} should be called to cleanup.
 * 
 * @author A248
 *
 */
public final class BukkitFactoryOfTheFuture extends DeadlockFreeFutureFactory implements AutoCloseable {

	private final Server server;
	private final BukkitTask task;
	
	/**
	 * Creates from a {@code JavaPlugin} to use, with the default wait strategy
	 * 
	 * @param plugin the plugin to use
	 */
	public BukkitFactoryOfTheFuture(JavaPlugin plugin) {
		this(plugin, new LightSleepManagedWaitStrategy());
	}

	/**
	 * Creates from a {@code JavaPlugin} and {@link ManagedWaitStrategy} to use for managed waits
	 *
	 * @param plugin the plugin
	 * @param waitStrategy the managed wait strategy
	 */
	public BukkitFactoryOfTheFuture(JavaPlugin plugin, ManagedWaitStrategy waitStrategy) {
		super(waitStrategy);
		Server server = plugin.getServer();
		this.server = server;

		isPrimaryThread(); // init main thread if possible
		task = server.getScheduler().runTaskTimer(plugin, runQueuedTasks, 0L, 1L);
	}

	@Override
	boolean isPrimaryThread0() {
		return server.isPrimaryThread();
	}
	
	@Override
	public void close() {
		/*
		 * By executing cancellation synchronously, and because our queue is FIFO,
		 * any previous submitted task WILL have a chance to complete.
		 */
		executeSync(task::cancel);
	}

}
