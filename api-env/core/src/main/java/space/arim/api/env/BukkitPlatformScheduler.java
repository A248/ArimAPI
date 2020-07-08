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

import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

class BukkitPlatformScheduler implements PlatformScheduler {
	
	private final JavaPlugin plugin;
	private final BukkitScheduler scheduler;
	
	BukkitPlatformScheduler(JavaPlugin plugin) {
		this.plugin = plugin;
		scheduler = plugin.getServer().getScheduler();
	}
 
	private static class ScheduledTaskBukkit implements ScheduledTask {
		private volatile boolean cancelled = false;
		private final BukkitTask bukkitTask;
		ScheduledTaskBukkit(BukkitTask bukkitTask) {
			this.bukkitTask = bukkitTask;
		}
		@Override
		public void cancel() {
			cancelled = true;
			bukkitTask.cancel();
		}
		@Override
		public boolean isCancelled() {
			return cancelled;
		}
		@Override
		public String toString() {
			return "ScheduledTaskBukkit [cancelled=" + cancelled + ", bukkitTask=" + bukkitTask + "]";
		}
	}
	
	private static long toTicks(long time, TimeUnit unit) {
		switch (unit) {
		case NANOSECONDS:
		case MICROSECONDS:
		case MILLISECONDS:
			long millis = unit.toMillis(time);
			return millis / 50L;
		case DAYS:
		case HOURS:
		case MINUTES:
		case SECONDS:
		default:
			long seconds = unit.toSeconds(time);
			if (seconds > Long.MAX_VALUE / 20L) {
				// prevent overflow
				return Long.MAX_VALUE;
			}
			return seconds * 20L;
		}
	}
	
	@Override
	public ScheduledTask runDelayedTask(Runnable command, long delay, TimeUnit unit) {
		return new ScheduledTaskBukkit(scheduler.runTaskLaterAsynchronously(plugin, command, toTicks(delay, unit)));
	}

	@Override
	public ScheduledTask runRepeatingTask(Runnable command, long initialDelay, long repeatingPeriod, TimeUnit unit) {
		return new ScheduledTaskBukkit(scheduler.runTaskTimerAsynchronously(plugin, command,
				toTicks(initialDelay, unit), toTicks(repeatingPeriod, unit)));
	}

}
