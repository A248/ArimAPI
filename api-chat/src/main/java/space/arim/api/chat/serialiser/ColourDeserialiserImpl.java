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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;

class ColourDeserialiserImpl extends DeserialiserImpl {
	
	private final JsonSection.Builder sectionBuilder = new JsonSection.Builder();
	
	private int currentColour;
	private int currentStyles;
	
	ColourDeserialiserImpl(Pattern colourPattern, String content) {
		super(colourPattern, content);
	}
	
	private void parseColours() {
		int beginIndex = 0;

		Matcher matcher = colourPattern().matcher(content());
		while (matcher.find()) {

			int currentIndex = matcher.start();
			String segment = content().substring(beginIndex, currentIndex);
			if (!segment.isEmpty()) {
				sectionBuilder.addContent(
						new ChatComponent.Builder().text(segment).colour(currentColour).styles(currentStyles).build());
			}
			applyFormatting(matcher.group());
			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end();
		}
		String remaining = content().substring(beginIndex);
		if (!remaining.isEmpty()) {
			sectionBuilder.addContent(
					new ChatComponent.Builder().text(remaining).colour(currentColour).styles(currentStyles).build());
		}
	}
	
	private void applyFormatting(String formatGroup) {
		switch (formatGroup.length()) {
		case 2:
			// Legacy colour code
			char codeChar = formatGroup.charAt(1);
			switch (codeChar) {
			case 'K':
			case 'k':
				currentStyles |= MessageStyle.MAGIC;
				break;
			case 'L':
			case 'l':
				currentStyles |= MessageStyle.BOLD;
				break;
			case 'M':
			case 'm':
				currentStyles |= MessageStyle.STRIKETHROUGH;
				break;
			case 'N':
			case 'n':
				currentStyles |= MessageStyle.UNDERLINE;
				break;
			case 'O':
			case 'o':
				currentStyles |= MessageStyle.ITALIC;
				break;
			case 'R':
			case 'r':
				currentColour = 0;
				currentStyles = 0;
				break;
			default:
				currentColour = PredefinedColour.getByChar(codeChar).getColour();
				break;
			}
			break;
		case 6:
			// Shortened hex colour code
			char[] shortHex = formatGroup.substring(2, 5).toCharArray();
			char[] fullHex = new char[] {shortHex[0], shortHex[0], shortHex[1], shortHex[1], shortHex[2], shortHex[2]};
			currentColour = Integer.parseInt(String.valueOf(fullHex), 16);
			break;
		case 9:
			// Full hex colour code
			currentColour = Integer.parseInt(formatGroup.substring(2, 8), 16);
			break;
		default:
			throw new IllegalStateException("Matched formatting " + formatGroup + " has no known way to be handled");
		}
	}
	
	JsonSection.Builder deserialiseBuilder() {
		parseColours();
		return sectionBuilder;
	}

	@Override
	SendableMessage deserialise() {
		return new SendableMessage.Builder().add(deserialiseBuilder().build()).build();
	}
	
}
