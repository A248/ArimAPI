/* 
 * ArimAPI-bungee-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-bungee-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-bungee-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-bungee-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.universal.registry.Registry;
import space.arim.universal.registry.RegistryPriority;
import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.env.PlatformPluginInfo;

public class BungeePlugin extends Plugin {

	@Override
	public void onLoad() {
		Registry registry = UniversalRegistry.get();
		registry.register(PlatformPluginInfo.class, RegistryPriority.LOWEST, new PlatformPluginInfo(this, getProxy()), getDescription().getName());
	}
	
}
