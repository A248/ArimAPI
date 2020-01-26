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
package space.arim.api.plugin.sponge;

import org.spongepowered.api.scheduler.SpongeExecutorService.SpongeFuture;

import space.arim.api.concurrent.Task;

class TaskWrapper implements Task {
	
	private final SpongeFuture<?> task;
	
	TaskWrapper(SpongeFuture<?> task) {
		this.task = task;
	}
	
	@Override
	public void cancel() {
		task.cancel(false);
	}

	@Override
	public boolean isCancelled() {
		return task.isCancelled();
	}
	
}