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
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * A {@link Executor} with upgraded concurrent functionality. Differs from {@link java.util.concurrent.ExecutorService} in that it contains
 * no methods for thread pool management (such as shutdown, await termination, etc.) but focuses specifically on concurrent execution. <br>
 * Specifications: <br>
 * * {@link #submit(Runnable)} <br>
 * * {@link #submit(Callable)} <br>
 * BasicExecutor provides default implementations for all of its specifications. <br>
 * <b>Thus, the only required method is the transitive {@link #execute(Runnable)}. </b><br>
 * 
 * @author A248
 *
 */
public interface BasicExecutor extends Executor {

	/**
	 * Execute an asynchronous action. <br>
	 * Differs from {@link #execute} in that the returned {@link Future} provides the ability to cancel the task before completion.
	 * 
	 * @param command the {@link java.lang.Runnable} to run
	 * @return a cancellable future which will return <code>null</code> on {@link Future#get()}
	 */
	default Future<?> submit(Runnable command) {
		RunnableFuture<Void> task = new FutureTask<Void>(command, null);
        execute(task);
        return task;
	}
	
	/**
	 * Submits a callable.
	 * 
	 * @param <T> the type of the callable
	 * @param task the callable itself
	 * @return a future
	 */
	default <T> Future<T> submit(Callable<T> task) {
        RunnableFuture<T> future = new FutureTask<T>(task);
        execute(future);
        return future;
	}
	
}
