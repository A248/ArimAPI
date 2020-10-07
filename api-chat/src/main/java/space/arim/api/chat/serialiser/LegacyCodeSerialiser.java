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

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;

/**
 * Message serialiser using legacy formatting codes
 * 
 * @author A248
 *
 */
public final class LegacyCodeSerialiser implements SendableMessageSerialiser {

	private final char codeChar;
	private final Pattern colourPattern;
	
	private static final char AMPERSAND = '&';
	private static final LegacyCodeSerialiser AMPERSAND_INSTANCE = new LegacyCodeSerialiser(AMPERSAND);
	private static final char SECTION_SIGN = '\u00a7';
	private static final LegacyCodeSerialiser SECTION_SIGN_INSTANCE = new LegacyCodeSerialiser(SECTION_SIGN);
	
	private LegacyCodeSerialiser(char codeChar) {
		this.codeChar = codeChar;
		colourPattern = Pattern.compile(Pattern.quote(String.valueOf(codeChar)) + "[0-9A-Fa-fK-Rk-r]");
	}
	
	/**
	 * Creates from a legacy colour code character
	 * 
	 * @param codeChar the code character, such as {@literal '&'}
	 * @return an instance for the code character
	 */
	public static LegacyCodeSerialiser getInstance(char codeChar) {
		switch (codeChar) {
		case AMPERSAND:
			return AMPERSAND_INSTANCE;
		case SECTION_SIGN:
			return SECTION_SIGN_INSTANCE;
		default:
			return new LegacyCodeSerialiser(codeChar);
		}
	}
	
	@Override
	public SendableMessage deserialise(String message) {
		return new LegacyColourDeserialiserImpl(colourPattern, message).deserialise();
	}

	@Override
	public String serialise(SendableMessage message) {
		StringBuilder builder = new StringBuilder();
		char currentColour = ' ';
		int currentStyles = 0;
		for (JsonSection section : message.getSections()) {
			for (ChatComponent component : section.getContents()) {
				if (component.isEmpty()) {
					continue;
				}
				char colour = PredefinedColour.getNearestTo(component.getColour()).getCodeChar();
				int styles = component.getStyles();
				if (styles != currentStyles) {
					// Reset and re-apply colours and styles
					if (currentStyles != 0) {
						builder.append(codeChar).append('r');
					}
					builder.append(codeChar).append(colour);
					new StyleSerialiserImpl(codeChar, builder)
						.serialiseStylesFrom(component);

				} else if (colour != currentColour) {
					// Same styles, different colour
					builder.append(codeChar).append(colour);
				}
				currentColour = colour;
				currentStyles = styles;
				builder.append(component.getText());
			}
		}
		return builder.toString();
	}

}
