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
package space.arim.api.env;

import java.util.Objects;
import java.util.function.Supplier;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;

import space.arim.api.env.annote.PlatformPlugin;
import space.arim.api.env.annote.PlatformServer;

abstract class AbstractPlatformHandle implements PlatformHandle {

	private final PlatformType type;
	final Registry registry;
	final PlatformPluginInfo pluginInfo;
	
	static final byte DEFAULT_PRIORITY = RegistryPriority.LOWEST;
	
	AbstractPlatformHandle(PlatformType type, Registry registry, @PlatformPlugin Object plugin, @PlatformServer Object server) {
		this(type, registry, new PlatformPluginInfo(plugin, server));
	}
	
	private AbstractPlatformHandle(PlatformType type, Registry registry, PlatformPluginInfo pluginInfo) {
		this.type = type;
		this.registry = registry;
		this.pluginInfo = pluginInfo;
	}
	
	AbstractPlatformHandle(PlatformType type, Registry registry) {
		this(type, registry, Objects.requireNonNull(registry.getProvider(PlatformPluginInfo.class)));
	}
	
	@Override
	public <T> T registerDefaultServiceIfAbsent(Class<T> service) {
		Supplier<Registration<T>> supplier = getDefaultServiceSupplier(service);
		if (supplier == null) {
			throw new IllegalArgumentException("Service not supported with default implementation");
		}
		return registry.registerIfAbsent(service, supplier).getProvider();
	}
	
	abstract <T> Supplier<Registration<T>> getDefaultServiceSupplier(Class<T> service);

	@Override
	public PlatformType getPlatformType() {
		return type;
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return pluginInfo;
	}

}
