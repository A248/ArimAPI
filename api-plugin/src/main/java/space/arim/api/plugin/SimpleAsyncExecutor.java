/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.AsyncExecutor;

public class SimpleAsyncExecutor implements AsyncExecutor {

	private final ExecutorService threadPool;
	
	public SimpleAsyncExecutor(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

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
	
}
