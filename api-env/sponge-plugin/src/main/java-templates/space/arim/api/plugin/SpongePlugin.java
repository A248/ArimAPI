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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import space.arim.api.env.convention.SpongePlatformConvention;

@Plugin(id = "${plugin.annotationId}", name = "${plugin.name}", version = "${plugin.version}", authors = {
		"${plugin.author}" }, description = "${plugin.description}", url = "${plugin.url}")
public class SpongePlugin {
	
	@Listener
	public void initRegistry(@SuppressWarnings("unused") GameInitializationEvent evt) {
		new SpongePlatformConvention(Sponge.getPluginManager().fromInstance(this).get()).getRegistry();
	}
	
}
