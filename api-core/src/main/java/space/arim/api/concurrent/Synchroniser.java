/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import space.arim.universal.registry.Registrable;

/**
 * A synchronising service.
 * 
 * @author A248
 *
 */
public interface Synchroniser extends Registrable {
	
	/**
	 * Runs a task synchronously.
	 * 
	 * @param command the Runnable
	 * @return a cancellable task
	 */
	Task runTask(Runnable command);
	
	/**
	 * Submits a callable.
	 * 
	 * @param <T> the type of the callable
	 * @param task the callable itself
	 * @return the future
	 */
	default <T> Future<T> submitTask(Callable<T> task) {
        RunnableFuture<T> future = new FutureTask<T>(task);
        runTask(future);
        return future;
	}
	
	/**
	 * Runs a delayed task synchronously.
	 * 
	 * @param command the Runnable
	 * @param delay the delay
	 * @return a cancellable task
	 */
	Task runTaskLater(Runnable command, long delay);
	
	/**
	 * Runs a task timer synchronously. The task may be cancelled. <br>
	 * <br>
	 * Using a synchronised task timer differs in one important regard from using an async timer which submits sync tasks.
	 * The synchronised task timer's timing matches the timing (e.g., TPS) of the main thread.
	 * However, the async timer's timing is independently calculated. If the main thread is experiencing latency,
	 * the async timer may run more frequently than the sync timer.
	 * 
	 * @param command the Runnable
	 * @param period the timespan between executions
	 * @return a cancellable task
	 */
	default Task runTaskTimer(Runnable command, long period) {
		return runTaskTimerLater(command, 0L, period);
	}
	
	/**
	 * Same as {@link #runTaskTimer(Runnable, long)} but includes an initial delay.
	 * 
	 * @param command the Runnable
	 * @param period the timespan between executions
	 * @param delay the initial delay
	 * @return a cancellable task
	 */
	Task runTaskTimerLater(Runnable command, long delay, long period);
	
}
