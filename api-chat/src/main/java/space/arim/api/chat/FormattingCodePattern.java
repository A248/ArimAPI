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

import java.util.regex.Pattern;

/**
 * Represents a regex pattern matching valid formatting codes. <br>
 * A formatting code is <i>valid</i> if the char identifying the colour or style maps to an actual client colour. <br>
 * <br>
 * {@link #getPattern()} may be used to get the underyling regex pattern.
 * 
 * @author A248
 *
 */
public final class FormattingCodePattern {
	
	/*
	 * These 2 formatting code patterns are commonly used, so it makes sense to cache their regex Pattern's
	 * 
	 */
	
	private static final FormattingCodePattern AMPERSAND_PATTERN = compile(MessageParserUtil.DEFAULT_FORMATTING_CHAR);
	private static final FormattingCodePattern SECTION_SIGN_PATTERN = compile(MessageParserUtil.SECTION_SIGN_FORMATTING_CHAR);
	
	private final char codeChar;
	private final Pattern pattern;
	
	private FormattingCodePattern(char codeChar, Pattern pattern) {
		this.codeChar = codeChar;
		this.pattern = pattern;
	}
	
	/**
	 * Gets the regex pattern used to match valid formatting codes
	 * 
	 * @return the pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * Gets the formatting code char this pattern corresponds to.
	 * 
	 * @return the formatting code char
	 */
	public char getChar() {
		return codeChar;
	}
	
	private static FormattingCodePattern compile(char codeChar) {
		return new FormattingCodePattern(codeChar, Pattern.compile(Pattern.quote(String.valueOf(codeChar)) + "[0-9A-Fa-fK-Rk-r]"));
	}
	
	/**
	 * Gets the default formatting code pattern.
	 * 
	 * @return the pattern used for {@link MessageParserUtil#DEFAULT_FORMATTING_CHAR}
	 */
	public static FormattingCodePattern get() {
		return get(MessageParserUtil.DEFAULT_FORMATTING_CHAR);
	}
	
	/**
	 * Gets a formatting code pattern for an arbitrary colour char, such as '§'. <br>
	 * The pattern will match all valid formatting codes in a message using the given char.
	 * 
	 * @param codeChar the formatting code character, like '§'
	 * @return a formatting code pattern for formatting codes
	 */
	public static FormattingCodePattern get(char codeChar) {
		switch (codeChar) {
		case MessageParserUtil.DEFAULT_FORMATTING_CHAR:
			return AMPERSAND_PATTERN;
		case MessageParserUtil.SECTION_SIGN_FORMATTING_CHAR:
			return SECTION_SIGN_PATTERN;
		default:
			return compile(codeChar);
		}
	}
	
}
