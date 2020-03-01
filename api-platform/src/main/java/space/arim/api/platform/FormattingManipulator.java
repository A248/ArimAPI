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
	 * Changes valid formatting codes using the given source char to equivalent codes using the target char.
	 * 
	 * @param sourceFormattingPattern the pattern corresponding to the source formatting code char
	 * @param targetFormattingChar the target formatting code char
	 * @return the input string with valid formatting codes converted
	 */
	default String transformFormattingCodes(String msg, FormattingCodePattern sourceFormattingPattern, char targetFormattingChar) {
		return PlatformSpecificDefaultImplementations.transformColourCodes(msg, sourceFormattingPattern, targetFormattingChar);
	}
	
	/**
	 * Changes valid formatting codes using the given source char to equivalent codes using '&' as the target char.
	 * 
	 * @param sourceFormattingPattern the pattern corresponding to the source formatting code char
	 * @return the input string with valid formatting codes converted
	 */
	default String transformFormattingCodes(String msg, FormattingCodePattern sourceFormattingPattern) {
		return transformFormattingCodes(msg, sourceFormattingPattern, MessageUtil.DEFAULT_COLOUR_CHAR);
	}
	
	/**
	 * Removes valid formatting codes from a message using the given formatting code char.
	 * 
	 * @param msg the source message
	 * @param formattingPattern the pattern corresponding to the formatting code char
	 * @return the same message with valid colours removed
	 */
	default String stripFormatting(String msg, FormattingCodePattern formattingPattern) {
		return PlatformSpecificDefaultImplementations.stripColour(msg, formattingPattern);
	}
	
	/**
	 * Removes valid formatting codes from a message.
	 * 
	 * @param msg the source message
	 * @return the same message with valid colours removed
	 */
	default String stripFormatting(String msg) {
		return stripFormatting(msg, FormattingCodePattern.get());
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
	 * Centers a message using the specified formatting code char to detect colours and styles. <br>
	 * <br>
	 * The centering is precise, taking into account individual character lengths and applied formatting codes. <br>
	 * Messages with line breaks (<code>'\n'</code>) are treated properly; each individual line is centered.
	 * 
	 * @param msg the message to center
	 * @param formattingPattern the pattern corresponding to the formatting code char
	 * @return a centered message
	 */
	default String center(String msg, FormattingCodePattern formattingPattern) {
		return PlatformSpecificDefaultImplementations.center(msg, formattingPattern);
	}
	
	/**
	 * Centers a message using the '&' formatting codes to detect colours and styles. <br>
	 * <br>
	 * The centering is precise, taking into account individual character lengths and applied formatting codes. <br>
	 * Messages with line breaks (<code>'\n'</code>) are treated properly; each individual line is centered.
	 * 
	 * @param msg the message to center
	 * @return a centered message
	 */
	default String center(String msg) {
		return center(msg, FormattingCodePattern.get());
	}
	
}
