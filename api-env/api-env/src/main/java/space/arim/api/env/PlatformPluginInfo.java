/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

import space.arim.api.env.annote.PlatformPlugin;
import space.arim.api.env.annote.PlatformServer;

import java.util.Objects;

/**
 * Holder for a platform-specific plugin object and server.
 * 
 * @author A248
 *
 */
public final class PlatformPluginInfo {

	@PlatformPlugin
	private final Object plugin;
	@PlatformServer
	private final Object server;
	
	/**
	 * Creates from a plugin and server object
	 * 
	 * @param plugin the platform-specific plugin object
	 * @param server the platform-specific server object
	 */
	public PlatformPluginInfo(@PlatformPlugin Object plugin, @PlatformServer Object server) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
		this.server = Objects.requireNonNull(server, "server");
	}
	
	@PlatformPlugin
	public Object getPlugin() {
		return plugin;
	}
	
	@PlatformServer
	public Object getServer() {
		return server;
	}

	/**
	 * Considered equal to another based on the identity of the plugin and server
	 *
	 * @param o the other object
	 * @return true if equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlatformPluginInfo that = (PlatformPluginInfo) o;
		return plugin == that.plugin && server == that.server;
	}

	@Override
	public int hashCode() {
		int result = System.identityHashCode(plugin);
		result = 31 * result + System.identityHashCode(server);
		return result;
	}

	@Override
	public String toString() {
		return "PlatformPluginInfo{" +
				"plugin=" + plugin +
				", server=" + server +
				'}';
	}
}
