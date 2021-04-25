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
package space.arim.api.env.concurrent;

import space.arim.api.env.velocity.VelocityPlatformHandle;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.impl.SimplifiedEnhancedExecutor;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;

/**
 * An implementation of {@link EnhancedExecutor} for the Velocity platform, using a specified
 * plugin to execute tasks via the platform's common thread pool.
 * 
 * @author A248
 *
 * @deprecated Use {@link VelocityPlatformHandle} and {@code createEnhancedExecutor} instead.
 * Relying on the identity of this implementation is not supported.
 */
@Deprecated
public class VelocityEnhancedExecutor extends SimplifiedEnhancedExecutor {
	
	private final PluginContainer plugin;
	private final Scheduler scheduler;
	
	/**
	 * Creates from a {@code PluginContainer} and {@code ProxyServer} to use for execution
	 * 
	 * @param plugin the plugin to use
	 * @param server the server to use
	 */
	public VelocityEnhancedExecutor(PluginContainer plugin, ProxyServer server) {
		this.plugin = plugin;
		scheduler = server.getScheduler();
	}

	@Override
	public void execute(Runnable command) {
		scheduler.buildTask(plugin, command).schedule();
	}
	
}
