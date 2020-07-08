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
package space.arim.api.env;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class DelegatingPlatformScheduler implements PlatformScheduler {

	private final ScheduledExecutorService delegate;
	
	DelegatingPlatformScheduler(ScheduledExecutorService delegate) {
		this.delegate = delegate;
	}
	
	private static class ScheduledFutureWrapper implements ScheduledTask {
		private final ScheduledFuture<?> scheduledFuture;
		ScheduledFutureWrapper(ScheduledFuture<?> scheduledFuture) {
			this.scheduledFuture = scheduledFuture;
		}
		@Override
		public void cancel() {
			scheduledFuture.cancel(false);
		}
		@Override
		public boolean isCancelled() {
			return scheduledFuture.isCancelled();
		}
		@Override
		public String toString() {
			return "ScheduledFutureWrapper [scheduledFuture=" + scheduledFuture + "]";
		}
	}
	
	@Override
	public ScheduledTask runDelayedTask(Runnable command, long delay, TimeUnit unit) {
		return new ScheduledFutureWrapper(delegate.schedule(command, delay, unit));
	}

	@Override
	public ScheduledTask runRepeatingTask(Runnable command, long initialDelay, long repeatingPeriod, TimeUnit unit) {
		return new ScheduledFutureWrapper(delegate.scheduleWithFixedDelay(command, initialDelay, repeatingPeriod, unit));
	}
	
}
