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

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

/**
 * A very simple serialiser implementation which only concerns itself with the plain text of a message.
 * All sort of formatting is stripped at serialisation.
 * 
 * @author A248
 *
 */
public final class SimpleTextSerialiser implements SendableMessageSerialiser {

	private static final SimpleTextSerialiser INSTANCE = new SimpleTextSerialiser();
	
	private SimpleTextSerialiser() {}
	
	/**
	 * Gets the instance
	 * 
	 * @return the instance
	 */
	public static SimpleTextSerialiser getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SendableMessage deserialise(String message) {
		return new SendableMessage.Builder().add(new JsonSection.Builder().addContent(
				new ChatComponent.Builder().text(message).build()).build()).build();
	}

	@Override
	public String serialise(SendableMessage message) {
		StringBuilder builder = new StringBuilder();
		for (JsonSection section : message.getSections()) {
			for (ChatComponent component : section.getContents()) {
				builder.append(component.getText());
			}
		}
		return builder.toString();
	}

}
