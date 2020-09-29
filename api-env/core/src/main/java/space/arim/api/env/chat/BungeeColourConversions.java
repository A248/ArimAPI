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

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.manipulator.ColourManipulator;

final class BungeeColourConversions {

	static final boolean HAS_HEX_SUPPORT;
	
	private BungeeColourConversions() {}
	
	static {
		boolean hexSupport = false;
		try {
			fromHex0(PredefinedColour.GRAY.getColour());
			hexSupport = true;
		} catch (NoSuchMethodError ignored) {}
		HAS_HEX_SUPPORT = hexSupport;
	}
	
	private static ChatColor fromHex0(int hex) {
		return ChatColor.of("#" + Integer.toHexString(hex));
	}
	
	// Can never return null
	static ChatColor convertColour(int hex) {
		if (HAS_HEX_SUPPORT) {
			PredefinedColour preDef = PredefinedColour.getExactTo(hex);
			if (preDef != null) {
				return ChatColor.getByChar(preDef.getCodeChar());
			}
			return fromHex0(hex);
		} else {
			return ChatColor.getByChar(PredefinedColour.getNearestTo(hex).getCodeChar());
		}
	}
	
	/**
	 * Gets the colour code character from a ChatColor ordinal, else 'z' if the ordinal
	 * does not correspond to a <i>colour</i> code
	 * 
	 * @param ordinal the ChatColor ordinal
	 * @return the character or 'z'
	 */
	private static char fromChatColorOrdinal(int ordinal) {
		switch (ordinal) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return Integer.toString(ordinal).charAt(0);
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
			return "abcdef".charAt(ordinal - 10);
		default:
			return 'z'; // indicates unknown
		}
	}
	
	static int convertColour(ChatColor colour) {
		if (HAS_HEX_SUPPORT) {
			Color awtColor = colour.getColor();
			if (awtColor == null) {
				// yet another pitfall of the BungeeCord Component API
				return 0;
			}
			return ColourManipulator.getInstance().fromJavaAwt(awtColor);

		} else {
			@SuppressWarnings("deprecation")
			int ordinal = colour.ordinal();
			char codeChar = fromChatColorOrdinal(ordinal);
			if (codeChar == 'z') {
				return 0;
			}
			return PredefinedColour.getByChar(codeChar).getColour();
		}
	}
	
	static int convertStyles(TextComponent component) {
		int styles = 0;
		if (component.isObfuscated()) {
			styles |= MessageStyle.MAGIC;
		}
		if (component.isBold()) {
			styles |= MessageStyle.BOLD;
		}
		if (component.isStrikethrough()) {
			styles |= MessageStyle.STRIKETHROUGH;
		}
		if (component.isUnderlined()) {
			styles |= MessageStyle.UNDERLINE;
		}
		if (component.isItalic()) {
			styles |= MessageStyle.ITALIC;
		}
		return styles;
	}
	
}
