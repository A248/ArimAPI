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
package space.arim.api.chat.serialiser;

import java.util.Objects;
import java.util.regex.Pattern;

import space.arim.api.chat.SendableMessage;

abstract class DeserialiserImpl {

	private final Pattern colourPattern;
	private final String content;
	
	DeserialiserImpl(Pattern colourPattern, String content) {
		this.colourPattern = Objects.requireNonNull(colourPattern, "internal error: colourPattern");
		this.content = Objects.requireNonNull(content, "internal error: content");
	}
	
	Pattern colourPattern() {
		return colourPattern;
	}
	
	String content() {
		return content;
	}
	
	abstract SendableMessage deserialise();
	
}
