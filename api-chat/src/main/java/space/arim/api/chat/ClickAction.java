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
 * An action which will run when a player clicks on the chat message.
 * 
 * @author A248
 *
 */
public class ClickAction extends JsonAction {

	private final Type type;
	private final String value;
	
	/**
	 * Creates from a type and value. <br>
	 * The type and value are akin to form and matter.
	 * 
	 * @param type the type
	 * @param value the value
	 */
	public ClickAction(Type type, String value) {
		this.type = Objects.requireNonNull(type, "ClickAction type must not be null");
		this.value = value;
	}
	
	/**
	 * The type of the click action
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * The value of the click action
	 * 
	 * @return the value, can be null
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * An enum for click actions supported
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
	public static ClickAction runCommand(String command) {
		return new ClickAction(Type.RUN_COMMAND, command);
	}
	
	/**
	 * Helper method to create a click action which suggest a command
	 * 
	 * @param command the command to run
	 * @return the click action
	 */
	public static ClickAction suggestCommand(String command) {
		return new ClickAction(Type.SUGGEST_COMMAND, command);
	}
	
	/**
	 * Helper method to create a click action which opens a url
	 * 
	 * @param url the hyperlink to open
	 * @return the click action
	 */
	public static ClickAction openUrl(String url) {
		return new ClickAction(Type.OPEN_URL, url);
	}

	@Override
	public String toString() {
		return "ClickAction [type=" + type + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClickAction)) {
			return false;
		}
		ClickAction other = (ClickAction) obj;
		if (type != other.type) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
}
