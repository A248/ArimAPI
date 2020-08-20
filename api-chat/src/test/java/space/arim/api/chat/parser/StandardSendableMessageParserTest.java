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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;
import space.arim.api.chat.parser.SendableMessageParser.ColourMode;
import space.arim.api.chat.parser.SendableMessageParser.JsonMode;

public class StandardSendableMessageParserTest {

	@Test
	public void testBasicParse() {
		SendableMessage message = new SendableMessage.Builder()
				.add(new TextualComponent.Builder().colour(PredefinedColour.BLACK.getColour()).text("Start ").build())
				.add(new TextualComponent.Builder().colour(PredefinedColour.DARK_PURPLE.getColour()).styles(MessageStyle.BOLD).text("more").build())
				.build();
		assertEquals(message, new StandardSendableMessageParser().parseMessage("&r&0Start &r&5&lmore", ColourMode.LEGACY_ONLY, JsonMode.NONE));
	}
	
}
