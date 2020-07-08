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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import com.velocitypowered.api.scheduler.TaskStatus;

class VelocityPlatformScheduler implements PlatformScheduler {

	private final Object plugin;
	private final Scheduler scheduler;
	
	VelocityPlatformScheduler(PluginContainer plugin, ProxyServer server) {
		this.plugin = plugin.getInstance().get();
		scheduler = server.getScheduler();
	}
	
	private static class ScheduledTaskVelocity implements ScheduledTask {
		private final com.velocitypowered.api.scheduler.ScheduledTask velocityTask;
		ScheduledTaskVelocity(com.velocitypowered.api.scheduler.ScheduledTask velocityTask) {
			this.velocityTask = velocityTask;
		}
		@Override
		public void cancel() {
			velocityTask.cancel();
		}
		@Override
		public boolean isCancelled() {
			return velocityTask.status() == TaskStatus.CANCELLED;
		}
		@Override
		public String toString() {
			return "ScheduledTaskVelocity [velocityTask=" + velocityTask + "]";
		}
	}
	
	@Override
	public ScheduledTask runDelayedTask(Runnable command, long delay, TimeUnit unit) {
		return new ScheduledTaskVelocity(scheduler.buildTask(plugin, command).delay(delay, unit).schedule());
	}

	@Override
	public ScheduledTask runRepeatingTask(Runnable command, long initialDelay, long repeatingPeriod, TimeUnit unit) {
		return new ScheduledTaskVelocity(
				scheduler.buildTask(unit, command).delay(initialDelay, unit).repeat(repeatingPeriod, unit).schedule());
	}

}
