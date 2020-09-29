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

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.SendableMessage;

public class SimpleTextSerialiserTest {
	
	private static String randomString() {
		Random random = ThreadLocalRandom.current();
		byte[] bytes = new byte[random.nextInt(255)];
		random.nextBytes(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	@Test
	public void testDeserialiseSerialise() {
		SimpleTextSerialiser serialiser = SimpleTextSerialiser.getInstance();
		String randomString = randomString();
		SendableMessage deserialised = serialiser.deserialise(randomString);
		assertEquals(randomString, serialiser.serialise(deserialised));
	}
	
}
