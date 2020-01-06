/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin;

import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "arimapiplugin", name = "ArimAPIPlugin", version = "see_plugin_jar_resource=plugin.yml")
public class ArimApiPluginSponge {
	
	/*
	 * I am so glad there is no implementation required here.
	 * The Sponge API is just absolutely horrible.
	 * 
	 * So much of its access is disgustingly static. There are no plugin or server instances for use.
	 * Worse, the extreme over-use of annotations for basic functionality is just appalling
	 */
	
}
