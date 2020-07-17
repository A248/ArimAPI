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
package space.arim.api.chat.parser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

/**
 * Implementation of {@link SendableMessageParser} supporting the {@link ColourMode#NONE}, {@link ColourMode#LEGACY_ONLY},
 * and {@link ColourMode#ALL_COLOURS} colour modes, and {@link JsonMode#NONE} and {@link JsonMode#JSON_SK} json modes.
 * 
 * @author A248
 *
 */
public class StandardSendableMessageParser implements SendableMessageParser {
	
	private static void addColouredContent(InternalBuilder builder, String message, Pattern colourPattern) {
		if (colourPattern == null) {
			// No colours requested
			builder.addBuilder((new TextualComponent.Builder().text(message)));
			return;
		}
		int beginIndex = 0;

		int currentColour = 0;
		int currentStyles = 0;

		Matcher matcher = colourPattern.matcher(message);
		while (matcher.find()) {

			int currentIndex = matcher.start();
			String segment = message.substring(beginIndex, currentIndex);
			if (!segment.isEmpty()) {
				builder.addBuilder(
						new TextualComponent.Builder().text(segment).colour(currentColour).styles(currentStyles));
			}
			switch (matcher.group().length()) {
			case 2:
				// Legacy colour code
				char codeChar = segment.charAt(1);
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
				currentColour = Integer.parseInt(segment.substring(2, 5), 16);
				break;
			case 9:
				// Full hex colour code
				currentColour = Integer.parseInt(segment.substring(2, 8), 16);
				break;
			default:
				throw new IllegalStateException("Matched colour code " + segment + " has no way to be handled");
			}
			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end() + 1;
		}
	}
	
	private static SendableMessage parseColours0(String message, Pattern colourPattern) {
		SimpleBuilder simpleBuilder = new SimpleBuilder();
		addColouredContent(simpleBuilder, message, colourPattern);
		return simpleBuilder.build();
	}
	
	private static SendableMessage parseJson0(String message, Pattern colourPattern) {
		JsonBuilder jsonBuilder = new JsonBuilder();
		JsonHover currentHover = null;
		JsonClick currentClick = null;
		JsonInsertion currentInsert = null;

		for (String node : DoublePipes.PATTERN.split(message)) {
			if (node.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getFor(node);
			if (tag == JsonTag.NONE) {

				jsonBuilder.concretifyCurrent();
				addColouredContent(jsonBuilder, node, colourPattern);

			} else if (jsonBuilder.hasAnyCurrent()) {
				String value = node.substring(4);
				switch (tag) {
				case TTP:
					currentHover = new JsonHover(parseColours0(value, colourPattern));
					break;
				case CMD:
					currentClick = new JsonClick(JsonClick.Type.RUN_COMMAND, value);
					break;
				case SGT:
					currentClick = new JsonClick(JsonClick.Type.SUGGEST_COMMAND, value);
					break;
				case URL:
					currentClick = new JsonClick(JsonClick.Type.OPEN_URL, value);
					break;
				case INS:
					currentInsert = new JsonInsertion(value);
					break;
				default:
					throw new IllegalStateException("Unknown JsonTag " + tag);
				}
				final JsonHover hover = currentHover;
				final JsonClick click = currentClick;
				final JsonInsertion insert = currentInsert;
				jsonBuilder.modifyCurrent((builder) -> {
					JsonComponent.Builder json;
					if (builder instanceof JsonComponent.Builder) {
						json = (JsonComponent.Builder) builder; 
					} else {
						json = new JsonComponent.Builder(builder);
					}
					return json.hoverAction(hover).clickAction(click).insertionAction(insert);
				});
			}
		}
		return jsonBuilder.build();
	}
	
	private static Pattern getColoursPattern(ColourMode colourMode) {
		switch (colourMode) {
		case NONE:
			return null;
		case LEGACY_ONLY:
			return LegacyColours.PATTERN;
		case ALL_COLOURS:
			return AllColours.PATTERN;
		default:
			throw new UnsupportedOperationException("Does not support ColoursMode " + colourMode);
		}
	}

	@Override
	public SendableMessage parseMessage(String rawMessage, ColourMode coloursMode, JsonMode jsonMode) {
		Objects.requireNonNull(rawMessage, "Raw message must not be null");
		Objects.requireNonNull(coloursMode, "Colours mode must not be null");
		Objects.requireNonNull(jsonMode, "Json mode must not be null");

		Pattern colourPattern = getColoursPattern(coloursMode);
		switch (jsonMode) {
		case NONE:
			return parseColours0(rawMessage, colourPattern);
		case JSON_SK:
			return parseJson0(rawMessage, colourPattern);
		default:
			throw new UnsupportedOperationException("Does not support JsonMode " + jsonMode);
		}
	}
	
}
