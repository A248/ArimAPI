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
package space.arim.api.env.bukkit;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import space.arim.api.env.concurrent.ClosableFactoryOfTheFuture;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.managedwaits.SimpleTaskQueue;
import space.arim.managedwaits.TaskQueue;

final class BukkitFactoryOfTheFutureImpl extends MainThreadCachingFutureFactory implements ClosableFactoryOfTheFuture {

	private final Server server;
	private final BukkitTask task;

	private BukkitFactoryOfTheFutureImpl(TaskQueue taskQueue, ManagedWaitStrategy waitStrategy,
                                         Server server, BukkitTask task) {
		super(taskQueue, waitStrategy);
		this.server = server;
		this.task = task;
	}

	static ClosableFactoryOfTheFuture create(JavaPlugin plugin, ManagedWaitStrategy waitStrategy) {
		Server server = plugin.getServer();
		SimpleTaskQueue taskQueue = new SimpleTaskQueue();
		BukkitTask task = server.getScheduler().runTaskTimer(plugin, taskQueue::pollAndRunAll, 0L, 1L);

		BukkitFactoryOfTheFutureImpl futuresFactory = new BukkitFactoryOfTheFutureImpl(taskQueue, waitStrategy, server, task);
		futuresFactory.isPrimaryThread(); // init main thread if possible
		return futuresFactory;
	}

	@Override
	boolean isPrimaryThread0() {
		return server.isPrimaryThread();
	}
	
	@Override
	public void close() {
		/*
		 * By executing cancellation synchronously, and because our queue is FIFO,
		 * any previous submitted task will have a chance to complete.
		 */
		executeSync(task::cancel);
	}

}
