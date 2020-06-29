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
package space.arim.api.chat;

import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.chat.JsonTag;
import space.arim.api.chat.MessageParserUtil;
import space.arim.api.util.StringsUtil;

/**
 * Used internally for ease of implementation.
 * 
 * @author A248
 *
 */
public class PlatformSpecificDefaultImplementations {
	
	private static final String codingCharacters = "AaBbCcDdEeFf0123456789KkLlMmNnOoRr";
	
	static String transformColourCodes(String msg, FormattingCodePattern sourceColourPattern, char targetColourChar) {
		char colourChar = sourceColourPattern.getChar();
		char[] source = msg.toCharArray();
		for (int n = 0; n < source.length; n++) {
			if (source[n] == colourChar && n + 1 < source.length && codingCharacters.indexOf(source[n + 1]) != -1) {
				source[n] = targetColourChar;
			}
		}
		return String.valueOf(source);
		// java.lang.IndexOutOfBoundsException: No group 1
		// return sourceColourPattern.getValue().matcher(msg).replaceAll(targetColourChar + "$1");
	}
	
	static String stripColour(String msg, FormattingCodePattern colourPattern) {
		return colourPattern.getPattern().matcher(msg).replaceAll("");
	}
	
	static String stripJson(String msg) {
		StringBuilder builder = new StringBuilder();
		for (String node : MessageParserUtil.DOUBLE_PIPE_PATTERN.split(msg)) {
			if (JsonTag.getFor(node).equals(JsonTag.NONE)) {
				builder.append(node);
			}
		}
		return builder.toString();
	}
	
	static String center(String msg, FormattingCodePattern colourPattern) {
		if (msg == null || msg.isEmpty()) {
			return msg;
		} else if (msg.contains("\n")) {
			/* 
			 * Handling with multi-line messages:
			 * 1. take the message apart according to new lines.
			 * 2. center each message.
			 * 3. concatenate the results.
			 */
			String[] msgs = msg.split("\n");
			for (int n = 0; n < msgs.length; n++) {
				msgs[n] = center(msgs[n], colourPattern);
			}
			return StringsUtil.concat(msgs, '\n');
		}
		return centeredMessageBuffer(msg, colourPattern.getChar()).append(msg).toString();
	}
	
	private static StringBuilder centeredMessageBuffer(String message, char colourChar) {
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		for (char c : message.toCharArray()) {
			if (c == colourChar) {
				previousCode = true;
			} else if (previousCode) {
				previousCode = false;
				isBold = c == 'l' || c == 'L';
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
        int toCompensate = DefaultFontInfo.CENTER_PX - messagePxSize / 2;
        StringBuilder builder = new StringBuilder();
        for (int compensated = 0; compensated < toCompensate; compensated += DefaultFontInfo.SPACE.getLength() + 1) {
        	builder.append(' ');
        }
        return builder;
	}
	
	private enum DefaultFontInfo {
		
		A('A', 5),
		a('a', 5),
		B('B', 5),
		b('b', 5),
		C('C', 5),
		c('c', 5),
		D('D', 5),
		d('d', 5),
		E('E', 5),
		e('e', 5),
		F('F', 5),
		f('f', 4),
		G('G', 5),
		g('g', 5),
		H('H', 5),
		h('h', 5),
		I('I', 3),
		i('i', 1),
		J('J', 5),
		j('j', 5),
		K('K', 5),
		k('k', 4),
		L('L', 5),
		l('l', 1),
		M('M', 5),
		m('m', 5),
		N('N', 5),
		n('n', 5),
		O('O', 5),
		o('o', 5),
		P('P', 5),
		p('p', 5),
		Q('Q', 5),
		q('q', 5),
		R('R', 5),
		r('r', 5),
		S('S', 5),
		s('s', 5),
		T('T', 5),
		t('t', 4),
		U('U', 5),
		u('u', 5),
		V('V', 5),
		v('v', 5),
		W('W', 5),
		w('w', 5),
		X('X', 5),
		x('x', 5),
		Y('Y', 5),
		y('y', 5),
		Z('Z', 5),
		z('z', 5),
		NUM_1('1', 5),
		NUM_2('2', 5),
		NUM_3('3', 5),
		NUM_4('4', 5),
		NUM_5('5', 5),
		NUM_6('6', 5),
		NUM_7('7', 5),
		NUM_8('8', 5),
		NUM_9('9', 5),
		NUM_0('0', 5),
		EXCLAMATION_POINT('!', 1),
		AT_SYMBOL('@', 6),
		NUM_SIGN('#', 5),
		DOLLAR_SIGN('$', 5),
		PERCENT('%', 5),
		UP_ARROW('^', 5),
		AMPERSAND('&', 5),
		ASTERISK('*', 5),
		LEFT_PARENTHESIS('(', 4),
		RIGHT_PERENTHESIS(')', 4),
		MINUS('-', 5),
		UNDERSCORE('_', 5),
		PLUS_SIGN('+', 5),
		EQUALS_SIGN('=', 5),
		LEFT_CURL_BRACE('{', 4),
		RIGHT_CURL_BRACE('}', 4),
		LEFT_BRACKET('[', 3),
		RIGHT_BRACKET(']', 3),
		COLON(':', 1),
		SEMI_COLON(';', 1),
		DOUBLE_QUOTE('"', 3),
		SINGLE_QUOTE('\'', 1),
		LEFT_ARROW('<', 4),
		RIGHT_ARROW('>', 4),
		QUESTION_MARK('?', 5),
		SLASH('/', 5),
		BACK_SLASH('\\', 5),
		LINE('|', 1),
		TILDE('~', 5),
		TICK('`', 2),
		PERIOD('.', 1),
		COMMA(',', 1),
		SPACE(' ', 3),
		DEFAULT('a', 4);
		
		private final static int CENTER_PX = 154;
		
		private char character;
		private int length;
		
		private DefaultFontInfo(char character, int length) {
			this.character = character;
			this.length = length;
		}
		
		private char getCharacter(){
			return character;
		}
		
		int getLength(){
			return length;
		}
		
		int getBoldLength(){
			return (this == DefaultFontInfo.SPACE) ? getLength() : getLength() + 1;
		}
		
		static DefaultFontInfo getDefaultFontInfo(char c){
			for(DefaultFontInfo info : DefaultFontInfo.values()){
				if (info.getCharacter() == c) {
					return info;
				}
			}
			return DefaultFontInfo.DEFAULT;
		}
	}
	
}
