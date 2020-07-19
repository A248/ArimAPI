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
 * Information relating to a {@link SendableMessage} or {@link SendableMessage.Builder}
 * 
 * @author A248
 *
 */
public interface SendableMessageInfo {

	/**
	 * Gets the components of this message or builder. The returned list
	 * may or may not be mutable.
	 * 
	 * @return the components of this message info
	 */
	List<TextualComponent> getComponents();
	
	/**
	 * Provides a string representation of this object, including all of its components
	 * 
	 * @return a string representation of this message info
	 */
	@Override
	String toString();
	
}