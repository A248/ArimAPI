/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.initializer;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import space.arim.universal.events.UniversalEvents;
import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.PlatformPluginInfo;
import space.arim.api.env.DetectingPlatformHandle;

/**
 * Initializer of the {@link Registry} on Bukkit servers, using the Bukkit ServicesManager. <br>
 * This class is only useful to plugins requiring ArimAPI which manage their dependencies by
 * downloading them at runtime. <br>
 * <br>
 * In practice, this is used by the ArimAPI plugin itself as well as plugins downloading ArimAPI at runtime.
 * 
 * @author A248
 *
 */
public class SpigotPlatformInitializer extends PlatformInitializer {

	private final JavaPlugin plugin;
	
	/**
	 * Creates from a JavaPlugin to use. The plugin must be enabled
	 * 
	 * @param plugin the plugin to use
	 */
	public SpigotPlatformInitializer(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Initialises the {@link Registry} using the Bukkit ServicesManager if it has not yet been
	 * initialised. <br>
	 * This also adds a registration for {@link PlatformPluginInfo} if not present, which is required by
	 * {@link DetectingPlatformHandle}
	 * 
	 * @return the initialised registry, never {@code null}
	 */
	@Override
	public Registry initRegistry() {
		return super.initRegistry();
	}
	
	@Override
	Registration<PlatformPluginInfo> getPluginInfo() {
		return new Registration<>(DEFAULT_PRIORITY, new PlatformPluginInfo(plugin, plugin.getServer()),
				plugin.getName());
	}
	
	/*
	 * The Bukkit ServicesManager does not have a #registerIfAbsent, so we synchronise on it
	 * 
	 */
	@Override
	Registry getRegistry() {
		Registry registry;
		ServicesManager bukkitServices = plugin.getServer().getServicesManager();
		synchronized (bukkitServices) {

			ServicePriority ownPriority;
			RegisteredServiceProvider<Registry> rsp = bukkitServices.getRegistration(Registry.class);
			if (rsp == null) {
				ownPriority = ServicePriority.Lowest;

			} else {
				Registry provided = rsp.getProvider();
				if (provided != null) {
					return provided;
				} else {
					warn("Another plugin registered a NULL provider for Registry: "
							+ rsp.getPlugin().getDescription().getFullName());

					ServicePriority higherPriority = getHigherPriority(rsp.getPriority());
					if (higherPriority == null) {
						/*
						 * Although the SimpleServicesManager implementation suggests our registration will be prioritised
						 * for registrations with the same priority, there are no such guarantees.
						 */
						warn("It may be impossible to recover from said null provider since it has the highest priority!");
						ownPriority = ServicePriority.Highest;
					} else {
						ownPriority = higherPriority;
					}
				}
			}
			registry = new UniversalRegistry(new UniversalEvents());
			bukkitServices.register(Registry.class, registry, plugin, ownPriority);
		}
		RegisteredServiceProvider<Registry> result = bukkitServices.getRegistration(Registry.class);
		if (result.getProvider() != registry) {
			error("Bukkit ServicesManager is tracking the wrong Registry; dependent plugins may fail!");
		}
		return registry;
	}
	
	/**
	 * Returns a priority higher than the specified priority, or {@code null}
	 * if the specified priority has no greater priority
	 * 
	 * @param initial
	 * @return a priority higher than {@code initial}, or {@code null} if there is none
	 */
	private static ServicePriority getHigherPriority(ServicePriority initial) {
		switch (initial) {
		case Lowest:
			return ServicePriority.Low;
		case Low:
			return ServicePriority.Normal;
		case Normal:
			return ServicePriority.High;
		case High:
			return ServicePriority.Highest;
		case Highest:
		default:
			return null;
		}
	}

	@Override
	void warn(String message) {
		plugin.getLogger().warning(message);
	}

	@Override
	void error(String message) {
		plugin.getLogger().severe(message);
	}
	
}
