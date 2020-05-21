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
 * An action which will show a tooltip when a player hovers over the chat message.
 * 
 * @author A248
 *
 */
public class HoverAction extends JsonAction {

	private final Message message;
	
	/**
	 * Creates from a hover message to show when a player hovers over the main message
	 * 
	 * @param message the hover message
	 */
	public HoverAction(Message message) {
		this.message = (message == null) ? null : message.stripJson();
	}
	
	/**
	 * Gets the {@link Message} which will be shown to players when they hover on the chat message. <br>
	 * The returned message cannot and will not contain any {@link JsonComponent}s.
	 * 
	 * @return the message which will be shown, can be null
	 */
	public Message getMessage() {
		return message;
	}
	
	/**
	 * Helper method to create a hover action which displays a tooltip
	 * 
	 * @param tooltip the tooltip
	 * @return the hover action
	 */
	public static HoverAction showTooltip(Message tooltip) {
		return new HoverAction(tooltip);
	}

	@Override
	public String toString() {
		return "HoverAction [message=" + message + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HoverAction)) {
			return false;
		}
		HoverAction other = (HoverAction) obj;
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		return true;
	}
	
}
