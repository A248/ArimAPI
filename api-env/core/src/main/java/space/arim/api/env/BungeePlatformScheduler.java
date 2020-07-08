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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

class BungeePlatformScheduler extends DelegatingPlatformScheduler {
	
	BungeePlatformScheduler(Plugin plugin) {
		super(new BungeeSESWrapper(plugin));
	}
	
	/*
	 * 
	 * BungeeCord's current scheduler implementation is quite wasteful. It creates
	 * a new thread for each scheduled task, and sleeps that thread until the task
	 * is ready to execute.
	 * 
	 * Since Java threads correspond precisely to OS threads, such could be devastating
	 * if too many scheduled tasks were created. Accordingly, it is far more efficient
	 * to use an own ScheduledExecutorService, using 1 scheduling thread, which then
	 * hands tasks to the BungeeCord scheduler as they reach their time.
	 * 
	 */
	
	private static class BungeeSESWrapper extends ScheduledThreadPoolExecutor {
		
		private final Plugin plugin;
		private final TaskScheduler scheduler;
		
		BungeeSESWrapper(Plugin plugin) {
			super(1);
			this.plugin = plugin;
			scheduler = plugin.getProxy().getScheduler();
		}
		
		/*
		 * 
		 * DelegatingPlatformScheduler doesn't use any other methods,
		 * so it would be unnecessary to override them.
		 * 
		 */
		
		@Override
		public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
			return super.schedule(() -> scheduler.runAsync(plugin, command), delay, unit);
		}

		@Override
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
				TimeUnit unit) {
			return super.scheduleWithFixedDelay(() -> scheduler.runAsync(plugin, command), initialDelay, delay, unit);
		}
		
	}

}
