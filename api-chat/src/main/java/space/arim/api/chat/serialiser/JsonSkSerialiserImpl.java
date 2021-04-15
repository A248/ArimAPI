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

import java.util.List;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

import static space.arim.api.jsonchat.adventure.internal.ComponentSerialization.appendColor;

class JsonSkSerialiserImpl {
	
	private final SendableMessage message;
	private final StringBuilder builder = new StringBuilder();
	
	JsonSkSerialiserImpl(SendableMessage message) {
		this.message = message;
	}

	String serialise() {

		boolean first = true;
		for (JsonSection section : message.getSections()) {
			if (section.isEmpty()) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				builder.append("||");
			}

			appendSimpleColoured(section.getContents());

			JsonHover hover = section.getHoverAction();
			if (hover != null) {
				builder.append("||ttp:");
				appendComponents(builder, hover.getContents());
			}
			appendClick(section.getClickAction());

			JsonInsertion insertion = section.getInsertionAction();
			if (insertion != null) {
				builder.append("||ins:").append(escapeDoublePipes(insertion.getValue()));
			}
		}
		return builder.toString();
	}
	
	private void appendClick(JsonClick click) {
		if (click == null) {
			return;
		}
		builder.append("||");
		switch (click.getType()) {
		case OPEN_URL:
			builder.append("url");
			break;
		case RUN_COMMAND:
			builder.append("cmd");
			break;
		case SUGGEST_COMMAND:
			builder.append("sgt");
			break;
		default:
			throw new UnsupportedOperationException("Not implemented for " + click.getType());
		}
		builder.append(':').append(escapeDoublePipes(click.getValue()));
	}
	
	private void appendSimpleColoured(List<ChatComponent> components) {
		StringBuilder localBuilder = new StringBuilder();
		appendComponents(localBuilder, components);
		String localString = localBuilder.toString();
		if (JsonTag.getTag(localString) != JsonTag.NONE || localString.startsWith("||")) {
			builder.append("nil:");
		}
		builder.append(localString);
	}
	
	private static void appendComponents(StringBuilder builder, List<ChatComponent> components) {
		int currentStyles = 0;
		int currentColour = 0xFFFFFF;
		char formattingChar = '&';
		for (ChatComponent component : components) {
			if (component.isEmpty()) {
				continue;
			}
			int colour = component.getColour();
			int styles = component.getStyles();
			if (styles != currentStyles) {
				// Reset and re-apply colours and styles
				if (currentStyles != 0) {
					builder.append(formattingChar).append('r');
				}
				appendColor(colour, builder);
				new StyleSerialiserImpl(formattingChar, builder)
					.serialiseStylesFrom(component);

			} else if (colour != currentColour) {
				// Same styles, different colour
				appendColor(colour, builder);
			}
			currentColour = colour;
			currentStyles = styles;
			builder.append(escapeDoublePipes(component.getText()));
		}
	}
	
	private static String escapeDoublePipes(String text) {
		return text.replace("||", "||||");
	}
	
}
