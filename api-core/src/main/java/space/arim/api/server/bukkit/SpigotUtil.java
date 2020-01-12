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

//import org.spongepowered.api.Platform;

import org.bukkit.event.EventHandler;

import net.md_5.bungee.api.chat.BaseComponent;

import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.annotation.Platform;
import space.arim.api.server.ChatUtil;
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
	 * Adds color to a message according to '&' color codes. <br>
	 * See {@link ChatUtil#color(String)} for more information.
	 * 
	 * @param colorable
	 * @return
	 */
	@EventHandler
	public static String color(String colorable) {
		return ChatUtil.color(colorable);
	}
	
	/**
	 * Removes color from a message according to '&' color codes. <br>
	 * See {@link ChatUtil#stripColor(String)} for more information.
	 * 
	 * @param colorable the input string
	 * @return an uncoloured string
	 */
	public static String stripColor(String colorable) {
		return ChatUtil.stripColor(colorable);
	}
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
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
	 * <b>Differs from {@link #parseJson(String)} in that this assumes the string is already colored.
	 * 
	 * @param jsonable the input string
	 * @return a BaseComponent array
	 */
	public static BaseComponent[] parseColoredJson(String jsonable) {
		return ChatUtil.parseColoredJson(jsonable);
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
	
}
