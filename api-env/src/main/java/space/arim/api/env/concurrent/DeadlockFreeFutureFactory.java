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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import space.arim.omnibus.util.concurrent.CentralisedFuture;
import space.arim.omnibus.util.concurrent.SynchronousExecutor;
import space.arim.omnibus.util.concurrent.impl.AbstractFactoryOfTheFuture;

abstract class DeadlockFreeFutureFactory extends AbstractFactoryOfTheFuture {

	private final Queue<Runnable> syncTasks = new ConcurrentLinkedQueue<>();
	private volatile Thread mainThread;
	final Runnable runQueuedTasks = new PeriodicSyncUnleasher();
	final SynchronousExecutor trustedSyncExecutor = new TrustedSyncExecutor();

	private final ManagedWaitStrategy waitStrategy;
	
	DeadlockFreeFutureFactory(ManagedWaitStrategy waitStrategy) {
		this.waitStrategy = waitStrategy;
	}
	
	@Override
	public <T> CentralisedFuture<T> newIncompleteFuture() {
		return new DeadlockFreeFuture<>(this);
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
	
	private void executeSyncNoExceptionGuard(Runnable command) {
		if (isPrimaryThread()) {
			command.run();
			return;
		}
		syncTasks.offer(command);
		waitStrategy.signalWhenTaskAdded(mainThread);
	}

	boolean requireSignalWhenFutureCompleted() {
		return waitStrategy.requireSignalWhenFutureCompleted();
	}

	void signalFutureCompleted() {
		waitStrategy.signalWhenFutureCompleted(mainThread);
	}

	<T> T await(DeadlockFreeFuture<T> future) {
		return waitStrategy.await(runQueuedTasks, future);
	}

	<T> T awaitInterruptibly(DeadlockFreeFuture<T> future) throws InterruptedException, ExecutionException {
		return waitStrategy.awaitInterruptibly(runQueuedTasks, future);
	}

	<T> T awaitUntil(DeadlockFreeFuture<T> future, long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, ExecutionException {
		return waitStrategy.awaitUntil(runQueuedTasks, future, timeout, unit);
	}
	
	@Override
	public void executeSync(Runnable command) {
		executeSyncNoExceptionGuard(new RunnableExceptionReporter(command));
	}
	
	/**
	 * Runs all scheduled tasks. Should only be called if known to be on main thread.
	 * 
	 */
	private void unleashSyncTasks() {
		Runnable syncTask;
		while ((syncTask = syncTasks.poll()) != null) {
			syncTask.run();
		}
	}
	
	private class PeriodicSyncUnleasher implements Runnable {
		
		@Override
		public void run() {
			unleashSyncTasks();
		}
	}
	
	private class TrustedSyncExecutor implements SynchronousExecutor {

		@Override
		public void executeSync(Runnable command) {
			executeSyncNoExceptionGuard(command);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [syncTasks=" + syncTasks + ", mainThread=" + mainThread + "]";
	}

}
