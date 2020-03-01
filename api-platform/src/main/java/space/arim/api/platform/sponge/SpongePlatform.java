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
package space.arim.api.platform.sponge;

import java.util.Objects;

import org.spongepowered.api.plugin.PluginContainer;

import space.arim.api.platform.PluginInformation;
import space.arim.api.util.LazySingleton;

/**
 * Sponge platform specific utilities. Use {@link #get()} to get the instance.
 * 
 * @author A248
 *
 */
public class SpongePlatform {

	private static final LazySingleton<SpongePlatform> INST = new LazySingleton<SpongePlatform>(SpongePlatform::new);
	
	protected SpongePlatform() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static SpongePlatform get() {
		return INST.get();
	}
	
	/**
	 * Gets the messages utility
	 * 
	 * @return the messages utility instance
	 */
	public SpongeMessages messages() {
		return SpongeMessages.get();
	}
	
	/**
	 * Gets the commands utility
	 * 
	 * @return the commans utility instance
	 */
	public SpongeCommands commands() {
		return SpongeCommands.get();
	}
	
	/**
	 * Gets platform independent plugin information for a plugin
	 * 
	 * @param plugin the sponge plugin
	 * @return plugin information
	 */
	public PluginInformation convertPluginInfo(PluginContainer plugin) {
		return new PluginInformation(plugin.getId(), Objects.requireNonNull(plugin.getVersion().orElse(null), "Version must not be null!"), plugin.getAuthors().toArray(new String[] {}), plugin.getUrl().orElse(null), plugin.getDescription().orElse(null));
	}
	
}
