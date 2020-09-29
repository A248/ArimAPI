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

import org.junit.jupiter.api.Test;

public class JsonSectionTest {

	@Test
	public void testEquality() {
		JsonSection section = new JsonSection.Builder()
				.addContent(
						new ChatComponent.Builder().colour(PredefinedColour.BLACK.getColour()).text("Start ").build())
				.addContent(
						new ChatComponent.Builder().colour(PredefinedColour.DARK_PURPLE.getColour()).styles(MessageStyle.BOLD).text("more").build())
				.build();
		JsonSection withEmptyComponent = new JsonSection.Builder()
				.addContent(
						new ChatComponent.Builder().colour(PredefinedColour.BLACK.getColour()).text("Start ").build())
				.addContent(new ChatComponent.Builder().text("").build())
				.addContent(
						new ChatComponent.Builder().colour(PredefinedColour.DARK_PURPLE.getColour()).styles(MessageStyle.BOLD).text("more").build())
				.build();
		assertEquals(section, withEmptyComponent);
	}
	
}
