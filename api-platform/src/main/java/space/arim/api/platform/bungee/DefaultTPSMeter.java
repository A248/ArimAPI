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
package space.arim.api.platform.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.api.util.TPSMeter;

/**
 * A default implementation of {@link TPSMeter} on the BungeeCord platform. <br>
 * <br>
 * Although BungeeCord is multithreaded, this is provided for programs which require a {@link TPSMeter} registration. <br>
 * (It simply returns 20 TPS consistently)
 * 
 * @author A248
 *
 */
public class DefaultTPSMeter implements TPSMeter {
	
	public DefaultTPSMeter(Plugin plugin) {
		
	}
	
	@Override
	public double getTPS() {
		return 20D;
	}

}
