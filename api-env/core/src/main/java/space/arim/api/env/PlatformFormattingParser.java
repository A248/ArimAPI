/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

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
	 * Adds colour to a message according to the specified formatting code char.
	 * 
	 * @param msg the source message
	 * @param formattingPattern the pattern corresponding to the formatting code char
	 * @return a coloured Text object
	 */
	T colour(String msg, FormattingCodePattern formattingPattern);
	
	/**
	 * Adds colour to a message according to '{@literal &}' formatting codes.
	 * 
	 * @param msg the source message
	 * @return a coloured result
	 */
	default T colour(String msg) {
		return colour(msg, FormattingCodePattern.get());
	}
	
	/**
	 * Forms a message directly, without explicitly parsing formatting codes. <br>
	 * Note that, if colour remains in the input string, clients may still see colours. <br>
	 * <br>
	 * <b>This method does not remove colour from a message.</b> It merely parses a message
	 * ignoring colours. The exact consequences of parsing a message with this method which in truth
	 * contains colours or formats are undefined.
	 * 
	 * @param msg the source message
	 * @return a direct result
	 */
	T uncoloured(String msg);
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <br>
	 * The following json tags are parsed: <code>ttp</code>, <code>url</code>, <code>cmd</code>, <code>sgt</code>, and <code>ins</code>. <br>
	 * <b>Colours are parsed according to the specified formatting code char.</b>
	 * 
	 * @param msg the source message
	 * @param formattingPattern the pattern corresponding to the formatting code char
	 * @return a json formatted result
	 */
	T parseJson(String msg, FormattingCodePattern formattingPattern);
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <br>
	 * The following json tags are parsed: <code>ttp</code>, <code>url</code>, <code>cmd</code>, <code>sgt</code>, and <code>ins</code>. <br>
	 * <b>Colours are parsed according to '{@literal &}' formatting codes.</b>
	 * 
	 * @param msg the source message
	 * @return a json formatted result
	 */
	default T parseJson(String msg) {
		return parseJson(msg, FormattingCodePattern.get());
	}
	
	/**
	 * Parses Json messages without explicitly parsing formatting codes. <br>
	 * Note that, if colour remains in the input string, clients may still see colours.
	 * 
	 * @param msg the source message
	 * @return a json formatted result
	 */
	T parseUncolouredJson(String msg);
	
}
