/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.platform;

import space.arim.universal.registry.Registrable;

/**
 * Base class for platform specific registrations.
 * 
 * @author A248
 *
 */
public abstract class PlatformRegistrable implements Registrable {

	private final PluginInformation information;
	
	public PlatformRegistrable(PluginInformation information) {
		this.information = information;
	}
	
	/**
	 * Returns the value of <code>getPluginInformation().getName()</code>
	 * 
	 * @return the plugin name
	 */
	@Override
	public String getName() {
		return information.getName();
	}
	
	/**
	 * Returns the plugin's author as defined in plugin.yml, bungee.yml, or Sponge annotation. <br>
	 * If the author is not defined, returns the default implementation of {@link Registrable#getAuthor()}
	 * 
	 * @return the author's name
	 */
	@Override
	public String getAuthor() {
		return (information.getAuthors().length > 0) ? information.getAuthors()[0] : Registrable.super.getAuthor();
	}
	
	/**
	 * Returns the plugin's version as defined in plugin.yml, bungee.yml, or Sponge annotation
	 * 
	 * @return the version
	 */
	@Override
	public String getVersion() {
		return information.getVersion();
	}
	
	/**
	 * Gets the <code>PluginInformation</code> whose information is used
	 * 
	 * @return the plugin information used
	 */
	public final PluginInformation getPluginInformation() {
		return information;
	}

}
