/* 
 * ArimAPI-env-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.plugin;

class PluginVariables {

	static final String ANNOTE_ID = "${plugin.annotationId}";
	
	static final String NAME = "${plugin.name}";
	
	static final String VERSION = "${plugin.version}";
	
	static final String DESCRIPTION = "${plugin.description}";
	
	static final String URL = "${plugin.url}";
	
}
