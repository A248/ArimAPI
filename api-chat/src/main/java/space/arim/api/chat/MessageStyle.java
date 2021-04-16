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

import java.util.Set;

/**
 * Definition of specific message styles. To combine styles, simply bitwise OR them together.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public final class MessageStyle {

	/**
	 * Magic or obfuscated text
	 * 
	 */
	public static final int MAGIC = 0b00001;
	/**
	 * Bolded text
	 * 
	 */
	public static final int BOLD = 0b00010;
	/**
	 * Strikethrough text
	 * 
	 */
	public static final int STRIKETHROUGH = 0b00100;
	/**
	 * Underlined text
	 * 
	 */
	public static final int UNDERLINE = 0b01000;
	/**
	 * Italicised text
	 * 
	 */
	public static final int ITALIC = 0b10000;
	
	private static final Set<Integer> VALUES = Set.of(MAGIC, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC);
	
	private MessageStyle() {}
	
	/**
	 * Gets an immutable set containing of all styles
	 * 
	 * @return an immutable set of all style integers
	 */
	public static Set<Integer> values() {
		return VALUES;
	}
	
	/**
	 * Gets a combined style comprising all the message styles
	 * 
	 * @return all the message styles
	 */
	public static int allStyles() {
		return MAGIC | BOLD | STRIKETHROUGH | UNDERLINE | ITALIC;
	}
	
	static void checkStylesRange(int styles) {
		if ((styles & ~MessageStyle.allStyles()) != 0) {
			throw new IllegalArgumentException("Styles " + Integer.toBinaryString(styles) + " out of range");
		}
	}
	
}
