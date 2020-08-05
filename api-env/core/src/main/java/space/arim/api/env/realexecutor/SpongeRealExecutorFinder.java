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

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Scheduler;

/**
 * The real executor finder for Sponge servers. Relies on implementations in SpongeCommon to retrieve the common
 * thread pool. <br>
 * <br>
 * Like Paper servers with the async scheduler patch, Sponge servers have the real executor declared in a field
 * named "executor", which is itself in an object in the field "asyncScheduler" of the server's scheduler.
 * 
 * @author A248
 *
 */
public class SpongeRealExecutorFinder implements RealExecutorFinder {

	//private final PluginContainer plugin;
	
	/**
	 * Creates from a plugin to use
	 * 
	 * @param plugin the plugin
	 */
	public SpongeRealExecutorFinder(PluginContainer plugin) {
		//this.plugin = plugin;
	}

	@Override
	public Executor findExecutor(Consumer<Exception> exceptionHandler) {
		Scheduler scheduler = Sponge.getScheduler();
		return ExecutorReflector.getFieldNamedAsyncSchedulerThenGetFieldNamedExecutorFromThat(scheduler, exceptionHandler);
	}
	
}
