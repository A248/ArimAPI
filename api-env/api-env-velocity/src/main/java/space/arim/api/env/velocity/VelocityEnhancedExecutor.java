/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
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

package space.arim.api.env.velocity;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.scheduler.Scheduler;
import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.impl.SimplifiedEnhancedExecutor;

/**
 * An implementation of {@link EnhancedExecutor} for the Velocity platform, using a specified
 * plugin to execute tasks via the platform's common thread pool.
 *
 */
final class VelocityEnhancedExecutor extends SimplifiedEnhancedExecutor {

	private final PluginContainer plugin;
	private final Scheduler scheduler;

	VelocityEnhancedExecutor(PluginContainer plugin, Scheduler scheduler) {
		this.plugin = plugin;
		this.scheduler = scheduler;
	}

	@Override
	public void execute(Runnable command) {
		scheduler.buildTask(plugin, command).schedule();
	}
	
}
