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

package space.arim.api.env.sponge;

import org.spongepowered.api.Server;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.plugin.PluginContainer;
import space.arim.api.env.concurrent.ClosableFactoryOfTheFuture;
import space.arim.api.env.concurrent.MainThreadCachingFutureFactory;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.managedwaits.SimpleTaskQueue;
import space.arim.managedwaits.TaskQueue;

final class SpongeFactoryOfTheFutureImpl extends MainThreadCachingFutureFactory implements ClosableFactoryOfTheFuture {

	private final Server server;
	private final ScheduledTask task;

	private SpongeFactoryOfTheFutureImpl(TaskQueue taskQueue, ManagedWaitStrategy waitStrategy,
										 Server server, ScheduledTask task) {
		super(taskQueue, waitStrategy);
		this.server = server;
		this.task = task;
	}

	static ClosableFactoryOfTheFuture create(PluginContainer plugin, Server server, ManagedWaitStrategy waitStrategy) {
		SimpleTaskQueue taskQueue = new SimpleTaskQueue();
		ScheduledTask task = server.scheduler().submit(Task.builder()
				.interval(Ticks.of(1L))
				.execute(taskQueue::pollAndRunAll)
				.plugin(plugin)
				.build());

		SpongeFactoryOfTheFutureImpl futuresFactory = new SpongeFactoryOfTheFutureImpl(taskQueue, waitStrategy, server, task);
		futuresFactory.isPrimaryThread(); // Init main thread if possible
		return futuresFactory;
	}

	@Override
	protected boolean isPrimaryThread0() {
		return server.onMainThread();
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
