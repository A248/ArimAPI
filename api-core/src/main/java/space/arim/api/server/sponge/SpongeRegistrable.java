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
package space.arim.api.server.sponge;

import org.spongepowered.api.plugin.PluginContainer;

import space.arim.universal.registry.Registrable;

import space.arim.api.annotation.Platform;
import space.arim.api.server.PluginInformation;
import space.arim.api.server.PluginRegistrable;

/**
 * A helper class for resources registered on Sponge. <br>
 * Subclassing SpongeRegistrable provides useful default implementations of {@link Registrable#getName()}, {@link Registrable#getAuthor()}, and {@link Registrable#getVersion()}
 * based on the corresponding {@link PluginContainer}. See {@link #getName()}, {@link #getAuthor()}, and {@link #getVersion()} for details. <br>
 * <br>
 * <b>Usage:</b> <br>
 * 1. The service type must be an interface to use SpongeRegistrable. <br>
 * 2. Let your registered implementation of the service type <code>extend</code> SpongeRegistrable. <br>
 * 3. Use the Sponge plugin manager (<code>Sponge.getPluginManager()</code>) to retrieve a {@link PluginContainer} for your plugin. <br>
 * 4. Provide super constructor {@link #SpongeRegistrable(PluginContainer)} with your PluginContainer. <br>
 * <br>
 * <b>Example:</b> <br>
 * You want your resource called 'SuperChat' to be registered as a ChatProvider (Note that ChatProvider must be an interface which extends Registrable). <br>
 * Your SuperChat class should look this:
 * <pre><code>
 * public class SuperChat extends SpongeRegistrable implements ChatProvider {
 *   public SuperChat(PluginContainer plugin) {
 *     super(plugin);
 *   }
 *   public byte getPriority() {
 *     return RegistryPriority.NORMAL;
 *   }
 * }
 * </code></pre>
 * Your main plugin class should look like this:
 * <pre><code>
 * {@code @Plugin}(id = "yourpluginid", name = "SuperChat")
 * public class SuperChatPlugin {
 *   public SuperChatPlugin() {
 *     // Getting your plugin
 *     PluginContainer plugin = Sponge.getPluginManager().getPlugin("yourpluginid").get();
 *     // Creating your SuperChat chat provider
 *     SuperChat chat = new SuperChat(plugin);
 *     // Registering your chat provider
 *     UniversalRegistry.get().register(ChatProvider.class, chat);
 *   }
 * }
 * </code></pre>
 * Good, now you don't have to worry about overriding #getName, #getAuthor, and #getVersion in your SuperChat resource.
 * SpongeRegistrable automatically takes care of all of that.
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.SPONGE)
public abstract class SpongeRegistrable extends PluginRegistrable {

	private final PluginContainer plugin;
	
	/**
	 * Initialises the abstract class from a provided {@link PluginContainer}. 
	 * The Sponge plugin which is registering a resource should use its own PluginContainer. <br>
	 * <br>
	 * The <code>PluginContainer</code> will be used for {@link Registrable#getName()}, {@link Registrable#getAuthor()}, and {@link Registrable#getVersion()}.
	 * 
	 * @param plugin the plugin main
	 */
	public SpongeRegistrable(PluginContainer plugin) {
		super(PluginInformation.getForSponge(plugin));
		this.plugin = plugin;
	}
	
	/**
	 * Gets the {@link PluginContainer} associated with this SpongeRegistrable for convenient use.
	 * 
	 * @return the plugin
	 */
	protected final PluginContainer getPlugin() {
		return plugin;
	}
	
}
