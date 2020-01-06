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
package space.arim.api.plugin.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.Synchroniser;
import space.arim.api.concurrent.Task;

public class DefaultSynchroniser extends BukkitRegistrable implements Synchroniser {
	
	public DefaultSynchroniser(JavaPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public Task runTaskLater(Runnable command, long delay) {
		return new TaskWrapper(getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), command, delay));
	}
	
	@Override
	public Task runTaskTimerLater(Runnable command, long delay, long period) {
		return new TaskWrapper(getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), command, delay, period));
	}
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}
	
}

class TaskWrapper implements Task {
	
	private final BukkitTask task;
	
	TaskWrapper(BukkitTask task) {
		this.task = task;
	}
	
	@Override
	public void cancel() {
		task.cancel();
	}
	
}
