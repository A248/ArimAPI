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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class PredefinedColourTest {
	
	@ParameterizedTest
	@EnumSource
	public void testByCharIdentity(PredefinedColour colour) {
		char codeChar = colour.getCodeChar();
		assertEquals(colour, PredefinedColour.getByChar(codeChar));
		assertEquals(colour, PredefinedColour.getByChar(Character.toUpperCase(codeChar)));
	}

	@ParameterizedTest
	@EnumSource
	public void testExactToIdentity(PredefinedColour colour) {
		assertEquals(colour, PredefinedColour.getExactTo(colour.getColour()));
	}

	@ParameterizedTest
	@EnumSource
	public void testNearestToIdentity(PredefinedColour colour) {
		assertEquals(colour, PredefinedColour.getNearestTo(colour.getColour()));
	}
	
}
