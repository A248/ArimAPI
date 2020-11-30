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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.SendableMessage;

public class JsonSkSerialiserTest {

	private final JsonSkSerialiser serialiser = JsonSkSerialiser.getInstance();
	
	private static JsonSection section(ChatComponent...contents) {
		return JsonSection.create(List.of(contents));
	}
	
	private static ChatComponent component(int colour, int styles, String text) {
		return new ChatComponent.Builder().colour(colour).styles(styles).text(text).build();
	}
	
	private static int getColour(char codeChar) {
		return PredefinedColour.getByChar(codeChar).getColour();
	}
	
	private void parseSimpleMessage(String message) {
		JsonSection section = section(
				component(getColour('a'), MessageStyle.BOLD, "Message with colours "),
				component(getColour('c'), 0, "and more"));
		SendableMessage expected = new SendableMessage.Builder().add(section).build();
		SendableMessage actual = serialiser.deserialise(message);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSimpleParse() {
		parseSimpleMessage("&a&lMessage with colours &r&cand more");
	}
	
	@Test
	public void testSimpleParseMultipleSections() {
		parseSimpleMessage("&a&lMessage with colours ||&r&cand more");
	}
	
	@Test
	public void testParseJson() {
		JsonSection section1 = new JsonSection.Builder().addContent(
				component(getColour('a'), MessageStyle.BOLD, "Message escaping pipes || with colours ||"))
				.hoverAction(JsonHover.create(List.of(component(getColour('7'), 0, "That is a tooltip"))))
				.build();
		JsonSection section2 = section(component(getColour('c'), 0, "and more"));
		JsonSection section3 = section(component(0xFFFFFF, 0, "ttp: with 'ttp:' too"));

		SendableMessage expected = SendableMessage.create(section1, section2, section3);

		// Ensure deserialiser recognises escaping
		SendableMessage actual = serialiser.deserialise(
				"&a&lMessage escaping pipes |||| with colours ||||||ttp:&7That is a tooltip||&r&cand more||nil:ttp: with 'ttp:' too");
		assertEquals(expected, actual);

		// Ensure serialiser writers proper escaping
		String backToString = serialiser.serialise(expected);
		assertEquals(expected, serialiser.deserialise(backToString));
	}
	
	@Test
	public void testDeserialiseReserialise() {
		String original = "&aBanned &c&o%VICTIM%&r&a for &a&o%DURATION%&r&a because of &e&o%REASON%&r&a.";
		SendableMessage deserialised = serialiser.deserialise(original);
		assertEquals(original, serialiser.serialise(deserialised));
	}
	
	@Test
	public void testRedundantColours() {
		String original = "&aBanned &c&o%VICTIM%&a for &a&o%DURATION%&a because of &e&o%REASON%&a.";
		String reserialised = serialiser.serialise(serialiser.deserialise(original));
		assertEquals("&aBanned &c&o%VICTIM%&a for %DURATION% because of &e%REASON%&a.", reserialised);
	}
	
}
