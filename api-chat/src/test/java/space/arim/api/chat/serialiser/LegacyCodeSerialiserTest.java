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

import org.junit.jupiter.api.Test;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;

public class LegacyCodeSerialiserTest {

	private final LegacyCodeSerialiser serialiser = LegacyCodeSerialiser.getInstance('&');

	@Test
	public void testDeserialiseSerialise() {
		String original = "&6Hello &amore text";
		SendableMessage deserialised = serialiser.deserialise(original);
		String reserialised = serialiser.serialise(deserialised);
		assertEquals(original, reserialised);
	}

	@Test
	public void testOrderOfDeserialisationAndConcatentationEquivalent() {
		String rawPrefix = "&6&lPrefix &r&8»&7 ";
		String rawContent = "&7Message";

		SendableMessage concatThenDeserialise = serialiser.deserialise(rawPrefix + rawContent);

		SendableMessage prefix = serialiser.deserialise(rawPrefix);
		SendableMessage content = serialiser.deserialise(rawContent);
		SendableMessage deserialiseThenConcat = prefix.concatenate(content);

		assertEquals(concatThenDeserialise, deserialiseThenConcat);
		assertEquals(serialiser.serialise(concatThenDeserialise), serialiser.serialise(deserialiseThenConcat));
	}

	@Test
	public void testConcatnatePrefixDifferentColour() {
		String rawPrefix = "&6&lPrefix &r&8»&7 ";
		String rawContent = "Platform category Paper";
		SendableMessage prefix = serialiser.deserialise(rawPrefix);
		SendableMessage content = serialiser.deserialise(rawContent);
		SendableMessage deserialiseThenConcat = prefix.concatenate(content);
		assertEquals("&6&lPrefix &r&8»&7 &fPlatform category Paper", serialiser.serialise(deserialiseThenConcat));
	}

	@Test
	public void testWhiteColourByDefault() {
		SendableMessage expected = SendableMessage.create(
				new JsonSection.Builder().addContent(
						new ChatComponent.Builder().text("Platform category Paper").build()
				).build());
		assertEquals(expected, serialiser.deserialise("Platform category Paper"));
	}

}
