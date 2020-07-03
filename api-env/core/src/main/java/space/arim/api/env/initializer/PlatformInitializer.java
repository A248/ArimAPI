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

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.PlatformPluginInfo;

/**
 * Platform-specific {@link Registry} initializer whose subclasses are intended to be used
 * by either the ArimAPI plugin itself, or plugins depending on ArimAPI which download their
 * dependencies at runtime.
 * 
 * @author A248
 *
 */
public abstract class PlatformInitializer {

	static final byte DEFAULT_PRIORITY = RegistryPriority.LOWEST;
	
	/**
	 * Initialises a {@link Registry} for the platform. <br>
	 * <br>
	 * If there is platform-specific services manager, the {@code Registry} will be drawn from that if there is
	 * a platform-specific registration, or registered with the platform-specific services manager and returned. <br>
	 * <br>
	 * Otherwise, if there is no platform-specific services manager, {@link UniversalRegistry#get()} is used.
	 * 
	 * @return the initialised registry, never {@code null}
	 */
	public Registry initRegistry() {
		Registry registry = getRegistry();
		registry.registerIfAbsent(PlatformPluginInfo.class, this::getPluginInfo);
		return registry;
	}
	
	abstract Registry getRegistry();
	
	abstract Registration<PlatformPluginInfo> getPluginInfo();
	
	abstract void warn(String message);
	
	abstract void error(String message);
	
}
