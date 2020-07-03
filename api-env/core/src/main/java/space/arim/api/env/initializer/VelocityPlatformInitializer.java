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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;

import space.arim.universal.registry.Registration;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.DetectingPlatformHandle;
import space.arim.api.env.PlatformPluginInfo;

/**
 * Initializer of the {@link Registry} on Velocity servers. <br>
 * This class is only useful to plugins requiring ArimAPI which manage their dependencies by
 * downloading them at runtime. <br>
 * <br>
 * In practice, this is used by the ArimAPI plugin itself as well as plugins downloading ArimAPI at runtime.
 * 
 * @author A248
 *
 */
public class VelocityPlatformInitializer extends DefaultPlatformInitializer {

	private final PluginContainer plugin;
	private final ProxyServer server;
	
	/**
	 * Creates from a PluginContainer and ProxyServer to use. The plugin's instance must exist
	 * 
	 * @param plugin the plugin to use
	 * @param server the server to use
	 */
	public VelocityPlatformInitializer(PluginContainer plugin, ProxyServer server) {
		this.plugin = plugin;
		this.server = server;
	}
	
	/**
	 * Initialises the {@link Registry}. <br>
	 * This also adds a registration for {@link PlatformPluginInfo} if not present, which is required by
	 * {@link DetectingPlatformHandle} <br>
	 * <br>
	 * For Velocity, this is currently implemented as {@link UniversalRegistry#get()}, however,
	 * this implementation may change if or once Velocity gets a services manager.
	 * 
	 * @return the initialised registry, never {@code null}
	 */
	@Override
	public Registry initRegistry() {
		return super.initRegistry();
	}
	
	@Override
	Registration<PlatformPluginInfo> getPluginInfo() {
		PluginDescription description = plugin.getDescription();
		return new Registration<>(DEFAULT_PRIORITY, new PlatformPluginInfo(plugin, server),
				description.getName().orElse(description.getId()));
	}
	
}
