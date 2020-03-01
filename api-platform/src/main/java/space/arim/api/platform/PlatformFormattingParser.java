/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform;

import space.arim.api.chat.FormattingCodePattern;

/**
 * A utility which parses <code>String</code>s, including colours, styles, or json features as specified. <br>
 * The type of the result depends on the specific platform.
 * 
 * @author A248
 *
 * @param <T> the platform specific type
 */
public interface PlatformFormattingParser<T> {
	
	/**
	 * Adds colour to a message according to the specified colour code char.
	 * 
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return a coloured Text object
	 */
	T colour(String msg, FormattingCodePattern colourPattern);
	
	/**
	 * Adds colour to a message according to '{@literal &}' colour codes.
	 * 
	 * @param msg the source message
	 * @return a coloured result
	 */
	default T colour(String msg) {
		return colour(msg, FormattingCodePattern.get());
	}
	
	/**
	 * Forms a message directly, without explicitly parsing colour codes. <br>
	 * Note that, if colour remains in the input string, clients may still see colours. <br>
	 * <br>
	 * <b>This method does not remove colour from a message</b>!
	 * Use {@link AbstractPlatformMessages#stripColour(String)} or {@link AbstractPlatformMessages#stripColour(String, FormattingCodePattern)} instead.
	 * 
	 * @param msg the source message
	 * @return a direct result
	 */
	T uncoloured(String msg);
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <br>
	 * The following json tags are parsed: <code>ttp</code>, <code>url</code>, <code>cmd</code>, <code>sgt</code>, and <code>ins</code>. <br>
	 * <b>Colours are parsed according to the specified colour code char.
	 * 
	 * @param msg the source message
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return a json formatted result
	 */
	T parseJson(String msg, FormattingCodePattern colourPattern);
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <br>
	 * The following json tags are parsed: <code>ttp</code>, <code>url</code>, <code>cmd</code>, <code>sgt</code>, and <code>ins</code>. <br>
	 * <b>Colours are parsed according to '{@literal &}' colour codes.
	 * 
	 * @param msg the source message
	 * @return a json formatted result
	 */
	default T parseJson(String msg) {
		return parseJson(msg, FormattingCodePattern.get());
	}
	
	/**
	 * Parses Json messages without explicitly parsing colour codes. <br>
	 * Note that, if colour remains in the input string, clients may still see colours.
	 * 
	 * @param msg the source message
	 * @return a json formatted result
	 */
	T parseUncolouredJson(String msg);
	
}
