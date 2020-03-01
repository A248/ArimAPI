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

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import space.arim.universal.util.proxy.CaptiveReference;

/**
 * Represents a regex pattern matching valid formatting codes. <br>
 * A formatting code is <i>valid</i> if the char identifying the colour or style maps to an actual client colour. <br>
 * <br>
 * {@link #getValue()} may be used to get the underyling regex pattern.
 * 
 * @author A248
 *
 */
public final class FormattingCodePattern extends CaptiveReference<Pattern> {
	
	private static final ConcurrentHashMap<Character, FormattingCodePattern> CACHE = new ConcurrentHashMap<Character, FormattingCodePattern>();
	
	private final char codeChar;
	
	private FormattingCodePattern(char codeChar, Pattern pattern) {
		super(pattern);
		this.codeChar = codeChar;
	}
	
	/**
	 * Gets the regex pattern used to match valid formatting codes
	 * 
	 * @return the pattern
	 */
	@Override
	public Pattern getValue() {
		return super.getValue();
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
	 * @return the pattern used for {@link MessageUtil#DEFAULT_FORMATTING_CHAR}
	 */
	public static FormattingCodePattern get() {
		return get(MessageUtil.DEFAULT_FORMATTING_CHAR);
	}
	
	/**
	 * Gets a formatting code pattern for an arbitrary colour char, such as '§'. <br>
	 * The pattern will match all valid formatting codes in a message using the given char. <br>
	 * <br>
	 * Results are cached in order to provide faster resolution for successive calls. The cache is thread safe.
	 * 
	 * @param codeChar the formatting code character, like '§'
	 * @return a formatting code pattern for formatting codes
	 */
	public static FormattingCodePattern get(char codeChar) {
		return CACHE.computeIfAbsent(codeChar, FormattingCodePattern::compile);
	}
	
	/**
	 * Gets a formatting code pattern for an arbitrary colour char, such as '&' or '§'. <br>
	 * The pattern will match all valid formatting codes in a message using the given char. <br>
	 * <br>
	 * As the method name suggests, unlike {@link #get()}, results are not cached.
	 * This method is provided in rare cases where caching is not desirable,
	 * such as a user input or a one time conversion of an older data format.
	 * 
	 * @param codeChar the formatting code character, like '&'
	 * @return a formatting code pattern for formatting codes
	 */
	public static FormattingCodePattern getUncached(char codeChar) {
		return (codeChar == MessageUtil.DEFAULT_FORMATTING_CHAR) ? get(codeChar) : compile(codeChar);
	}
	
}
