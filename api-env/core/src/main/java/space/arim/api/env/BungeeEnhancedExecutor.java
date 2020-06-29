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
package space.arim.api.env;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import space.arim.universal.util.concurrent.impl.SelfSchedulingEnhancedExecutor;

class BungeeEnhancedExecutor extends SelfSchedulingEnhancedExecutor {

	private final Plugin plugin;
	private final TaskScheduler scheduler;
	
	BungeeEnhancedExecutor(Plugin plugin) {
		this.plugin = plugin;
		scheduler = plugin.getProxy().getScheduler();
	}
	
	@Override
	public void execute(Runnable command) {
		scheduler.runAsync(plugin, command);
	}

}
