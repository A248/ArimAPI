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
package space.arim.api.plugin.bungee;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.Synchroniser;
import space.arim.api.concurrent.Task;

public class DefaultSynchroniser extends BungeeRegistrable implements Synchroniser {

	public DefaultSynchroniser(Plugin plugin) {
		super(plugin);
	}

	@Override
	public Task runTaskLater(Runnable command, long delay) {
		return new TaskWrapper(getPlugin().getProxy().getScheduler().schedule(getPlugin(), command, delay, TimeUnit.MILLISECONDS));
	}
	
	@Override
	public Task runTaskTimerLater(Runnable command, long delay, long period) {
		return new TaskWrapper(getPlugin().getProxy().getScheduler().schedule(getPlugin(), command, delay, period, TimeUnit.MILLISECONDS));
	}

	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}
	
}

class TaskWrapper implements Task {
	
	private final ScheduledTask task;
	
	TaskWrapper(ScheduledTask task) {
		this.task = task;
	}
	
	@Override
	public void cancel() {
		task.cancel();
	}
	
}
