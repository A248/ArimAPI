/* 
 * ArimAPI-plugin
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.plugin;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.AsyncExecution;
import space.arim.api.platform.bungee.DefaultAsyncExecution;
import space.arim.api.platform.bungee.DefaultUUIDResolution;
import space.arim.api.uuid.UUIDResolution;

@SuppressWarnings("deprecation")
public class ArimApiPluginBungee extends Plugin {
	
	private static Plugin inst;
	
	public ArimApiPluginBungee() {
		inst = this;
	}
	
	/**
	 * Registers {@link DefaultUUIDResolution} if no registration for
	 * {@link UUIDResolution} exists.
	 * 
	 * @param registry the registry to use
	 * 
	 * @deprecated The {@link UUIDResolution} interface is itself deprecated
	 */
	@Deprecated
	public static void registerDefaultUUIDResolutionIfAbsent(Registry registry) {
		registry.registerIfAbsent(UUIDResolution.class, () -> new Registration<UUIDResolution>(RegistryPriority.LOWEST, new DefaultUUIDResolution(inst), "Default UUIDResolution Implementation"));
	}
	
	/**
	 * Registers {@link DefaultAsyncExecution} if no registration for
	 * {@link AsyncExecution} exists.
	 * 
	 * @param registry the registry to use
	 * 
	 * @deprecated The {@link AsyncExecution} interface is itself deprecated
	 */
	@Deprecated
	public static void registerDefaultAsyncExecutionIfAbsent(Registry registry) {
		registry.registerIfAbsent(AsyncExecution.class, () -> new Registration<AsyncExecution>(RegistryPriority.LOWEST, new DefaultAsyncExecution(inst), "Default AsyncExecution Implementation"));
	}
	
}
