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

import java.util.function.Supplier;

import space.arim.omnibus.resourcer.ResourceHook;
import space.arim.omnibus.resourcer.ResourceInfo;
import space.arim.omnibus.resourcer.Resourcer;

import space.arim.api.env.annote.PlatformPlugin;
import space.arim.api.env.annote.PlatformServer;

abstract class AbstractPlatformHandle implements PlatformHandle {

	private final PlatformType type;
	private final PlatformPluginInfo pluginInfo;
	
	AbstractPlatformHandle(PlatformType type, @PlatformPlugin Object plugin, @PlatformServer Object server) {
		this.type = type;
		this.pluginInfo = new PlatformPluginInfo(plugin, server);
	}
	
	@Override
	public <T> ResourceHook<T> hookPlatformResource(Resourcer resourcer, Class<T> resourceClass) {
		Supplier<ResourceInfo<T>> defaultImplProvider = getResourceDefaultImplProvider(resourceClass);
		if (defaultImplProvider == null) {
			throw new IllegalArgumentException("Resource type " + resourceClass + " not supported with a default implementation");
		}
		return resourcer.hookUsage(resourceClass, defaultImplProvider);
	}
	
	abstract <T> Supplier<ResourceInfo<T>> getResourceDefaultImplProvider(Class<T> service);

	@SuppressWarnings("deprecation")
	@Override
	public PlatformType getPlatformType() {
		return type;
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return pluginInfo;
	}

}
