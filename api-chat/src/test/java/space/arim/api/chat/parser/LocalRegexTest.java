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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import space.arim.api.chat.parser.LocalRegex.AllColours;
import space.arim.api.chat.parser.LocalRegex.DoublePipes;
import space.arim.api.chat.parser.LocalRegex.LegacyColours;

public class LocalRegexTest {

	@Test
	public void testDoublePipes() {
		assertTrue(DoublePipes.PATTERN.matcher("||").matches());
		assertArrayEquals(new String[] {"first part", "second json node", "third block of text"},
			DoublePipes.PATTERN.split("first part||second json node||third block of text"));
	}
	
	private static void testLegacy(Pattern pattern) {
		assertTrue(pattern.matcher("&a").matches());
		assertTrue(pattern.matcher("&A").matches());
		assertTrue(pattern.matcher("&3").matches());
	}
	
	@Test
	public void testLegacyColours() {
		testLegacy(LegacyColours.PATTERN);
	}
	
	@Test
	public void testAllColours() {
		Pattern pattern = AllColours.PATTERN;
		testLegacy(pattern);
		assertTrue(pattern.matcher("<#00AAFF>").matches());
		assertTrue(pattern.matcher("<#4BC>").matches());
	}
	
}
