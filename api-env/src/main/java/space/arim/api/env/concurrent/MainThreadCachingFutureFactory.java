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

package space.arim.api.env.concurrent;

import space.arim.managedwaits.DeadlockFreeFutureFactory;
import space.arim.managedwaits.ManagedWaitStrategy;
import space.arim.managedwaits.TaskQueue;

abstract class MainThreadCachingFutureFactory extends DeadlockFreeFutureFactory {

	private volatile Thread mainThread;

	MainThreadCachingFutureFactory(TaskQueue taskQueue, ManagedWaitStrategy waitStrategy) {
		super(taskQueue, waitStrategy);
	}

	@Override
	public final boolean isPrimaryThread() {
		Thread mainThread = this.mainThread;
		if (mainThread == null) {
			if (isPrimaryThread0()) {
				mainThread = Thread.currentThread();
				this.mainThread = mainThread;
				return true;
			}
			return false;
		}
		return Thread.currentThread() == mainThread;
	}

	@Override
	public final Thread getPrimaryThread() {
		return mainThread;
	}

	abstract boolean isPrimaryThread0();

}
