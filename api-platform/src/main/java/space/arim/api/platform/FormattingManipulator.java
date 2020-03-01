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
import space.arim.api.chat.MessageUtil;

/**
 * A utility which operates on <code>String</code>s to change their formatting.
 * 
 * @author A248
 *
 */
public interface FormattingManipulator {
	
	/**
	 * Changes valid colour codes using the given source char to equivalent codes using the target char.
	 * 
	 * @param sourceColourPattern the pattern corresponding to the source colour code char
	 * @param targetColourChar the target colour code char
	 * @return the input string with valid colour codes converted
	 */
	default String transformColourCodes(String msg, FormattingCodePattern sourceColourPattern, char targetColourChar) {
		return PlatformSpecificDefaultImplementations.transformColourCodes(msg, sourceColourPattern, targetColourChar);
	}
	
	/**
	 * Changes valid colour codes using the given source char to equivalent codes using '&' as the target char.
	 * 
	 * @param sourceColourPattern the pattern corresponding to the source colour code char
	 * @return the input string with valid colour codes converted
	 */
	default String transformColourCodes(String msg, FormattingCodePattern sourceColourPattern) {
		return transformColourCodes(msg, sourceColourPattern, MessageUtil.DEFAULT_COLOUR_CHAR);
	}
	
	/**
	 * Removes valid colour codes from a message using the given colour code char.
	 * 
	 * @param msg the source message
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return the same message with valid colours removed
	 */
	default String stripColour(String msg, FormattingCodePattern colourPattern) {
		return PlatformSpecificDefaultImplementations.stripColour(msg, colourPattern);
	}
	
	/**
	 * Removes valid colour codes from a message.
	 * 
	 * @param msg the source message
	 * @return the same message with valid colours removed
	 */
	default String stripColour(String msg) {
		return stripColour(msg, FormattingCodePattern.get());
	}
	
	/**
	 * Removes all Json message formatting
	 * 
	 * @param msg the source message
	 * @return the same message with only text, colours, and styles remaining
	 */
	default String stripJson(String msg) {
		return PlatformSpecificDefaultImplementations.stripJson(msg);
	}
	
	/**
	 * Centers a message using the specified colour code char to detect colours and styles. <br>
	 * <br>
	 * The centering is precise, taking into account individual character lengths and applied colour codes. <br>
	 * Messages with line breaks (<code>'\n'</code>) are treated properly; each individual line is centered.
	 * 
	 * @param msg the message to center
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return a centered message
	 */
	default String center(String msg, FormattingCodePattern colourPattern) {
		return PlatformSpecificDefaultImplementations.center(msg, colourPattern);
	}
	
	/**
	 * Centers a message using the '&' colour codes to detect colours and styles. <br>
	 * <br>
	 * The centering is precise, taking into account individual character lengths and applied colour codes. <br>
	 * Messages with line breaks (<code>'\n'</code>) are treated properly; each individual line is centered.
	 * 
	 * @param msg the message to center
	 * @return a centered message
	 */
	default String center(String msg) {
		return center(msg, FormattingCodePattern.get());
	}
	
}
