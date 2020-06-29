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

import space.arim.api.env.annote.PlatformPlugin;
import space.arim.api.env.annote.PlatformServer;

/**
 * Holder for a platform-specific plugin object and server.
 * 
 * @author A248
 *
 */
public class PlatformPluginInfo {

	@PlatformPlugin
	private final Object plugin;
	@PlatformServer
	private final Object server;
	
	/**
	 * Creates from a plugin object
	 * 
	 * @param plugin the platform-specific plugin object
	 * @param server the platform-specific server object
	 */
	public PlatformPluginInfo(@PlatformPlugin Object plugin, @PlatformServer Object server) {
		this.plugin = plugin;
		this.server = server;
	}
	
	@PlatformPlugin
	public Object getPlugin() {
		return plugin;
	}
	
	@PlatformServer
	public Object getServer() {
		return server;
	}
	
}
