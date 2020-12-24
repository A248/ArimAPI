package space.arim.api.env.concurrent;

import space.arim.omnibus.util.concurrent.CentralisedFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static space.arim.api.env.concurrent.DeadlockFreeFuture.ABSENT_VALUE;
import static space.arim.api.env.concurrent.DeadlockFreeFuture.reportGet;
import static space.arim.api.env.concurrent.DeadlockFreeFuture.reportJoin;

/**
 * Implementation of {@code ManagedWaitStrategy} using a {@link Lock} + {@link Condition}.
 * May incur overhead due to locking and internal creation of futures. <br>
 * <br>
 * There are few cases where this strategy is optimal. It is most useful
 * when the main thread is blocked for longer periods of time.
 */
public final class BlockingManagedWaitStrategy implements ManagedWaitStrategy {

	private final ReentrantLock completionLock = new ReentrantLock();
	private final Condition completionCondition = completionLock.newCondition();

	@Override
	public boolean requireSignalWhenFutureCompleted() {
		return true;
	}

	@Override
	public void signalWhenFutureCompleted(Thread mainThread) {
		signal();
	}

	@Override
	public void signalWhenTaskAdded(Thread mainThread) {
		signal();
	}

	private void signal() {
		completionLock.lock();
		try {
			completionCondition.signal();
		} finally {
			completionLock.unlock();
		}
	}

	@Override
	public <T> T await(Runnable runQueuedTasks, CentralisedFuture<T> future) {
		for (;;) {
			runQueuedTasks.run();

			completionLock.lock();
			try {
				T result;
				if ((result = reportJoin(future)) != ABSENT_VALUE) {	// if (isDone()) {
					return result;										// return super.join(); }
				}
				completionCondition.awaitUninterruptibly();
			} finally {
				completionLock.unlock();
			}
		}
	}

	@Override
	public <T> T awaitInterruptibly(Runnable runQueuedTasks, CentralisedFuture<T> future)
			throws InterruptedException, ExecutionException {
		for (;;) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			runQueuedTasks.run();

			completionLock.lockInterruptibly();
			try {
				T result;
				if ((result = reportGet(future)) != ABSENT_VALUE) {	// if (isDone()) {
					return result;									// return super.get(); }
				}
				completionCondition.await();
			} finally {
				completionLock.unlock();
			}
			T result;
			if ((result = reportGet(future)) != ABSENT_VALUE) {	// if (isDone()) {
				return result;									// return super.get(); }
			}
		}
	}

	@Override
	public <T> T awaitUntil(Runnable runQueuedTasks, CentralisedFuture<T> future, long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException, ExecutionException {
		long deadline = System.nanoTime() + unit.toNanos(timeout);
		for (;;) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			runQueuedTasks.run();

			completionLock.lockInterruptibly();
			try {
				T result;
				if ((result = reportGet(future)) != ABSENT_VALUE) {	// if (isDone()) {
					return result;									// return super.get(); }
				}
				long waitFor = deadline - System.nanoTime();
				if (waitFor <= 0) {
					throw new TimeoutException();
				}
				completionCondition.awaitNanos(waitFor);
			} finally {
				completionLock.unlock();
			}
			T result;
			if ((result = reportGet(future)) != ABSENT_VALUE) {	// if (isDone()) {
				return result;									// return super.get(); }
			}
		}
	}

}
