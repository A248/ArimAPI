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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

import space.arim.omnibus.util.concurrent.CentralisedFuture;
import space.arim.omnibus.util.concurrent.SynchronousExecutor;
import space.arim.omnibus.util.concurrent.impl.AbstractFactoryOfTheFuture;

abstract class DeadlockFreeFutureFactory extends AbstractFactoryOfTheFuture {

	private final Queue<Runnable> syncTasks = new ConcurrentLinkedQueue<>();
	volatile Thread mainThread;
	final SynchronousExecutor trustedSyncExecutor;
	
	DeadlockFreeFutureFactory() {
		trustedSyncExecutor = new TrustedSyncExecutor();
	}
	
	boolean isPrimaryThread() {
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
	
	abstract boolean isPrimaryThread0();
	
	@Override
	public <T> CentralisedFuture<T> newIncompleteFuture() {
		return new DeadlockFreeFuture<>(this);
	}
	
	void executeSync0(Runnable command) {
		if (isPrimaryThread()) {
			command.run();
			return;
		}
		syncTasks.offer(command);
		LockSupport.unpark(mainThread);
	}
	
	@Override
	public void executeSync(Runnable command) {
		executeSync0(new RunnableExceptionReporter(command));
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
	
	private static class RunnableExceptionReporter implements Runnable {
		
		private final Runnable command;
		
		RunnableExceptionReporter(Runnable command) {
			this.command = command;
		}
		
		@Override
		public void run() {
			try {
				command.run();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	class PeriodicSyncUnleasher implements Runnable {
		
		@Override
		public void run() {
			unleashSyncTasks();
		}
	}
	
	class TrustedSyncExecutor implements SynchronousExecutor {

		@Override
		public void executeSync(Runnable command) {
			executeSync0(command);
		}
	}

}
