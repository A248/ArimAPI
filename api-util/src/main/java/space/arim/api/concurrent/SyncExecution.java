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

import space.arim.universal.util.concurrent.EnhancedExecutor;

/**
 * Effectively a clone of {@link AsyncExecution} but with modified contract specifications. <br>
 * <br>
 * SyncExecution, obviously is supposed to run tasks synchronously.
 * 
 * @author A248
 *
 */
public interface SyncExecution extends EnhancedExecutor {

	/**
	 * Runs a <code>Runnable</code> command synchronously. <br>
	 * <br>
	 * The command MUST run on the main thread if the application has a main thread.
	 * 
	 */
	@Override
	void execute(Runnable command);
	
}
