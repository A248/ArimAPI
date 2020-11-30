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

import java.util.regex.Pattern;

import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;

class LegacyColourDeserialiserImpl extends ColourDeserialiserImpl {

	LegacyColourDeserialiserImpl(Pattern colourPattern, String content) {
		super(colourPattern, content);
	}

	@Override
	void applyFormatting(String formatGroup) {
		char codeChar = formatGroup.charAt(1);
		switch (codeChar) {
		case 'K':
		case 'k':
			addStyle(MessageStyle.MAGIC);
			break;
		case 'L':
		case 'l':
			addStyle(MessageStyle.BOLD);
			break;
		case 'M':
		case 'm':
			addStyle(MessageStyle.STRIKETHROUGH);
			break;
		case 'N':
		case 'n':
			addStyle(MessageStyle.UNDERLINE);
			break;
		case 'O':
		case 'o':
			addStyle(MessageStyle.ITALIC);
			break;
		case 'R':
		case 'r':
			setColour(0xFFFFFF);
			setStyles(0);
			break;
		default:
			int colour = PredefinedColour.getByChar(codeChar).getColour();
			setColour(colour);
			break;
		}
	}

}
