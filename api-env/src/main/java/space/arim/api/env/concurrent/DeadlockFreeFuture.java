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
	
	private boolean dontSignalChildFuture;
	
	/**
	 * Creates without signalling completion
	 * 
	 * @param factory the deadlock free future factory
	 * @param sig parameter used to distinguish signature 
	 */
	private DeadlockFreeFuture(DeadlockFreeFutureFactory factory, @SuppressWarnings("unused") Void sig) {
		super(factory.trustedSyncExecutor);
		this.factory = factory;
	}
	
	DeadlockFreeFuture(DeadlockFreeFutureFactory factory) {
		this(factory, null);
		if (factory.requireSignalWhenFutureCompleted()) {
			whenCompleteSignal();
		}
	}
	
	/*
	 * Completion signalling
	 */
	
	@Override
	public <U> CentralisedFuture<U> newIncompleteFuture() {
		DeadlockFreeFuture<U> childFuture = new DeadlockFreeFuture<>(factory, null);

		if (factory.requireSignalWhenFutureCompleted()) {
			synchronized (this) {
				if (!dontSignalChildFuture) {
					childFuture.whenCompleteSignal();
				}
			}
		}
		return childFuture;
	}
	
	private synchronized void whenCompleteSignal() {
		dontSignalChildFuture = true;
		super.whenComplete((ignore1, ignore2) -> factory.signalFutureCompleted());
		dontSignalChildFuture = false;
	}
	
	/*
	 * 
	 * Managed wait implementation
	 * 
	 */

	/**
	 * Placeholder for value not yet complete
	 *
	 */
	static final Object ABSENT_VALUE = new Object();

	/**
	 * Gets the completed value, or {@code ABSENT_VALUE} if not completed. If completed exceptionally,
	 * throws in accordance with {@link CompletableFuture#join()}. <br>
	 * <br>
	 * This method is used primarily for performance purposes. It avoids the double volatile read
	 * which would be associated with: <br>
	 * <code>if (future.isDone()) { return future.join(); }</code>
	 *
	 * @param <T> the type of the future
	 * @param future the future
	 * @return the completed value or {@code ABSENT_VALUE}
	 * @throws CancellationException if the computation was cancelled
	 * @throws CompletionException if this future completed exceptionally or a completion computation threw an exception
	 */
	@SuppressWarnings("unchecked")
	static <T> T reportJoin(CentralisedFuture<T> future) {
		return future.getNow((T) ABSENT_VALUE);
	}
	
	/**
	 * Gets the completed value, or {@code ABSENT_VALUE} if not completed. If completed exceptionally,
	 * throws in accordance with {@link CompletableFuture#get()}
	 *
	 * @param <T> the type of the future
	 * @param future the future
	 * @return the completed value or {@code ABSENT_VALUE}
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if this future completed exceptionally
	 */
	static <T> T reportGet(CentralisedFuture<T> future) throws ExecutionException {
		try {
			return reportJoin(future);
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
		T result;
		if ((result = reportJoin(this)) != ABSENT_VALUE) {		// if (isDone()) {
			return result;										// return super.join(); }
		}
		return factory.await(this);
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		if (!factory.isPrimaryThread()) {
			return super.get();
		}
		T result;
		if ((result = reportGet(this)) != ABSENT_VALUE) {	// if (isDone()) {
			return result;									// return super.get(); }
		}
		return factory.awaitInterruptibly(this);
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (!factory.isPrimaryThread()) {
			return super.get(timeout, unit);
		}
		T result;
		if ((result = reportGet(this)) != ABSENT_VALUE) {	// if (isDone()) {
			return result;									// return super.get(); }
		}
		if (timeout <= 0L) {
			throw new TimeoutException();
		}
		return factory.awaitUntil(this, timeout, unit);
	}

}
