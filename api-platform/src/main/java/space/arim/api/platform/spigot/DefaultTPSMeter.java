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
package space.arim.api.platform.spigot;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.plugin.Plugin;

import space.arim.universal.registry.RegistryPriority;

import space.arim.api.platform.spigot.nms.NMS;
import space.arim.api.util.TPSMeter;

/**
 * A default implementation of {@link TPSMeter} on the Spigot platform.
 * 
 * @author A248
 *
 */
public class DefaultTPSMeter extends SpigotRegistrable implements TPSMeter {
	
	/**
	 * Creates the instance. See {@link SpigotRegistrable#SpigotRegistrable(Plugin)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 */
	public DefaultTPSMeter(Plugin plugin) {
		super(plugin);
	}
	
	@Override
	public double getTPS() {
		try {
			return NMS.getTPS()[0];
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
			return 20D;
		}
	}
	
	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}

}
