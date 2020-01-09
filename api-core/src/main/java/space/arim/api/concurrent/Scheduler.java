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

import java.util.function.Consumer;

/**
 * A scheduler designed for creating delayed tasks.
 * <br>
 * Requirements:
 * {@link #runTaskLater(Runnable, long)}
 * {@link #runTaskTimerLater(Runnable, long, long)}
 * <br>
 * Each method specified by Scheduler itself returns a {@link Task} to provide basic cancellation. <br>
 * <br>
 * All time units are in <b>MILLISECONDS</b> to provide compatibility with implementing platforms which do not support other units.
 * <br>
 * All methods accept either a basic {@link Runnable} or a {@link Consumer} as the task to schedule. <br>
 * The consumer may be used so that the executing task can cancel its own scheduling. <br>
 * <br>
 * {@link #runTaskLater(Runnable, long)} and {@link #runTaskTimerLater(Runnable, long, long)}
 * are required, while {@link #runTask(Runnable)} and {@link #runTaskTimer(Runnable, long)} have default implementations
 * which call the required methods, but may be overriden where the underlying Scheduler implementation supports it.
 * The methods accepting Consumer instead of Runnable parameters need never be overriden.
 * 
 * @author A248
 *
 */
public interface Scheduler {
	
	/**
	 * Runs a delayed task
	 * 
	 * @param command the execution
	 * @param delay the delay
	 * @return a cancellable task
	 */
	Task runTaskLater(Runnable command, long delay);
	
	/**
	 * Same as {@link #runTaskLater(Runnable, long)} but with the ability to access the task
	 * (and thus cancel further scheduling of it) from within the execution itself.
	 * 
	 * @param command the execution
	 * @param delay the delay
	 * @return a cancellable task
	 */
	default Task runTaskLater(Consumer<Task> command, long delay) {
		PreTask pre = new PreTask();
		Task result = runTaskLater(() -> command.accept(pre), delay);
		pre.setValue(result);
		return result;
	}
	
	/**
	 * Runs a task timer. The task may be cancelled.
	 * 
	 * @param command the execution
	 * @param period the timespan between executions
	 * @return a cancellable task
	 */
	default Task runTaskTimer(Runnable command, long period) {
		return runTaskTimerLater(command, 0L, period);
	}
	
	/**
	 * Same as {@link #runTaskTimer(Runnable, long)} but with the ability to access the task
	 * (and thus cancel further scheduling of it) from within the execution itself.
	 * 
	 * @param command the execution
	 * @param period the timespan between executions
	 * @return a cancellable task
	 */
	default Task runTaskTimer(Consumer<Task> command, long period) {
		PreTask pre = new PreTask();
		Task result = runTaskTimer(() -> command.accept(pre), period);
		pre.setValue(result);
		return result;
	}
	
	/**
	 * Same as {@link #runTaskTimer(Consumer, long)} but includes an initial delay.
	 * 
	 * @param command the execution
	 * @param period the timespan between executions
	 * @param delay the initial delay
	 * @return a cancellable task
	 */
	Task runTaskTimerLater(Runnable command, long delay, long period);
	
	/**
	 * Same as {@link #runTaskTimerLater(Runnable, long)} but with the ability to access the task
	 * (and thus cancel further scheduling of it) from within the execution itself.
	 * 
	 * @param command the execution
	 * @param period the timespan between executions
	 * @param delay the initial delay
	 * @return a cancellable task
	 */
	default Task runTaskTimerLater(Consumer<Task> command, long delay, long period) {
		PreTask pre = new PreTask();
		Task result = runTaskTimerLater(() -> command.accept(pre), delay, period);
		pre.setValue(result);
		return result;
	}
	
}
