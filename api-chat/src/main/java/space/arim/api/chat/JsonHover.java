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
 * An action which will show a tooltip when a player hovers over the chat message.
 * 
 * @author A248
 *
 */
public final class JsonHover extends JsonAction {

	private final SendableMessage tooltip;
	
	/**
	 * Creates from a tooltip to show when a player hovers over the main message
	 * 
	 * @param tooltip the tooltip to display
	 */
	public JsonHover(SendableMessage tooltip) {
		this.tooltip = Objects.requireNonNull(tooltip, "Tooltip must not be null");
	}
	
	/**
	 * Gets the {@link SendableMessage} which will be shown to players when they hover on the chat message.
	 * 
	 * @return the tooltip which will be shown, never {@code null}
	 */
	public SendableMessage getTooltip() {
		return tooltip;
	}

	@Override
	public String toString() {
		return "JsonHover [tooltip=" + tooltip + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tooltip.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JsonHover)) {
			return false;
		}
		JsonHover other = (JsonHover) object;
		return tooltip.equals(other.tooltip);
	}
	
}
