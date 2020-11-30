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
import space.arim.api.chat.SendableMessage;

abstract class ColourDeserialiserImpl extends DeserialiserImpl {
	
	private final Pattern colourPattern;
	
	private final JsonSection.Builder sectionBuilder = new JsonSection.Builder();
	
	private int currentColour = 0xFFFFFF;
	private int currentStyles;
	
	ColourDeserialiserImpl(Pattern colourPattern, String content) {
		super(content);
		this.colourPattern = colourPattern;
	}
	
	private void parseColours() {
		int beginIndex = 0;

		Matcher matcher = colourPattern.matcher(content());
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
	
	void addStyle(int style) {
		currentStyles |= style;
	}
	
	void setStyles(int styles) {
		currentStyles = styles;
	}
	
	void setColour(int colour) {
		currentColour = colour;
	}
	
	abstract void applyFormatting(String formatGroup);
	
	JsonSection.Builder deserialiseBuilder() {
		parseColours();
		return sectionBuilder;
	}

	@Override
	SendableMessage deserialise() {
		return new SendableMessage.Builder().add(deserialiseBuilder().build()).build();
	}
	
}
