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
package space.arim.api.server.bungee;

import java.util.Collection;
import java.util.stream.Collectors;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import space.arim.api.annotation.Platform;
import space.arim.api.server.ChatUtil;

/**
 * Basic BungeeCord utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.BUNGEE)
public final class BungeeUtil {

	private BungeeUtil() {}
	
	/**
	 * Iterates across online players, finds players matching the first argument in the args parameter, and returns applicable player names.
	 * 
	 * @param args command arguments to tab complete
	 * @param proxy the proxy server, use {@link net.md_5.bungee.api.plugin.Plugin#getProxy() plugin.getProxy()} for this parameter
	 * @return tab completable set
	 */
	public static Iterable<String> getPlayerNameTabComplete(String[] args, Collection<ProxiedPlayer> players) {
		return (args.length > 0) ? players.stream().map((player) -> player.getName()).filter((name) -> name.toLowerCase().startsWith(args[0])).collect(Collectors.toSet()) : players.stream().map((player) -> player.getName()).collect(Collectors.toSet());
	}
	
	/**
	 * Adds colour to a message according to '&' colour codes. <br>
	 * See {@link ChatUtil#colourBungee(String)} for more information.
	 * 
	 * @param colourable the input string
	 * @return a coloured BaseComponent array
	 */
	public static BaseComponent[] colour(String colourable) {
		return ChatUtil.colourBungee(colourable);
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
	 * <br>
	 * <b>Example usage:</b> <br>
	 * <pre>
	 * <code>
	 * public void sendJsonMessage(ProxiedPlayer player, String json) {
	 *   // Woohoo! Now we can send json messages!
	 *   player.sendMessage(BungeeUtil.parseJson(json));
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
	 * <b>Differs from {@link #parseJson(String)} in that this assumes the string is already coloured.
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
	
}
