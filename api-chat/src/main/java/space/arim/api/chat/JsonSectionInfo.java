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

import java.util.List;

/**
 * Information relating to a {@link JsonSection} or {@link JsonSection.Builder}. <br>
 * <br>
 * Defines a series of attributes regarding a section of a message, but provides no guarantee of immutability
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public interface JsonSectionInfo extends Emptyable {

	/**
	 * Gets the contents of this section or builder. Neither the list nor any of its contents may be {@code null}.
	 * The list may not be modified.
	 * 
	 * @return the contents of this section or builder, never {@code null}
	 */
	List<ChatComponent> getContents();
	
	/**
	 * Whether this section or builder is empty
	 * 
	 * @return true if empty, false otherwise
	 */
	@Override
	default boolean isEmpty() {
		return getContents().isEmpty();
	}
	
	/**
	 * Gets the hover action of this section or builder
	 * 
	 * @return the hover action or {@code null} if there is none
	 */
	JsonHover getHoverAction();
	
	/**
	 * Gets the click action of this section or builder
	 * 
	 * @return the click action or {@code null} if there is none
	 */
	JsonClick getClickAction();
	
	/**
	 * Gets the insertion action of this section or builder
	 * 
	 * @return the insertion action or {@code null} if there is none
	 */
	JsonInsertion getInsertionAction();
	
	/**
	 * Convenience method to determine whether any of the JSON actions are set. Equivalent to: <br>
	 * <code>getHoverAction() != null || getClickAction() != null || getInsertionAction() != null</code>
	 * 
	 * @return true if any json action is set, false otherwise
	 */
	default boolean hasAnyActions() {
		return getHoverAction() != null || getClickAction() != null || getInsertionAction() != null;
	}
	
}
