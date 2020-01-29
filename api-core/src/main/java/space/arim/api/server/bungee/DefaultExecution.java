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
package space.arim.api.server.bungee;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.Task;
import space.arim.api.server.bungee.BungeeRegistrable;

class DefaultExecution extends BungeeRegistrable {
	
	DefaultExecution(Plugin plugin) {
		super(plugin);
	}
	
	public void execute(Runnable command) {
		getPlugin().getProxy().getScheduler().runAsync(getPlugin(), command);
	}
	
	public Task runTaskLater(Runnable command, long delay) {
		return new TaskWrapper(getPlugin().getProxy().getScheduler().schedule(getPlugin(), command, delay, TimeUnit.MILLISECONDS));
	}
	
	public Task runTaskTimerLater(Runnable command, long delay, long period) {
		return new TaskWrapper(getPlugin().getProxy().getScheduler().schedule(getPlugin(), command, delay, period, TimeUnit.MILLISECONDS));
	}
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}
	
}
