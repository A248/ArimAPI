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

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import space.arim.universal.util.collections.ArraysUtil;

/**
 * Generic plugin description information. <br>
 * <br>
 * A superconstruct for Bukkit's PluginDescriptionFile, Bungee's PluginDescription, and Sponge's PluginContainer.
 * 
 * @author A248
 *
 */
public final class PluginInformation {

	private final String name;
	private final String version;
	private final String[] authors;
	private final String url;
	private final String description;
	
	/**
	 * Directly instantiates with the given information. <br>
	 * <br>
	 * <b>Most programmers will prefer to use either the static factory methods or the platform specific plugin information converter</b>
	 * 
	 * @param name the plugin name
	 * @param version the plugin version
	 * @param authors all of the authors or contributors of the plugin
	 * @param url a website or link to the plugin page
	 * @param description a brief description of the plugin
	 */
	public PluginInformation(String name, String version, String[] authors, String url, String description) {
		this.name = name;
		this.version = version;
		this.authors = authors;
		this.url = url;
		this.description = description;
	}
	
	/**
	 * Creates a PluginInformation with all possible information supplied.
	 * 
	 * @param name the plugin name
	 * @param version the plugin version
	 * @param authors a list of authors
	 * @param url the project website or URL
	 * @param description a basic description
	 * @return plugin information, never <code>null</code>
	 */
	public static PluginInformation fromInformation(@NonNull String name, @NonNull String version, String[] authors, String url, String description) {
		return new PluginInformation(Objects.requireNonNull(name, "Plugin name must not be null!"), Objects.requireNonNull(version, "Plugin version must not be null!"), authors == null ? new String[] {} : authors, url, description);
	}
	
	/**
	 * Creates a PluginInformation based on a name, version, and authors.
	 * 
	 * @param name the plugin name
	 * @param version the plugin version
	 * @param authors a list of authors
	 * @return plugin information, never <code>null</code>
	 */
	public static PluginInformation fromIdentifiers(@NonNull String name, @NonNull String version, String[] authors) {
		return fromInformation(name, version, authors, null, null);
	}
	
	/**
	 * Creates a PluginInformation based on main identifiers: name and version.
	 * 
	 * @param name the plugin name
	 * @param version the plugin version
	 * @return plugin information, never <code>null</code>
	 */
	public static PluginInformation fromIdentifiers(@NonNull String name, @NonNull String version) {
		return fromInformation(name, version, null, null, null);
	}
	
	/**
	 * Gets the name of the plugin
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the version of the plugin
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Gets the authors of the plugin
	 * 
	 * @return the authors
	 */
	public String[] getAuthors() {
		return ArraysUtil.copy(authors);
	}
	
	/**
	 * Gets a possible url or website for the plugin
	 * 
	 * @return a potential url
	 */
	@Nullable
	public String getUrl() {
		return url;
	}
	
	/**
	 * Gets a possible description for the plugin
	 * 
	 * @return a potential description
	 */
	@Nullable
	public String getDescription() {
		return description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(authors);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof PluginInformation) {
			PluginInformation other = (PluginInformation) object;
			return getName().equals(other.getName()) && getVersion().equals(other.getVersion()) && Arrays.equals(getAuthors(), other.getAuthors());
		}
		return false;
	}
	
}
