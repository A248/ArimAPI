/* 
 * ArimAPI-env
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FactoryImpl extends DeadlockFreeFutureFactory {

	private final Thread mainThread = Thread.currentThread();
	private final ScheduledFuture<?> task;
	
	FactoryImpl(ScheduledExecutorService scheduledExecutor) {
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.debug("Created factory " + this + " on main thread " + mainThread.getName());
		task = scheduledExecutor.scheduleWithFixedDelay(new PeriodicSyncUnleasher(), 0L, 100L, TimeUnit.MILLISECONDS);
	}
	
	void cancel() {
		task.cancel(false);
	}
	
	@Override
	boolean isPrimaryThread0() {
		return Thread.currentThread() == mainThread;
	}
	
}
