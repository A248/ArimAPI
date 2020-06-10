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

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;

import space.arim.api.concurrent.AsyncExecution;
import space.arim.api.platform.spigot.DefaultAsyncExecution;
import space.arim.api.platform.spigot.DefaultUUIDResolution;
import space.arim.api.uuid.UUIDResolution;

/**
 * Previous main plugin class on Spigot. <br>
 * This class contains some static platform-specific methods, which are deprecated for their own reasons.
 * 
 * @author A248
 *
 * @deprecated Depending on ArimAPI-plugin directly (this artifact) is deprecated. All API and utilities
 * are found in the other module artifacts.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class ArimApiPluginSpigot {
	
	private static JavaPlugin getPlugin() {
		return (JavaPlugin) Bukkit.getPluginManager().getPlugin("ArimAPI");
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
		registry.registerIfAbsent(UUIDResolution.class, () -> new Registration<UUIDResolution>(RegistryPriority.LOWEST, new DefaultUUIDResolution(getPlugin()), "Default UUIDResolution Implementation"));
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
		registry.registerIfAbsent(AsyncExecution.class, () -> new Registration<AsyncExecution>(RegistryPriority.LOWEST, new DefaultAsyncExecution(getPlugin()), "Default AsyncExecution Implementation"));
	}
	
}
