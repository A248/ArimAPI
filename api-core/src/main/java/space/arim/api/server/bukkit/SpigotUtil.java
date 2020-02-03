/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.server.bukkit;

import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.EntityType;

import net.md_5.bungee.api.chat.BaseComponent;

import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.annotation.Platform;
import space.arim.api.server.ChatUtil;
import space.arim.api.server.ServerUtil;
import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;

/**
 * Basic Spigot utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.SPIGOT)
public final class SpigotUtil {

	private SpigotUtil() {}
	
	/**
	 * Iterates across online players, finds players matching the first argument,
	 * and returns applicable player names in alphabetical order.
	 * 
	 * @param args command arguments to tab complete
	 * @param server the server, use {@link org.bukkit.plugin.Plugin#getServer() plugin.getServer()} for this parameter
	 * @return a list per the Spigot API
	 */
	public static List<String> getPlayerNameTabComplete(String[] args, Server server) {
		return ServerUtil.getPlayerNameTabComplete(server.getOnlinePlayers().stream().map((player) -> player.getName()), args);
	}
	
	/**
	 * Retrieves the latest version of a posted spigot plugin according to its resourceId. <br>
	 * <b>Be sure to call this method in an asynchronous task to avoid stalling the main thread.</b> <br>
	 * <br>
	 * E.g., for AdvancedBan (resourceId bolded): <br>
	 * * plugin url = https://www.spigotmc.org/resources/advancedban.<b>8695</b>/ <br>
	 * * <code>String latestVersion = SpigotUtil.getLatestSpigotPluginVersion(<b>8695</b>);</code>
	 * 
	 * @param resourceId the spigot plugin id
	 * @return String the latest version
	 * @throws FetcherException if a miscellaneous web problem occured, such as IOException
	 * @throws HttpStatusException if the http status code was not 200
	 */
	public static String getLatestPluginVersion(int resourceId) throws FetcherException, HttpStatusException {
		return FetcherUtil.getLatestSpigotPluginVersion(resourceId);
	}
	
	/**
	 * Adds colour to a message according to '&' colour codes. <br>
	 * See {@link ChatUtil#colour(String)} for more information.
	 * 
	 * @param colourable
	 * @return the coloured string
	 */
	public static String colour(String colourable) {
		return ChatUtil.colour(colourable);
	}
	
	/**
	 * Removes colour from a message according to '&' colour codes. <br>
	 * See {@link ChatUtil#stripColour(String)} for more information.
	 * 
	 * @param colourable the input string
	 * @return an uncoloured string
	 */
	public static String stripColour(String colourable) {
		return ChatUtil.stripColour(colourable);
	}
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * Colors are parsed according to '&' color codes. <br>
	 * <br>
	 * <b>Example usage:</b> <br>
	 * <pre>
	 * <code>
	 * public void sendJsonMessage(Player player, String json) {
	 *   // Woohoo! Now we can send json messages!
	 *   player.spigot().sendMessage(SpigotUtil.parseJson(json));
	 * }
	 * </code>
	 * </pre>
	 * See {@link ChatUtil#parseJson(String)} for more information.
	 * 
	 * @param jsonable the input string
	 * @return a BaseComponent array
	 */
	public static BaseComponent[] parseJson(String jsonable) {
		return ChatUtil.parseJson(jsonable);
	}
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <b>Differs from {@link #parseJson(String)} in that this assumes
	 * the string is already coloured according to '§' color codes.
	 * 
	 * @param jsonable the input string
	 * @return a BaseComponent array
	 */
	public static BaseComponent[] parseColouredJson(String jsonable) {
		return ChatUtil.parseColouredJson(jsonable);
	}
	
	/**
	 * Removes json formatting from an input string. <br>
	 * Useful for sending messages to the console with formatting removed. <br>
	 * See {@link ChatUtil#stripJson(String)} for more information.
	 * 
	 * @param json the input string
	 * @return a string stripped of all json tags
	 */
	public static String stripJson(String json) {
		return ChatUtil.stripJson(json);
	}
	
	/**
	 * Parses an entity type from a string. <br>
	 * Useful for enabling String based, version specific entity types in configuration values.
	 * 
	 * @param type the string to parse from
	 * @return the EntityType if found
	 * @throws IllegalArgumentException if not entity type is found.
	 */
	public static EntityType parseEntityType(String type) {
		for (EntityType entityType : EntityType.values()) {
			if (entityType.name().equalsIgnoreCase(type)) {
				return entityType;
			}
		}
		throw new IllegalArgumentException("Invalid entity type: " + type);
	}
	
}
