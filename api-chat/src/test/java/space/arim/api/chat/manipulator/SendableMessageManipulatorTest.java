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
package space.arim.api.chat.manipulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.manipulator.SendableMessageManipulator.TextGoal;

public class SendableMessageManipulatorTest {

	private static final String TEXT_VARIABLE = "%TEXT%";
	private static final String TEXT_VALUE = "value1";
	private static final String HOVER_VARIABLE = "%HOVER%";
	private static final String HOVER_VALUE = "value2";

	@Test
	public void testReplaceText() {
		ChatComponent mainContentTemplate = new ChatComponent.Builder().text("Some text with variable " + TEXT_VARIABLE).build();
		JsonHover hoverTemplate = JsonHover.create(List.of(new ChatComponent.Builder().text("hover variable " + HOVER_VARIABLE).build()));

		ChatComponent mainContentReplaced = new ChatComponent.Builder().text("Some text with variable " + TEXT_VALUE).build();
		JsonHover hoverReplaced = JsonHover.create(List.of(new ChatComponent.Builder().text("hover variable " + HOVER_VALUE).build()));

		SendableMessage messageTemplate = SendableMessage.create(new JsonSection.Builder()
				.addContent(mainContentTemplate).hoverAction(hoverTemplate).build());
		UnaryOperator<String> replaceFunction = (str) -> str.replace(HOVER_VARIABLE, HOVER_VALUE).replace(TEXT_VARIABLE, TEXT_VALUE);

		// All goals
		assertEquals(
				SendableMessage.create(new JsonSection.Builder()
						.addContent(mainContentReplaced).hoverAction(hoverReplaced).build()),
				SendableMessageManipulator.create(messageTemplate).replaceText(replaceFunction));

		// Hover text goal not included
		assertEquals(
				SendableMessage.create(new JsonSection.Builder()
						.addContent(mainContentReplaced).hoverAction(hoverTemplate).build()),
				SendableMessageManipulator.create(messageTemplate, TextGoal.SIMPLE_TEXT).replaceText(replaceFunction));

		// Simple text goal not included
		assertEquals(
				SendableMessage.create(new JsonSection.Builder()
						.addContent(mainContentTemplate).hoverAction(hoverReplaced).build()),
				SendableMessageManipulator.create(messageTemplate, TextGoal.HOVER_TEXT).replaceText(replaceFunction));
	}

}
