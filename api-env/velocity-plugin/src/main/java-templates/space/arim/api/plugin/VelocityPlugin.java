/* 
 * ArimAPI-velocity-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-velocity-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-velocity-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-velocity-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import space.arim.api.env.convention.VelocityPlatformConvention;

@Plugin(id = "${plugin.annotationId}", name = "${plugin.name}", version = "${plugin.version}", authors = {
		"${plugin.author}" }, url = "${plugin.url}", description = "${plugin.description}")
public class VelocityPlugin {

	private final ProxyServer server;
	
	@Inject
	public VelocityPlugin(ProxyServer server) {
		this.server = server;
	}
	
	@Subscribe
	public void initRegistry(@SuppressWarnings("unused") ProxyInitializeEvent evt) {
		new VelocityPlatformConvention(server.getPluginManager().fromInstance(this).get(), server).getRegistry();
	}
	
}
