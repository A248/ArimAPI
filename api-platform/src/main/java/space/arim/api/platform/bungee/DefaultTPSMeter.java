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

import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.RequireRegistration;

import space.arim.api.concurrent.SyncExecution;
import space.arim.api.util.TPSMeter;

/**
 * A default implementation of {@link TPSMeter} on the Bukkit platform.
 * Currently, it returns 20 TPS unconditionally, since BungeeCord is multithreaded, so there is no such concept as "Ticks per second". <br>
 * However, this is provided for programs which require a {@link TPSMeter} registration to function properly.
 * 
 * @author A248
 *
 */
public class DefaultTPSMeter extends BungeeRegistrable implements TPSMeter {

	/**
	 * Creates the instance. See {@link BungeeRegistrable#BungeeRegistrable(Plugin)} for more information. <br>
	 * Since the current implementation simply returns 20 TPS regardless, the second parameter would not normally be required.
	 * However, it must be supplied should future changes to this implementation may use it to calculate TPS.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 * @param registry the {@link Registry} to use. It must have a registration for {@link SyncExecution} as specified by the annotation.
	 */
	public DefaultTPSMeter(Plugin plugin, @RequireRegistration(SyncExecution.class) Registry registry) {
		super(plugin);
	}
	
	@Override
	public double getTPS() {
		return 20D; 
	}

	@Override
	public byte getPriority() {
		return RegistryPriority.LOWEST;
	}

}
