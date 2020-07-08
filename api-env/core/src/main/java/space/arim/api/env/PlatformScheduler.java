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
package space.arim.api.env;

import java.util.concurrent.TimeUnit;

/**
 * An interface bridging platform-specific schedulers. <br>
 * <br>
 * If the underlying platform-specific scheduler provides a distinction between "sync" and "async"
 * tasks, implementations of this interface should use the async variant. <br>
 * <br>
 * Additionally, some implementations may not support accuracy in any arbitrary {@code TimeUnit} due to
 * limitations in the underlying scheduler. In these cases, durations are floored as necessary to estimate
 * the equivalent platform-specific unit. <br>
 * <br>
 * Moreover, not all platform-specific schedulers support {@link ScheduledTask#isCancelled()} natively
 * via their own "task" objects. In these cases, the wrapper implementing {@code ScheduledTask}, which
 * delegates to the platform-specific task object, will maintain its own thread safe tracker for whether
 * the task has been cancelled.
 * 
 * @author A248
 *
 */
public interface PlatformScheduler {
	
	/**
	 * Runs a delayed task according to the platform-specific scheduler. <br>
	 * <br>
	 * Exactly when the task runs is entirely dependent on the underlying scheduler. Some schedulers
	 * may not run tasks in real time but rather in variance with program performance.
	 * 
	 * @param command the runnable to execute later
	 * @param delay the delay
	 * @param unit the unit of the delay
	 * @return a scheduled task which may be cancelled per the underlying API
	 */
	ScheduledTask runDelayedTask(Runnable command, long delay, TimeUnit unit);
	
	/**
	 * Runs a task repeatedly according to the platform-specific scheduler. <br>
	 * <br>
	 * Exactly when the task runs is entirely dependent on the underlying scheduler. Some schedulers
	 * may not run tasks in real time but rather in variance with program performance. <br>
	 * <br>
	 * It is further unspecified whether the task runs at a fixed rate of execution or with a fixed delay
	 * between executions. Such wholly depends on the underlying platform-specific scheduler. Fixed delay
	 * scheduling will be preferred but not guaranteed.
	 * 
	 * @param command the runnable to execute repeatedly
	 * @param initialDelay the initial delay
	 * @param repeatingPeriod the repeating interval
	 * @param unit the unit of the delay and interval
	 * @return a scheduled task which may be cancelled per the underlying API
	 */
	ScheduledTask runRepeatingTask(Runnable command, long initialDelay, long repeatingPeriod, TimeUnit unit);
	
	/**
	 * An interface bridging platform-specific cancellable tasks
	 * 
	 * @author A248
	 *
	 */
	interface ScheduledTask {
		
		/**
		 * Cancels the task if it has not been cancelled already
		 * 
		 */
		void cancel();
		
		/**
		 * Checks whether this task has been cancelled. <br>
		 * <br>
		 * On some platforms which do not support this functionality directly,
		 * implementations may use their own cancellation flag triggered by
		 * {@link #cancel()}, in which case using platform-specific cancellation
		 * and bypassing this interface may cause issues.
		 * 
		 * @return true if cancelled, false otherwise
		 */
		boolean isCancelled();
		
	}
	
}
