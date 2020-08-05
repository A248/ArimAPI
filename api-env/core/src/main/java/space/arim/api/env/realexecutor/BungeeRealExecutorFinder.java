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

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

/**
 * The real executor finder for BungeeCord. Luckily, Bungee is not plagued by this problem. <br>
 * The {@code findExecutor} methods should never fail. However, BungeeCord's implementation may not be the
 * most efficient, as it currently maintains a thread pool per plugin.
 * 
 * @author A248
 *
 */
public class BungeeRealExecutorFinder implements RealExecutorFinder {

	private final Plugin plugin;
	
	/**
	 * Creates from a plugin to use
	 * 
	 * @param plugin the plugin
	 */
	public BungeeRealExecutorFinder(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Executor findExecutor(Consumer<Exception> exceptionHandler) {
		TaskScheduler scheduler = plugin.getProxy().getScheduler();
		return (cmd) -> scheduler.runAsync(plugin, cmd);
	}
	
}
