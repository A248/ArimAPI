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

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;
import space.arim.api.jsonchat.ChatMessageParser;
import space.arim.api.jsonchat.ClickEventInfo;
import space.arim.api.jsonchat.ParsingVisitor;

class JsonSkDeserialiserImpl extends DeserialiserImpl {

	private final SendableMessage.Builder messageBuilder = new SendableMessage.Builder();
	
	private JsonSection.Builder sectionBuilder = new JsonSection.Builder();

	JsonSkDeserialiserImpl(String content) {
		super(content);
	}

	private class AsParsingVisitor implements ParsingVisitor {

		@Override
		public void visitHoverEvent(String hoverValue) {
			JsonHover hover = JsonHover.create(new AllColourDeserialiserImpl(hoverValue).deserialiseBuilder().getContents());
			sectionBuilder.hoverAction(hover);
		}

		@Override
		public void visitClickEvent(ClickEventInfo.ClickType clickType, String value) {
			switch (clickType) {
			case RUN_COMMAND:
				sectionBuilder.clickAction(JsonClick.runCommand(value));
				break;
			case SUGGEST_COMMAND:
				sectionBuilder.clickAction(JsonClick.suggestCommand(value));
				break;
			case OPEN_URL:
				sectionBuilder.clickAction(JsonClick.openUrl(value));
				break;
			default:
				throw new IllegalStateException("Unknown click type " + clickType);
			}
		}

		@Override
		public void visitInsertion(String value) {
			sectionBuilder.insertionAction(JsonInsertion.create(value));
		}

		@Override
		public void visitPlainText(String text) {
			messageBuilder.add(sectionBuilder.build());
			sectionBuilder = new AllColourDeserialiserImpl(text).deserialiseBuilder();
		}
	}

	@Override
	SendableMessage deserialise() {
		new ChatMessageParser(new AsParsingVisitor(), content()).parse();
		messageBuilder.add(sectionBuilder.build());
		return messageBuilder.build();
	}
	
}
