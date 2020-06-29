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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import space.arim.universal.util.concurrent.impl.BaseCentralisedFuture;

class DeadlockFreeFuture<T> extends BaseCentralisedFuture<T> {

	private final DeadlockFreeFutureFactory factory;
	
	DeadlockFreeFuture(DeadlockFreeFutureFactory factory) {
		super(factory);
		this.factory = factory;
	}
	
	@Override
	public T join() {
		if (factory.isPrimaryThread()) {
			factory.unleashSyncTasks();
			while (!isDone()) {
				LockSupport.park();
				factory.unleashSyncTasks();
			}
		}
		return super.join();
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		if (factory.isPrimaryThread()) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks();
			while (!isDone()) {
				LockSupport.park();
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
			long deadline = System.nanoTime() + unit.toNanos(timeout);
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks(deadline);
			while (!isDone()) {
				LockSupport.parkNanos(deadline - System.nanoTime());
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
