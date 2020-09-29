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

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.MessageStyle;

class StyleSerialiserImpl {

	private final char codeChar;
	private final StringBuilder builder;
	
	StyleSerialiserImpl(char codeChar, StringBuilder builder) {
		this.codeChar = codeChar;
		this.builder = builder;
	}
	
	void serialiseStylesFrom(ChatComponent component) {
		if (component.hasStyle(MessageStyle.MAGIC)) {
			builder.append(codeChar).append('k');
		}
		if (component.hasStyle(MessageStyle.BOLD)) {
			builder.append(codeChar).append('l');
		}
		if (component.hasStyle(MessageStyle.STRIKETHROUGH)) {
			builder.append(codeChar).append('m');
		}
		if (component.hasStyle(MessageStyle.UNDERLINE)) {
			builder.append(codeChar).append('n');
		}
		if (component.hasStyle(MessageStyle.ITALIC)) {
			builder.append(codeChar).append('o');
		}
	}
	
}
