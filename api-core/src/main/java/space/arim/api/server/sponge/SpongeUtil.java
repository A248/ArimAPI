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
package space.arim.api.server.sponge;

import java.util.List;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import space.arim.api.annotation.Platform;
import space.arim.api.server.ChatUtil;
import space.arim.api.server.ServerUtil;

/**
 * Basic Sponge utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.SPONGE)
public final class SpongeUtil {

	private SpongeUtil() {}
	
	/**
	 * Iterates across online players, finds players matching the first argument,
	 * and returns applicable player names in alphabetical order.
	 * 
	 * @param args command arguments to tab complete
	 * @param server the server, use {@link Sponge#getServer()} for this parameter
	 * @return a list per the Sponge API
	 */
	public static List<String> getPlayerNameTabComplete(String[] args, Server server) {
		return ServerUtil.getPlayerNameTabComplete(server.getOnlinePlayers().stream().map((player) -> player.getName()), args);
	}
	
	/**
	 * Adds colour to a message according to '{@literal &}' colour codes. <br>
	 * <b>See {@link ChatUtil#colourSponge(String)}</b> for more information.
	 * 
	 * @param colourable the input string
	 * @return a coloured Text object
	 */
	public static Text colour(String colourable) {
		return ChatUtil.colourSponge(colourable);
	}
	
	/**
	 * Removes colour from a message according to '{@literal &}' colour codes. <br>
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
	 * <br>
	 * <b>Example usage:</b> <br>
	 * <pre>
	 * <code>
	 * public void sendJsonMessage(Player player, String json) {
	 *   // Woohoo! Now we can send json messages!
	 *   player.sendMessage(SpongeUtil.parseJson(json));
	 * }
	 * </code>
	 * </pre>
	 * See {@link ChatUtil#parseJsonSponge(String)} for more information.
	 * 
	 * @param jsonable the input string
	 * @return a formatted Text object
	 */
	public static Text parseJson(String jsonable) {
		return ChatUtil.parseJsonSponge(jsonable);
	}
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <b>Differs from {@link #parseJson(String)} in that this assumes the string is already coloured.</b>
	 * 
	 * @param jsonable the input string
	 * @return a formatted Text object
	 */
	public static Text parseColouredJson(String jsonable) {
		return ChatUtil.parseColouredJsonSponge(jsonable);
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
