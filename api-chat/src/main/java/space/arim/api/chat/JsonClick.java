/* 
 * ArimAPI-chat
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-chat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-chat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-chat. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat;

import java.util.Objects;

/**
 * An action which will act when a player clicks on the chat message.
 * 
 * @author A248
 *
 */
public final class JsonClick extends JsonAction {

	private final Type type;
	private final String value;
	
	/**
	 * Creates from a type and value. <br>
	 * The type and value are akin to form and matter.
	 * 
	 * @param type the type
	 * @param value the value
	 */
	public JsonClick(Type type, String value) {
		this.type = Objects.requireNonNull(type, "JsonClick type must not be null");
		this.value = Objects.requireNonNull(value, "JsonClick value must not be null");
	}
	
	/**
	 * The type of the click action
	 * 
	 * @return the type, never {@code null}
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * The value of the click action
	 * 
	 * @return the value, never {@code null}
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * An enum for click action types supported
	 * 
	 * @author A248
	 *
	 */
	public enum Type {
		
		RUN_COMMAND,
		SUGGEST_COMMAND,
		OPEN_URL
		
	}
	
	/**
	 * Helper method to create a click action which runs a command
	 * 
	 * @param command the command to run
	 * @return the click action
	 */
	public static JsonClick runCommand(String command) {
		return new JsonClick(Type.RUN_COMMAND, command);
	}
	
	/**
	 * Helper method to create a click action which suggests a command
	 * 
	 * @param command the command to run
	 * @return the click action
	 */
	public static JsonClick suggestCommand(String command) {
		return new JsonClick(Type.SUGGEST_COMMAND, command);
	}
	
	/**
	 * Helper method to create a click action which opens a url
	 * 
	 * @param url the hyperlink to open
	 * @return the click action
	 */
	public static JsonClick openUrl(String url) {
		return new JsonClick(Type.OPEN_URL, url);
	}
	
}
