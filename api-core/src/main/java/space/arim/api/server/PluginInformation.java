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
package space.arim.api.server;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import org.bukkit.plugin.PluginDescriptionFile;

import org.spongepowered.api.plugin.PluginContainer;

import net.md_5.bungee.api.plugin.PluginDescription;

import space.arim.api.annotation.Platform;

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
	
	private PluginInformation(String name, String version, String[] authors, String url, String description) {
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
	
	@Platform(Platform.Type.SPIGOT)
	public static PluginInformation getForSpigot(PluginDescriptionFile bukkitDescription) {
		return new PluginInformation(bukkitDescription.getName(), bukkitDescription.getVersion(), bukkitDescription.getAuthors().toArray(new String[] {}), bukkitDescription.getWebsite(), bukkitDescription.getDescription());
	}
	
	@Platform(Platform.Type.BUNGEE)
	public static PluginInformation getForBungee(PluginDescription bungeeDescription) {
		return new PluginInformation(bungeeDescription.getName(), bungeeDescription.getVersion(), bungeeDescription.getAuthor() == null ? new String[] {} : new String[] {bungeeDescription.getAuthor()}, null, bungeeDescription.getDescription());
	}
	
	@Platform(Platform.Type.SPONGE)
	public static PluginInformation getForSponge(PluginContainer spongePlugin) {
		return new PluginInformation(spongePlugin.getId(), Objects.requireNonNull(spongePlugin.getVersion().orElse(null), "Version must not be null!"), spongePlugin.getAuthors().toArray(new String[] {}), spongePlugin.getUrl().orElse(null), spongePlugin.getDescription().orElse(null));
	}
	
	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String[] getAuthors() {
		return authors;
	}
	
	@Nullable
	public String getUrl() {
		return url;
	}
	
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
