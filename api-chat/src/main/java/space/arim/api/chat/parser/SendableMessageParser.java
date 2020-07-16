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
package space.arim.api.chat.parser;

import space.arim.api.chat.SendableMessage;

/**
 * General interface for parsers of {@link SendableMessage}s.
 * 
 * @author A248
 *
 */
public interface SendableMessageParser {

	/**
	 * Parses a {@link SendableMessage} based on the specified modes
	 * 
	 * @param rawMessage the message to parse
	 * @param coloursMode the {@code ColoursMode} to use
	 * @param jsonMode the {@code JsonMode} to use
	 * @return the parsed {@code SendableMessage}
	 * @throws UnsupportedOperationException if the parser does not support such a mode
	 */
	SendableMessage parseMessage(String rawMessage, ColourMode coloursMode, JsonMode jsonMode);
	
	/**
	 * Approaches for parsing a message's colours
	 * 
	 * @author A248
	 *
	 */
	enum ColourMode {
		
		/**
		 * No colours should be parsed
		 * 
		 */
		NONE,
		
		/**
		 * Parse only legacy colour codes, i.e. those starting with '{@literal &}'.
		 * 
		 */
		LEGACY_ONLY,
		
		/**
		 * Parse all colour codes, including legacy colour codes, and also
		 * hex colour codes, denoted by the pseudo regex <pre>{@literal <#(6*(A-F|0-9))>}</pre>. <br>
		 * <br>
		 * Furthermore, 3-character hex values are permitted. For example, all of the following are accepted: <br>
		 * <pre>
		 * {@literal &a, &1, &f}
		 * {@literal <#AA00AA>}
		 * {@literal <#00FFFF>}
		 * {@literal <#0FF>} (equivalent to the previous)
		 * </pre>
		 * 
		 */
		ALL_COLOURS,
		
	}
	
	/**
	 * Approaches for parsing a message's JSON
	 * 
	 * @author A248
	 *
	 */
	enum JsonMode {
		
		/**
		 * Do not parse any JSON
		 * 
		 */
		NONE,
		
		/**
		 * Parse JSON formatting via the JSON.sk format by RezzedUp.
		 * 
		 */
		JSON_SK
		
	}
	
}
