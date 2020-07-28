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

import space.arim.omnibus.util.AutoClosable;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
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
public class BukkitFactoryOfTheFuture extends DeadlockFreeFutureFactory implements AutoClosable {

	private final Server server;
	private final BukkitTask task;
	
	/**
	 * Creates from a {@code JavaPlugin} to use for synchronous execution
	 * 
	 * @param plugin the plugin to use
	 */
	public BukkitFactoryOfTheFuture(JavaPlugin plugin) {
		Server server = plugin.getServer();
		this.server = server;
		BukkitScheduler scheduler = server.getScheduler();
		scheduler.runTask(plugin, () -> {
			mainThread = Thread.currentThread();
		});
		task = scheduler.runTaskTimer(plugin, new PeriodicSyncUnleasher(), 0L, 1L);
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
		executeSync0(task::cancel);
	}

}
