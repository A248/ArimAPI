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
package space.arim.api.platform.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.impl.SelfSchedulingEnhancedExecutor;

import space.arim.api.concurrent.AsyncExecution;

/**
 * A default implementation of {@link AsyncExecution} on the Sponge platform.
 * 
 * @author A248
 *
 * @deprecated {@link AsyncExecution} is itself deprecated. Please use {@link EnhancedExecutor}
 * and {@link DefaultEnhancedExecutor} as corresponding replacements.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DefaultAsyncExecution extends SelfSchedulingEnhancedExecutor implements AsyncExecution {

	private final SpongeExecutorService threadPool;
	
	/**
	 * Creates the instance
	 * 
	 * @param plugin the plugin to use
	 */
	public DefaultAsyncExecution(PluginContainer plugin) {
		threadPool = Sponge.getScheduler().createAsyncExecutor(plugin.getInstance().get());
	}

	@Override
	public void execute(Runnable command) {
		threadPool.execute(command);
	}
	
}
