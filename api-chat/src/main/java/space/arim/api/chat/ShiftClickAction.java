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

/**
 * An action which will make text appear when a player shift clicks on the chat message.
 * 
 * @author A248
 *
 */
public class ShiftClickAction extends JsonAction {

	private final String insertion;
	
	/**
	 * Creates from an insertion to insert into the chat
	 * 
	 * @param insertion the insertion
	 */
	public ShiftClickAction(String insertion) {
		this.insertion = insertion;
	}
	
	/**
	 * The insertion of the shift click action
	 * 
	 * @return the insertion, can be null
	 */
	public String getInsertion() {
		return insertion;
	}

	/**
	 * Helper method to create a shift click action which inserts text
	 * 
	 * @param text the text to insert
	 * @return the shift click action
	 */
	public static ShiftClickAction insertText(String text) {
		return new ShiftClickAction(text);
	}

	@Override
	public String toString() {
		return "ShiftClickAction [insertion=" + insertion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((insertion == null) ? 0 : insertion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ShiftClickAction)) {
			return false;
		}
		ShiftClickAction other = (ShiftClickAction) obj;
		if (insertion == null) {
			if (other.insertion != null) {
				return false;
			}
		} else if (!insertion.equals(other.insertion)) {
			return false;
		}
		return true;
	}
	
}
