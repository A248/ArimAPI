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

import space.arim.universal.util.concurrent.EnhancedExecutor;

/**
 * A service designed for multithreading via asynchronous concurrent execution and scheduling. <br>
 * <br>
 * <b>Contract and specifications:</b> see {@link #execute(Runnable)} <br>
 * <br>
 * AsyncExecution differs from {@link java.util.concurrent.ExecutorService ExecutorService}
 * in a few important ways.
 * First, AsyncExecution is not necessarily a thread pool, but simply an executor and scheduler.
 * Thus it does not specify any methods relating to shutting down, awaiting termination, or invoking other callers' submissions.
 * Second, AsyncExecution enables scheduling, which simple thread pools do not.
 * Third, AsyncExecution is designed to be implemented registered as a service in a
 * {@link space.arim.universal.registry.Registry Registry}.
 * 
 * @author A248
 *
 * @deprecated This interface is obsolete, because an {@link EnhancedExecutor} (which this interface extends) should <i>always</i>
 * run work asynchronously. Before UniversalUtil 0.10.0, in which breaking changes were made to {@code EnhancedExecutor} and its
 * specifications, this was not the case.
 */
@Deprecated
public interface AsyncExecution extends EnhancedExecutor {

	/**
	 * Runs a <code>Runnable</code> command asynchronously. <br>
	 * <br>
	 * The command MUST NOT run on the main thread if the application has a main thread.
	 * 
	 */
	@Override
	void execute(Runnable command);
	
}
