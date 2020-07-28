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

import java.util.concurrent.Executor;

import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.impl.SimplifiedEnhancedExecutor;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * An implementation of {@link EnhancedExecutor} for the Sponge platform, using a specified
 * plugin to execute tasks via the platform's common thread pool.
 * 
 * @author A248
 *
 */
public class SpongeEnhancedExecutor extends SimplifiedEnhancedExecutor {

	private final Executor delegate;
	
	/**
	 * Creates from a {@code PluginContainer} to use for execution
	 * 
	 * @param plugin the plugin to use
	 */
	public SpongeEnhancedExecutor(PluginContainer plugin) {
		delegate = Sponge.getScheduler().createAsyncExecutor(plugin);
	}
	
	@Override
	public void execute(Runnable command) {
		delegate.execute(command);
	}

}
