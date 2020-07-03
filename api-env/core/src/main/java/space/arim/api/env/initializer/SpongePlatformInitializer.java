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

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ServiceManager;

import space.arim.universal.events.UniversalEvents;
import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.DetectingPlatformHandle;
import space.arim.api.env.PlatformPluginInfo;

/**
 * Initializer of the {@link Registry} on Sponge servers, using the Sponge ServiceManager. <br>
 * This class is only useful to plugins requiring ArimAPI which manage their dependencies by
 * downloading them at runtime. <br>
 * <br>
 * In practice, this is used by the ArimAPI plugin itself as well as plugins downloading ArimAPI at runtime.
 * 
 * @author A248
 *
 */
public class SpongePlatformInitializer extends PlatformInitializer {

	private final PluginContainer plugin;
	private final Logger logger;
	
	/**
	 * Creates from a PluginContainer to use. The plugin's instance must exist
	 * 
	 * @param plugin the plugin to use
	 */
	public SpongePlatformInitializer(PluginContainer plugin) {
		this.plugin = plugin;
		logger = plugin.getLogger();
	}
	
	/**
	 * Initialises the {@link Registry} using the Sponge ServiceManager if it has not yet been
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
		return new Registration<>(DEFAULT_PRIORITY, new PlatformPluginInfo(plugin, Sponge.getGame()), plugin.getName());
	}
	
	/*
	 * The Sponge ServiceManager does not have a #registerIfAbsent, so we synchronise on it
	 * 
	 */
	@Override
	Registry getRegistry() {
		Registry registry;
		ServiceManager spongeServices = Sponge.getServiceManager();
		synchronized (spongeServices) {

			Registry provided = spongeServices.provide(Registry.class).orElse(null);
			if (provided == null) {
				registry = new UniversalRegistry(new UniversalEvents());
				spongeServices.setProvider(plugin.getInstance().get(), Registry.class, registry);

			} else {
				registry = provided;
			}
		}
		if (spongeServices.provideUnchecked(Registry.class) != registry) {
			error("Sponge ServiceManager is tracking the wrong Registry; dependent plugins may fail!");
		}
		return registry;
	}

	@Override
	void warn(String message) {
		logger.warn(message);
	}

	@Override
	void error(String message) {
		logger.error(message);
	}

}
