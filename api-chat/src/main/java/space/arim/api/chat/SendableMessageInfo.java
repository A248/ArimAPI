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
 * Information relating to a {@link SendableMessage} or {@link SendableMessage.Builder} <br>
 * <br>
 * Defines a message composed of a list of sections with attributes, but provides no guarantee of immutability
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public interface SendableMessageInfo extends Emptyable {

	/**
	 * Gets the sections of this message or builder. The list may not be modified
	 * 
	 * @return the components of this message info
	 */
	List<JsonSection> getSections();
	
	/**
	 * Whether this message or builder is empty
	 * 
	 * @return true if empty, false otherwise
	 */
	@Override
	default boolean isEmpty() {
		return getSections().isEmpty();
	}
	
}
