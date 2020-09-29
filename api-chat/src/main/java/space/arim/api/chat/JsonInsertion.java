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
 * An action which will insert text into the player's chat when such player shift clicks the chat message.
 * 
 * @author A248
 *
 */
public final class JsonInsertion extends JsonAction {

	private final String value;
	
	private JsonInsertion(String value) {
		this.value = Objects.requireNonNull(value, "value");
	}
	
	/**
	 * Creates from the string to insert into the player's chat
	 * 
	 * @param value the string to insert
	 * @return the insertion action
	 * @throws NullPointerException if {@code value} is null
	 */
	public static JsonInsertion create(String value) {
		return new JsonInsertion(value);
	}
	
	/**
	 * Gets the value of this insertion, which is inserted into the chat input when the
	 * chat message is shift clicked
	 * 
	 * @return the insertion value, never {@code null}
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "JsonInsertion [value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JsonInsertion)) {
			return false;
		}
		JsonInsertion other = (JsonInsertion) object;
		return value.equals(other.value);
	}
	
}
