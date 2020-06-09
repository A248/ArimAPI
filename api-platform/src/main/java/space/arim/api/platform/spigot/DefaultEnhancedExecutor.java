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
package space.arim.api.platform.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.impl.SelfSchedulingEnhancedExecutor;

/**
 * Default implementation of {@link EnhancedExecutor} on the spigot platform.
 * 
 * @author A248
 *
 */
public class DefaultEnhancedExecutor extends SelfSchedulingEnhancedExecutor {

	private final JavaPlugin plugin;
	private final BukkitScheduler scheduler;
	
	private DefaultEnhancedExecutor(JavaPlugin plugin) {
		this.plugin = plugin;
		scheduler = plugin.getServer().getScheduler();
	}
	
	/**
	 * Registers an instance with a {@link Registry} if there is no registration for {@link EnhancedExecutor}.
	 * 
	 * @param registry the registry in which to register
	 * @param plugin the plugin to use to construct the default instance, if necessary
	 * @return the value of {@link Registry#registerIfAbsent(Class, java.util.function.Supplier)}
	 */
	public static Registration<EnhancedExecutor> registerIfAbsent(Registry registry, JavaPlugin plugin) {
		return registry.registerIfAbsent(EnhancedExecutor.class,
				() -> new Registration<>(RegistryPriority.LOWEST, new DefaultEnhancedExecutor(plugin), "DefaultEnhancedExecutor-Spigot"));
	}
	
	@Override
	public void execute(Runnable command) {
		scheduler.runTaskAsynchronously(plugin, command);
	}

}
