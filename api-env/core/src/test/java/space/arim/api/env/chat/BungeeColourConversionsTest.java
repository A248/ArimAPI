/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.PredefinedColour;
import space.arim.api.chat.manipulator.ColourManipulator;

import net.md_5.bungee.api.ChatColor;

public class BungeeColourConversionsTest {

	@Test
	public void testPredefinedColourConversion() {
		for (PredefinedColour colour : PredefinedColour.values()) {
			char codeChar = colour.getCodeChar();
			ChatColor chatColour = ChatColor.getByChar(codeChar);
			assertNotNull(chatColour);
			assertEquals(colour.getColour(), ColourManipulator.getInstance().fromJavaAwt(chatColour.getColor()));
		}
	}
	
}
