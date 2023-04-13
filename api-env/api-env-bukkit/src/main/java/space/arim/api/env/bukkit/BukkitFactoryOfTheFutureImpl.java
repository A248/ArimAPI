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
import space.arim.api.env.concurrent.ClosableFactoryOfTheFuture;
import space.arim.api.env.concurrent.MainThreadCachingFutureFactory;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.managedwaits.SimpleTaskQueue;
import space.arim.managedwaits.TaskQueue;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.morepaperlib.scheduling.GracefulScheduling;
import space.arim.morepaperlib.scheduling.ScheduledTask;

final class BukkitFactoryOfTheFutureImpl extends MainThreadCachingFutureFactory implements ClosableFactoryOfTheFuture {

	private final GracefulScheduling scheduling;
	private final ScheduledTask task;

	private BukkitFactoryOfTheFutureImpl(TaskQueue taskQueue, ManagedWaitStrategy waitStrategy,
                                         GracefulScheduling scheduling, ScheduledTask task) {
		super(taskQueue, waitStrategy);
		this.scheduling = scheduling;
		this.task = task;
	}

	static ClosableFactoryOfTheFuture create(Plugin plugin, ManagedWaitStrategy waitStrategy) {
		SimpleTaskQueue taskQueue = new SimpleTaskQueue();
		GracefulScheduling scheduling = new MorePaperLib(plugin).scheduling();
		ScheduledTask task = scheduling.globalRegionalScheduler().runAtFixedRate(
				taskQueue::pollAndRunAll, 1L, 1L
		);
		BukkitFactoryOfTheFutureImpl futuresFactory = new BukkitFactoryOfTheFutureImpl(
				taskQueue, waitStrategy, scheduling, task
		);
		futuresFactory.isPrimaryThread(); // Init main thread if possible
		return futuresFactory;
	}

	@Override
	protected boolean isPrimaryThread0() {
		return scheduling.isOnGlobalRegionThread();
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
