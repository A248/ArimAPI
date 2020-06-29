/* 
 * ArimAPI-sponge-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-sponge-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-sponge-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-sponge-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import space.arim.universal.events.UniversalEvents;
import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.PlatformPluginInfo;

@Plugin(id = "${plugin.annotationId}", name = "${plugin.name}", version = "${plugin.version}", authors = {
		"${plugin.author}" }, description = "${plugin.description}", url = "${plugin.url}")
public class SpongePlugin {
	
	private final Game server;
	
	@Inject
	public SpongePlugin(Game server) {
		this.server = server;
	}
	
	@Listener
	public void initRegistry(@SuppressWarnings("unused") GameInitializationEvent evt) {
		Registry registry = new UniversalRegistry(new UniversalEvents());
		server.getServiceManager().setProvider(this, Registry.class, registry);
		registry.register(PlatformPluginInfo.class, RegistryPriority.LOWEST,
				new PlatformPluginInfo(server.getPluginManager().fromInstance(this).get(), server), "${plugin.name}");
	}
	
}
