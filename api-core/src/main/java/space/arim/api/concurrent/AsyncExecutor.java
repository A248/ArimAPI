/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import space.arim.universal.registry.Registrable;
import space.arim.universal.registry.RegistryPriority;

/**
 * A service designed to for multithreading. <br>
 * <br>
 * * AsyncExecutor is a {@link Registrable}, and thus requires {@link #getPriority()} <br>
 * * AsyncExecutor is an {@link Executor}, and thus requires {@link #execute(Runnable)} <br>
 * * AsyncExecutor contains default implementations for {@link #submit(Runnable)} and {@link #submit(Callable)}, which happen to be, but are not necessarily, identical to those in {@link AbstractExecutorService}. <br>
 * <br>
 * AsyncExecutor differs from {@link ExecutorService} in a few important ways. First, it is a Registrable.
 * Second, AsyncExecutor is stripped to mere execution; it is not necessarily a thread pool.
 * AsyncExecutor does not specify any of ExecutorService's methods relating to
 * shutting down, termination, or invoking other callers' submissionse.
 * In fact, the only required method related to execution of tasks is {@link #execute(Runnable)}.
 * 
 * @author A248
 *
 */
public interface AsyncExecutor extends Registrable, Executor {
	
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
	
	/**
	 * Gets a simple implementation from an ExecutorService
	 * 
	 * @param threadPool the {@link ExecutorService} to use for executions
	 * @return a basic AsyncExecutor implementation
	 */
	static AsyncExecutor fromThreadPool(ExecutorService threadPool) {
		return new AsyncExecutor() {
			
			@Override
			public void execute(Runnable command) {
				threadPool.execute(command);
			}
			
			@Override
			public Future<?> submit(Runnable command) {
				return threadPool.submit(command);
			}
			
			@Override
			public <T> Future<T> submit(Callable<T> task) {
				return threadPool.submit(task);
			}
			
			@Override
			public byte getPriority() {
				return RegistryPriority.LOWEST;
			}
		};
	}
	
}
