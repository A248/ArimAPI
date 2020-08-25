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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import space.arim.api.chat.HexManipulator;
import space.arim.api.chat.HexManipulatorTest;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;
import space.arim.api.chat.parser.SendableMessageParser.ColourMode;
import space.arim.api.chat.parser.SendableMessageParser.JsonMode;

public class StandardSendableMessageParserTest {

	private SendableMessageParser parser;
	
	@BeforeEach
	public void setup() {
		parser = new StandardSendableMessageParser();
	}
	
	@Test
	public void testBasicColoursParse() {
		SendableMessage legacyColours = new SendableMessage.Builder()
				.add(new TextualComponent.Builder().colour(PredefinedColour.BLACK.getColour()).text("Start ").build())
				.add(new TextualComponent.Builder().colour(PredefinedColour.DARK_PURPLE.getColour())
						.styles(MessageStyle.BOLD).text("more").build())
				.build();
		String legacyColourText = "&r&0Start &r&5&lmore";
		assertEquals(legacyColours, parser.parseMessage(legacyColourText, ColourMode.LEGACY_ONLY, JsonMode.NONE));
		assertEquals(legacyColours, parser.parseMessage(legacyColourText, ColourMode.ALL_COLOURS, JsonMode.NONE));

		int hexColour = HexManipulatorTest.randomHex();
		SendableMessage allColours = new SendableMessage.Builder()
				.add(new TextualComponent.Builder().colour(hexColour).text("Custom colour ").build())
				.add(new TextualComponent.Builder().colour(PredefinedColour.DARK_AQUA.getColour()).text("legacy colour").build())
				.build();

		assertEquals(allColours, parser.parseMessage("<#" + bytesToHex(hexColour) + ">Custom colour &3legacy colour",
				ColourMode.ALL_COLOURS, JsonMode.NONE));
	}
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toLowerCase().toCharArray();
	static String bytesToHex(int hex) {
		byte[] bytes = new HexManipulator().toBytes(hex);
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
}
