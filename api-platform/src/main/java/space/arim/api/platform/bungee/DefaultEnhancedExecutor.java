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
package space.arim.api.platform.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.impl.SelfSchedulingEnhancedExecutor;

/**
 * Default implementation of {@link EnhancedExecutor} on the bungee platform.
 * 
 * @author A248
 *
 */
public class DefaultEnhancedExecutor extends SelfSchedulingEnhancedExecutor {

	private final Plugin plugin;
	private final TaskScheduler scheduler;
	
	private DefaultEnhancedExecutor(Plugin plugin) {
		this.plugin = plugin;
		scheduler = plugin.getProxy().getScheduler();
	}
	
	/**
	 * Registers an instance with a {@link Registry} if there is no registration for {@link EnhancedExecutor}.
	 * 
	 * @param registry the registry in which to register
	 * @param plugin the plugin to use to construct the default instance, if necessary
	 * @return the value of {@link Registry#registerIfAbsent(Class, java.util.function.Supplier)}
	 */
	public static Registration<EnhancedExecutor> registerIfAbsent(Registry registry, Plugin plugin) {
		return registry.registerIfAbsent(EnhancedExecutor.class,
				() -> new Registration<>(RegistryPriority.LOWEST, new DefaultEnhancedExecutor(plugin), "DefaultEnhancedExecutor-Bungee"));
	}
	
	@Override
	public void execute(Runnable command) {
		scheduler.runAsync(plugin, command);
	}

}
