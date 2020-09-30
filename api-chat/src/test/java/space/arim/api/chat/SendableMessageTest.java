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
package space.arim.api.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SendableMessageTest {
	
	private int RANDOM_COLOUR;
	private int RANDOM_STYLES;
	
	@BeforeEach
	public void setup() {
		Random random = ThreadLocalRandom.current();
		RANDOM_COLOUR = random.nextInt(0xFFFFFF + 1);
		for (int style : MessageStyle.values()) {
			if (random.nextBoolean()) {
				RANDOM_STYLES |= style;
			}
		}
	}

	@Test
	public void testCompaction() {
		List<JsonSection> simple = List.of(section(component("text and more stuff")));
		List<JsonSection> needsComponentCompaction = List.of(
				section(component("text "), component("and more stuff")));
		List<JsonSection> needsMultipleCompaction = List.of(
				section(component("text ")),
				section(component("and more stuff")));
		assertEquals(
				SendableMessage.create(simple),
				SendableMessage.create(needsComponentCompaction));
		assertEquals(
				SendableMessage.create(needsComponentCompaction),
				SendableMessage.create(needsMultipleCompaction));
	}
	
	private ChatComponent component(String text) {
		return new ChatComponent.Builder().text(text).colour(RANDOM_COLOUR).styles(RANDOM_STYLES).build();
	}
	
	private JsonSection section(ChatComponent...components) {
		return new JsonSection.Builder().addContent(components).build();
	}
	
}
