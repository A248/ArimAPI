/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.concurrent;

/**
 * Internal class used for default implementations of {@link Scheduler} methods which accept a {@link Consumer} as the task parameter.
 * This object is passed to the Consumer parameters and then its {@link #cancel()}
 * 
 * @author A248
 *
 */
class PreTask implements Task {
	
	private Task value;
	
	void setValue(Task value) {
		this.value = value;
	}
	
	/**
	 * Cancels the PreTask. <br>
	 * <br>
	 * There are 2 possibilities: <br>
	 * * The underlying task has been created and passed to the PreTask, in which case its cancel method is called.
	 * * The underlying task has not been passed. In this rare yet unavoidable race condition, the underlying task is not cancelled.
	 * In most cases, this should not be a problem, since timed tasks won't be cancelling themselves ASAP.
	 * 
	 */
	@Override
	public void cancel() {
		if (value != null) {
			value.cancel();
		}
	}
	
}
