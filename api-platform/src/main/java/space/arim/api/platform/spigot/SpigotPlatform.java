/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform.spigot;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import space.arim.api.platform.PluginInformation;
import space.arim.api.util.LazySingleton;

/**
 * Spigot platform specific utilities. Use {@link #get()} to get the instance.
 * 
 * @author A248
 *
 */
public class SpigotPlatform {

	private static final LazySingleton<SpigotPlatform> INST = new LazySingleton<SpigotPlatform>(SpigotPlatform::new);
	
	protected SpigotPlatform() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static SpigotPlatform get() {
		return INST.get();
	}
	
	/**
	 * Gets the messages utility
	 * 
	 * @return the messages utility instance
	 */
	public SpigotMessages messages() {
		return SpigotMessages.get();
	}
	
	/**
	 * Gets the commands utility
	 * 
	 * @return the commans utility instance
	 */
	public SpigotCommands commands() {
		return SpigotCommands.get();
	}
	
	/**
	 * Gets platform independent plugin information for the given <code>PluginDescriptionFile</code>
	 * 
	 * @param description the spigot plugin description
	 * @return plugin information
	 */
	public PluginInformation convertPluginInfo(PluginDescriptionFile description) {
		return new PluginInformation(description.getName(), description.getVersion(), description.getAuthors().toArray(new String[] {}), description.getWebsite(), description.getDescription());
	}
	
	/**
	 * Gets platform independent plugin information for a plugin
	 * 
	 * @param plugin the spigot plugin
	 * @return plugin information
	 */
	public PluginInformation convertPluginInfo(Plugin plugin) {
		return convertPluginInfo(plugin.getDescription());
	}
	
}
