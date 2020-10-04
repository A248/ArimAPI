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
package space.arim.api.env.chat;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;

class SpongeFormattingConversions {

	private SpongeFormattingConversions() {}
	
	static TextColor convertColour(int hex) {
		char codeChar = PredefinedColour.getNearestTo(hex).getCodeChar();
		switch (codeChar) {
		case '0':
			return TextColors.BLACK;
		case '1':
			return TextColors.DARK_BLUE;
		case '2':
			return TextColors.DARK_GREEN;
		case '3':
			return TextColors.DARK_AQUA;
		case '4':
			return TextColors.DARK_RED;
		case '5':
			return TextColors.DARK_PURPLE;
		case '6':
			return TextColors.GOLD;
		case '7':
			return TextColors.GRAY;
		case '8':
			return TextColors.DARK_GRAY;
		case '9':
			return TextColors.BLUE;
		case 'a':
			return TextColors.GREEN;
		case 'b':
			return TextColors.AQUA;
		case 'c':
			return TextColors.RED;
		case 'd':
			return TextColors.LIGHT_PURPLE;
		case 'e':
			return TextColors.YELLOW;
		case 'f':
			return TextColors.WHITE;
		default:
			throw new IllegalArgumentException("Unknown code char " + codeChar);
		}
	}
	
	static TextStyle convertStyles(ChatComponent component) {
		TextStyle start = TextStyles.NONE;
		start = start.obfuscated(component.hasStyle(MessageStyle.MAGIC));
		start = start.bold(component.hasStyle(MessageStyle.BOLD));
		start = start.strikethrough(component.hasStyle(MessageStyle.STRIKETHROUGH));
		start = start.underline(component.hasStyle(MessageStyle.UNDERLINE));
		start = start.italic(component.hasStyle(MessageStyle.ITALIC));
		return start;
	}
	
}
