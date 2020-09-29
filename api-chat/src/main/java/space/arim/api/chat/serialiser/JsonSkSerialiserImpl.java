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
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.manipulator.ColourManipulator;

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
			if (!first) {
				builder.append("||");
			} else {
				first = false;
			}

			appendSimpleColoured(builder, section.getContents());

			JsonHover hover = section.getHoverAction();
			if (hover != null) {
				builder.append("||ttp:");
				appendSimpleColoured(builder, hover.getContents());
			}
			appendClick(builder, section.getClickAction());

			JsonInsertion insertion = section.getInsertionAction();
			if (insertion != null) {
				builder.append("||ins:").append(insertion.getValue());
			}
		}
		return builder.toString();
	}
	
	private void appendClick(StringBuilder builder, JsonClick click) {
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
		builder.append(':').append(click.getValue());
	}
	
	private void appendSimpleColoured(StringBuilder builder, List<ChatComponent> components) {
		for (ChatComponent component : components) {
			if (component.isEmpty()) {
				continue;
			}
			PredefinedColour predefinedColour = PredefinedColour.getExactTo(component.getColour());
			if (predefinedColour == null) {
				builder.append("<#").append(bytesToHex(component.getColour())).append('>');
			} else {
				char code = predefinedColour.getCodeChar();
				builder.append('&').append(code);
			}
			new StyleSerialiserImpl('&', builder).serialiseStylesFrom(component);
			builder.append(component.getText());
		}
	}
	
	private static String bytesToHex(int hex) {
		byte[] bytes = ColourManipulator.getInstance().toBytes(hex);
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    if (hexChars[0] == hexChars[1] && hexChars[2] == hexChars[3] && hexChars[4] == hexChars[5]) {
	    	return String.valueOf(new char[] {hexChars[0], hexChars[2], hexChars[4]});
	    }
	    return String.valueOf(hexChars);
	}
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toLowerCase().toCharArray();
	
}
