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

import space.arim.api.chat.SendableMessage;

public class LegacyCodeSerialiserTest {

	@Test
	public void testDeserialiseSerialise() {
		LegacyCodeSerialiser serialiser = LegacyCodeSerialiser.getInstance('&');
		String original = "&6Hello &amore text";
		SendableMessage deserialised = serialiser.deserialise(original);
		String reserialised = serialiser.serialise(deserialised);
		assertEquals(original, reserialised);
	}
	
}
