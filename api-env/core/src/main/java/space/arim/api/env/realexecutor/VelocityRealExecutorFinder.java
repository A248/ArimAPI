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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;

/**
 * The real executor finder for Velocity. Luckily, Velocity is not plagued by this problem. <br>
 * The {@code findExecutor} methods should never fail.
 * 
 * @author A248
 *
 */
public class VelocityRealExecutorFinder implements RealExecutorFinder {

	private final PluginContainer plugin;
	private final ProxyServer server;
	
	/**
	 * Creates from a plugin and server to use
	 * 
	 * @param plugin the plugin
	 * @param server the server
	 */
	public VelocityRealExecutorFinder(PluginContainer plugin, ProxyServer server) {
		this.plugin = plugin;
		this.server = server;
	}

	@Override
	public Executor findExecutor(Consumer<Exception> exceptionHandler) {
		Scheduler scheduler = server.getScheduler();
		return (cmd) -> scheduler.buildTask(plugin, cmd).schedule();
	}
	
}
