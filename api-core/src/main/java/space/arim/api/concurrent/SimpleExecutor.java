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

import java.util.concurrent.Executor;

/**
 * 
 * A simple implementation of {@link EnhancedExecutor}
 * 
 * @author A248
 *
 */
public class SimpleExecutor implements EnhancedExecutor {

	private Executor executor;
	
	/**
	 * Creates a SimpleExecutor from an existing executor. Essentially, using a SimpleExecutor in this manner adds functionality at no additional cost.
	 * 
	 * @param executor the underlying {@link Executor}
	 */
	public SimpleExecutor(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void execute(Runnable command) {
		executor.execute(command);
	}
	
}
