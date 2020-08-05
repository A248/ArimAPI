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
package space.arim.api.env.realexecutor;

import java.lang.reflect.InaccessibleObjectException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * The real executor finder for Bukkit servers. <br>
 * <br>
 * Assumes the implementation is {@code CraftBukkit} and attempts to reflectively access the CraftScheduler. On some servers,
 * the field is declared in {@code CraftScheduler} as "executor". On later Paper servers, containing a patch for
 * improved async scheduling, it is inside a {@code CraftAsyncScheduler}, also as "executor"; the {@code CraftAsyncScheduler}
 * is itself a field in {@code CraftScheduler} called "asyncScheduler".
 * 
 * @author A248
 *
 */
public class BukkitRealExecutorFinder implements RealExecutorFinder {
	
	private final JavaPlugin plugin;
	
	/**
	 * Creates from a plugin to use
	 * 
	 * @param plugin the plugin
	 */
	public BukkitRealExecutorFinder(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public Executor findExecutor(Consumer<Exception> exceptionHandler) {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		try {
			return ExecutorReflector.getFieldNamedExecutor0(scheduler);

		} catch (SecurityException | IllegalAccessException | InaccessibleObjectException | ClassCastException ex) {
			return ExecutorReflector.handle(exceptionHandler, ex);

		} catch (NoSuchFieldException improvedAsyncScheduler) {
			// Server has improved async scheduler patch, workaround below
		}
		return ExecutorReflector.getFieldNamedAsyncSchedulerThenGetFieldNamedExecutorFromThat(scheduler, exceptionHandler);
	}

}
