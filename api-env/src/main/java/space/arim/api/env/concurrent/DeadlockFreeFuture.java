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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import space.arim.omnibus.util.concurrent.impl.BaseCentralisedFuture;

class DeadlockFreeFuture<T> extends BaseCentralisedFuture<T> {

	private final DeadlockFreeFutureFactory factory;
	
	DeadlockFreeFuture(DeadlockFreeFutureFactory factory) {
		super(factory.trustedSyncExecutor);
		this.factory = factory;
	}
	
	static {
		// Reduce risk of ill timed classloading
		@SuppressWarnings("unused")
		Class<?> ensureLoaded = LockSupport.class;
	}
	
	@Override
	public T join() {
		if (factory.isPrimaryThread() && !isDone()) {
			factory.unleashSyncTasks();
			while (!isDone()) {
				LockSupport.park(this);
				factory.unleashSyncTasks();
			}
		}
		return super.join();
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		if (factory.isPrimaryThread() && !isDone()) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks();
			while (!isDone()) {
				LockSupport.park(this);
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				factory.unleashSyncTasks();
			}
		}
		return super.get();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (factory.isPrimaryThread()) {
			if (isDone()) {
				return super.get();
			}
			if (timeout <= 0L) {
				throw new TimeoutException();
			}
			long deadline = System.nanoTime() + unit.toNanos(timeout);
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks(deadline);
			while (!isDone()) {
				LockSupport.parkNanos(this, deadline - System.nanoTime());
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				if (System.nanoTime() - deadline >= 0) {
					throw new TimeoutException();
				}
				factory.unleashSyncTasks(deadline);
			}
			return super.get();

		} else {
			return super.get(timeout, unit);
		}
	}

}
