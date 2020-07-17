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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import space.arim.universal.util.concurrent.CentralisedFuture;
import space.arim.universal.util.concurrent.impl.AbstractFactoryOfTheFuture;

abstract class DeadlockFreeFutureFactory extends AbstractFactoryOfTheFuture {

	private final Queue<Runnable> syncTasks = new ConcurrentLinkedQueue<>();
	volatile Thread mainThread;
	
	private static final boolean DISABLE_LAZY_EXECUTE;
	
	static {
		boolean disableLazyExec = false;
		try {
			disableLazyExec = Boolean.getBoolean("space.arim.api.DeadlockFreeFutureFactory.disableLazyExec");
		} catch (SecurityException ignored) {}

		DISABLE_LAZY_EXECUTE = disableLazyExec;
	}
	
	DeadlockFreeFutureFactory() {
		
	}
	
	boolean isPrimaryThread() {
		if (mainThread == null) {
			if (isPrimaryThread0()) {
				mainThread = Thread.currentThread();
				return true;
			}
			return false;
		}
		return Thread.currentThread() == mainThread;
	}
	
	abstract boolean isPrimaryThread0();
	
	@Override
	protected <T> CentralisedFuture<T> newIncompleteFuture() {
		return new DeadlockFreeFuture<>(this);
	}
	
	@Override
	public void executeSync(Runnable command) {
		if (DISABLE_LAZY_EXECUTE && isPrimaryThread()) {
			command.run();
			return;
		}
		syncTasks.offer(command);
		LockSupport.unpark(mainThread);
	}
	
	/**
	 * Runs all scheduled tasks. Should only be called on main thread.
	 * 
	 */
	void unleashSyncTasks() {
		assert isPrimaryThread() : mainThread;

		Runnable toRun;
		while ((toRun = syncTasks.poll()) != null) {
			toRun.run();
		}
	}

	/**
	 * Same as previous, but with a checked timeout deadline after each ran task
	 * 
	 * @param deadline the deadline corresponding to <code>System.nanoTime()</code>
	 * @throws TimeoutException if the deadline was hit
	 */
	void unleashSyncTasks(long deadline) throws TimeoutException {
		assert isPrimaryThread() : mainThread;

		Runnable toRun;
		while ((toRun = syncTasks.poll()) != null) {
			toRun.run();
			if (System.nanoTime() - deadline >= 0) {
				throw new TimeoutException();
			}
		}
	}

}
