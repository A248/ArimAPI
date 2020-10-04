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

import org.junit.jupiter.api.Test;

import space.arim.api.chat.MessageStyle;

import net.kyori.adventure.text.format.TextDecoration;

public class AdventureColourConversionsTest {

	@Test
	public void testStyleConversion() {
		assertEquals(MessageStyle.MAGIC, AdventureColourConversions.convertDecor(TextDecoration.OBFUSCATED));
		assertEquals(MessageStyle.BOLD, AdventureColourConversions.convertDecor(TextDecoration.BOLD));
		assertEquals(MessageStyle.STRIKETHROUGH, AdventureColourConversions.convertDecor(TextDecoration.STRIKETHROUGH));
		assertEquals(MessageStyle.UNDERLINE, AdventureColourConversions.convertDecor(TextDecoration.UNDERLINED));
		assertEquals(MessageStyle.ITALIC, AdventureColourConversions.convertDecor(TextDecoration.ITALIC));
	}
	
}
