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
package space.arim.api.server.bukkit;

import org.bukkit.plugin.Plugin;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.SyncExecution;
import space.arim.api.concurrent.Task;

/**
 * A default implementation of {@link SyncExecution} on the Bukkit platform. Uses the server's inbuilt scheduling.
 * 
 * @author A248
 *
 */
public class DefaultSyncExecution extends BukkitRegistrable implements SyncExecution {
	
	/**
	 * Creates the instance. See {@link BukkitRegistrable#BukkitRegistrable(Plugin)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 */
	public DefaultSyncExecution(Plugin plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(Runnable command) {
		getPlugin().getServer().getScheduler().runTask(getPlugin(), command);
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
