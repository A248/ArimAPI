/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform;

import space.arim.api.concurrent.SyncExecution;
import space.arim.api.util.TPSMeter;

/**
 * An implementation of {@link TPSMeter} which simply runs a synchronous task every tick. <br>
 * The ticks per second is calculated each tick based on the difference between the current and previous run.
 * 
 * @author A248
 *
 */
public abstract class AbstractTPSMeter extends PlatformRegistrable implements TPSMeter {
	
	private volatile long last = System.currentTimeMillis();
	private volatile double tps = 20D;
	
	/**
	 * Creates an AbstractTPSMeter based on the given {@link PluginInformation} and {@link SyncExecution}
	 * 
	 * @param information the plugin information
	 * @param sync the synchronous executor
	 */
	public AbstractTPSMeter(PluginInformation information, SyncExecution sync) {
		super(information);
		start(sync);
	}
	
	private void start(SyncExecution sync) {
		sync.runTaskTimer(() -> {
			long current = System.currentTimeMillis();
			tps = 1000D/(current - last);
			last = current;
		}, 50L);
	}
	
	@Override
	public double getTPS() {
		return tps;
	}
	
}
