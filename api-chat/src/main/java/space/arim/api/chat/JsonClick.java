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
 * An action which will act when a player clicks on the chat message. The type and value are akin to form and matter.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public final class JsonClick extends JsonAction {

	private final ClickType type;
	private final String value;
	
	private JsonClick(ClickType type, String value) {
		this.type = Objects.requireNonNull(type, "type");
		this.value = Objects.requireNonNull(value, "value");
	}
	
	/**
	 * Creates from a click type and value
	 * 
	 * @param type the click type
	 * @param value the value
	 * @return the click action
	 * @throws NullPointerException if either parameter is null
	 */
	public static JsonClick create(ClickType type, String value) {
		return new JsonClick(type, value);
	}
	
	/**
	 * Create a click action which runs a command
	 * 
	 * @param command the command to run
	 * @return the click action
	 * @throws NullPointerException if {@code command} is null
	 */
	public static JsonClick runCommand(String command) {
		return create(ClickType.RUN_COMMAND, command);
	}
	
	/**
	 * Create a click action which suggests a command
	 * 
	 * @param command the command to run
	 * @return the click action
	 * @throws NullPointerException if {@code command} is null
	 */
	public static JsonClick suggestCommand(String command) {
		return create(ClickType.SUGGEST_COMMAND, command);
	}
	
	/**
	 * Create a click action which opens a url
	 * 
	 * @param url the hyperlink to open
	 * @return the click action
	 * @throws NullPointerException if {@code url} is null
	 */
	public static JsonClick openUrl(String url) {
		return create(ClickType.OPEN_URL, url);
	}
	
	/**
	 * The type of the click action
	 * 
	 * @return the type, never null
	 */
	public ClickType getType() {
		return type;
	}
	
	/**
	 * The value of the click action
	 * 
	 * @return the value, never null
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
	public enum ClickType {
		
		RUN_COMMAND,
		SUGGEST_COMMAND,
		OPEN_URL
		
	}

	@Override
	public String toString() {
		return "JsonClick [type=" + type + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type.hashCode();
		result = prime * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JsonClick)) {
			return false;
		}
		JsonClick other = (JsonClick) object;
		return type == other.type && value.equals(other.value);
	}
	
}
