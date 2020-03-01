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

import java.util.concurrent.ExecutorService;

import space.arim.universal.registry.Registrable;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.Scheduler;
import space.arim.universal.util.concurrent.Task;

/**
 * A service designed for multithreading via asynchronous concurrent execution and scheduling. <br>
 * <b>Effectively a clone of {@link AsyncExecution} but with modified contract specifications.</b> <br>
 * <br>
 * <b>Specifications:</b> <br>
 * * from {@link Registrable}: requires {@link #getPriority()} <br>
 * * from {@link EnhancedExecutor}: requires {@link #execute(Runnable)} <br>
 * * from {@link Scheduler}: requires {@link #runTaskLater(Runnable, long)} and {@link #runTaskTimerLater(Runnable, long, long)} <br>
 * <br>
 * <b>Contract:</b> <br>
 * * from Registrable: Implementations should be registered under SyncExecution. <br>
 * * from Scheduler: {@link Task#cancel()} method implementations should properly cancel further scheduling. Also, time units are in <i>milliseconds</i>.<br>
 * * SyncExecution: <b>All execution and scheduled tasks MUST run on the main thread</b> if the application has a main thread. <br>
 * <br>
 * SyncExecution differs from {@link ExecutorService} in a few important ways. First, it is a Registrable.
 * Second, SyncExecution is not necessarily a thread pool, but simply an executor and scheduler.
 * Thus it does not specify any methods relating to shutting down, awaiting termination, or invoking other callers' submissions.
 * Thirdly, SyncExecution enables scheduling, which simple thread pools do not.
 * 
 * @author A248
 *
 */
public interface SyncExecution extends Registrable, EnhancedExecutor, Scheduler {

}
