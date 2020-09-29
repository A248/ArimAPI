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

import java.util.Locale;
import java.util.regex.Pattern;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

class JsonSkDeserialiserImpl extends DeserialiserImpl {
	
	private static final Pattern COLOUR_PATTERN = Pattern.compile(
			// Legacy colour codes
			"(&[0-9A-Fa-fK-Rk-r])|"
			// Hex codes such as <#00AAFF>
			+ "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)|"
			// and the shorter <#4BC>
			+ "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)");
	
	private static final Pattern DOUBLE_PIPES_PATTERN = Pattern.compile("||", Pattern.LITERAL);
	
	private final SendableMessage.Builder messageBuilder = new SendableMessage.Builder();
	
	private JsonSection.Builder sectionBuilder = new JsonSection.Builder();
	private JsonHover currentHover;
	private JsonClick currentClick;
	private JsonInsertion currentInsert;

	JsonSkDeserialiserImpl(String content) {
		super(COLOUR_PATTERN, content);
	}
	
	private void parseJson(String content) {
		for (String node : DOUBLE_PIPES_PATTERN.split(content)) {
			if (node.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getTag(node);
			if (tag == JsonTag.NONE) {

				messageBuilder.add(sectionBuilder.build());
				sectionBuilder = new ColourDeserialiserImpl(colourPattern(), node).deserialiseBuilder();

			} else if (!sectionBuilder.isEmpty()) {

				parseTag(tag, node.substring(4));
			}
		}
		messageBuilder.add(sectionBuilder.build());
	}
	
	private void parseTag(JsonTag tag, String value) {
		switch (tag) {
		case TTP:
			currentHover = JsonHover.create(new ColourDeserialiserImpl(colourPattern(), value).deserialiseBuilder().getContents());
			break;
		case CMD:
			currentClick = JsonClick.runCommand(value);
			break;
		case SGT:
			currentClick = JsonClick.suggestCommand(value);
			break;
		case URL:
			currentClick = JsonClick.openUrl(value);
			break;
		case INS:
			currentInsert = JsonInsertion.create(value);
			break;
		default:
			throw new IllegalStateException("Unknown JsonTag " + tag);
		}
		sectionBuilder.hoverAction(currentHover).clickAction(currentClick).insertionAction(currentInsert);
	}
	
	private enum JsonTag {
		
		NONE,
		TTP,
		CMD,
		SGT,
		URL,
		INS;
		
		static JsonTag getTag(String node) {
			if (node.length() <= 4) {
				return NONE;
			}
			switch (node.substring(0, 4).toLowerCase(Locale.ROOT)) {
			case "ttp:":
				return TTP;
			case "cmd:":
				return CMD;
			case "sgt:":
				return SGT;
			case "url:":
				return URL;
			case "ins:":
				return INS;
			default:
				return NONE;
			}
		}
	}

	@Override
	SendableMessage deserialise() {
		content().lines().forEach(this::parseJson);
		return messageBuilder.build();
	}
	
}
