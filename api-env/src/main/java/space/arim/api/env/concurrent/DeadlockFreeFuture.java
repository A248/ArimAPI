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

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import space.arim.omnibus.util.concurrent.CentralisedFuture;
import space.arim.omnibus.util.concurrent.impl.BaseCentralisedFuture;

class DeadlockFreeFuture<T> extends BaseCentralisedFuture<T> {

	private final DeadlockFreeFutureFactory factory;
	
	private boolean noSignalChild;
	
	private static final Object ABSENT_VALUE = new Object();
	
	/**
	 * Creates without signalling completion
	 * 
	 * @param factory the deadlock free future factory
	 * @param sig parameter used to distinguish signature 
	 */
	private DeadlockFreeFuture(DeadlockFreeFutureFactory factory, Void sig) {
		super(factory.trustedSyncExecutor);
		this.factory = factory;
	}
	
	DeadlockFreeFuture(DeadlockFreeFutureFactory factory) {
		this(factory, null);
		whenCompleteSignal();
	}
	
	/*
	 * Completion signalling
	 */
	
	@Override
	public <U> CentralisedFuture<U> newIncompleteFuture() {
		boolean noSignalChild;
		synchronized (this) {
			noSignalChild = this.noSignalChild;
		}
		if (noSignalChild) {
			return new DeadlockFreeFuture<>(factory, null);
		} else {
			return new DeadlockFreeFuture<>(factory);
		}
	}
	
	private synchronized void whenCompleteSignal() {
		noSignalChild = true;
		super.whenComplete((ignore1, ignore2) -> {
			factory.signal();
		});
		noSignalChild = false;
	}
	
	/*
	 * 
	 * Managed wait implementation
	 * 
	 */
	
	/**
	 * Gets the completed value, or {@code ABSENT_VALUE} if not completed. <br>
	 * <br>
	 * If completed exceptionally, throws in accordance with {@link CompletableFuture#join()}
	 * 
	 * @return the completed value or {@code ABSENT_VALUE}
	 * @throws CancellationException if the computation was cancelled
	 * @throws CompletionException if this future completed exceptionally or a completion computation threw an exception
	 */
	// Used to avoid double volatile reads
	@SuppressWarnings("unchecked")
	private T getNowJoin() {
		return getNow((T) ABSENT_VALUE);
	}
	
	/**
	 * Gets the completed value, or {@code ABSENT_VALUE} if not completed. <br>
	 * <br>
	 * If completed exceptionally, throws in accordance with {@link CompletableFuture#get()}
	 * 
	 * @return the completed value or {@code ABSENT_VALUE}
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if this future completed exceptionally
	 */
	private T getNowGet() throws ExecutionException {
		try {
			return getNowJoin();
		} catch (CompletionException ex) {
			Throwable cause = ex.getCause();
			throw new ExecutionException((cause == null) ? ex : cause);
		}
	}
	
	// Managed waits
	
	@Override
	public T join() {
		if (!factory.isPrimaryThread()) {
			return super.join();
		}

		for (;;) {
			T result;
			if ((result = getNowJoin()) != ABSENT_VALUE) {	// if (isDone()) {
				return result;								// return super.join(); }
			}
			factory.unleashSyncTasks();
			factory.completionLock.lock();
			try {
				if ((result = getNowJoin()) != ABSENT_VALUE) {	// if (isDone()) {
					return result;								// return super.join(); }
				}
				factory.completionCondition.awaitUninterruptibly();
			} finally {
				factory.completionLock.unlock();
			}
		}
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		if (!factory.isPrimaryThread()) {
			return super.get();
		}

		T result;
		if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
			return result;								// return super.get(); }
		}
		for (;;) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks();
			factory.completionLock.lockInterruptibly();
			try {
				if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
					return result;								// return super.get(); }
				}
				factory.completionCondition.await();
			} finally {
				factory.completionLock.unlock();
			}
			if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
				return result;								// return super.get(); }
			}
		}
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (!factory.isPrimaryThread()) {
			return super.get(timeout, unit);
		}

		T result;
		if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
			return result;								// return super.get(); }
		}
		if (timeout <= 0L) {
			throw new TimeoutException();
		}
		long deadline = System.nanoTime() + unit.toNanos(timeout);
		for (;;) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			factory.unleashSyncTasks(deadline);
			factory.completionLock.lockInterruptibly();
			try {
				if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
					return result;								// return super.get(); }
				}
				factory.completionCondition.awaitNanos(deadline - System.nanoTime());
			} finally {
				factory.completionLock.unlock();
			}
			if (System.nanoTime() - deadline >= 0) {
				throw new TimeoutException();
			}
			if ((result = getNowGet()) != ABSENT_VALUE) {	// if (isDone()) {
				return result;								// return super.get(); }
			}
		}
	}

}
