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

/**
 * Enumeration of predefined hex colours, corresponding to the legacy colour codes.
 * 
 * @author A248
 *
 */
public enum PredefinedColour {

	BLACK('0', 0x000000),
	DARK_BLUE('1', 0x0000AA),
	DARK_GREEN('2', 0x00AA00),
	DARK_AQUA('3', 0x00AAAA),
	DARK_RED('4', 0xAA0000),
	DARK_PURPLE('5', 0xAA00AA),
	GOLD('6', 0xFFAA00),
	GRAY('7', 0xAAAAAA),
	DARK_GRAY('8', 0x555555),
	BLUE('9', 0x5555FF),
	GREEN('a', 0x55FF55),
	AQUA('b', 0x55FFFF),
	RED('c', 0xFF5555),
	LIGHT_PURPLE('d', 0xFF55FF),
	YELLOW('e', 0xFFFF55),
	WHITE('f', 0xFFFFFF);

	private final char codeChar;
	private final int hex;
	
	private static final PredefinedColour[] VALUES = values();

	private PredefinedColour(char codeChar, int hex) {
		this.codeChar = codeChar;
		this.hex = hex;
	}
	
	/**
	 * Gets the legacy code char of this predefined colour. The lowercase character is returned
	 * 
	 * @return the legacy code character
	 */
	public char getCodeChar() {
		return codeChar;
	}
	
	/**
	 * Gets the hex colour value
	 * 
	 * @return the hex colour
	 */
	public int getHexColour() {
		return hex;
	}
	
	/**
	 * Gets the entry corresponding to the specified legacy code character
	 * 
	 * @param codeChar the legacy code character, case insensitive
	 * @return the corresponding predefined hex colour, or {@code null} if none was found matching the code character
	 */
	public static PredefinedColour getByChar(char codeChar) {
		switch (codeChar) {
		case '0':
			return BLACK;
		case '1':
			return DARK_BLUE;
		case '2':
			return DARK_GREEN;
		case '3':
			return DARK_AQUA;
		case '4':
			return DARK_RED;
		case '5':
			return DARK_PURPLE;
		case '6':
			return GOLD;
		case '7':
			return GRAY;
		case '8':
			return DARK_GRAY;
		case '9':
			return BLUE;
		case 'A':
		case 'a':
			return GREEN;
		case 'B':
		case 'b':
			return AQUA;
		case 'C':
		case 'c':
			return RED;
		case 'D':
		case 'd':
			return LIGHT_PURPLE;
		case 'E':
		case 'e':
			return YELLOW;
		case 'F':
		case 'f':
			return WHITE;
		default:
			return null;
		}
	}
	
	/**
	 * Finds the predefined colour whose hex value exactly matches the specified hex colour.
	 * 
	 * @param hex the hex colour
	 * @return the exact predefined hex colour matching the specified, else {@code null} if there is none
	 */
	public static PredefinedColour getExactTo(int hex) {
		for (PredefinedColour entry : VALUES) {
			if (entry.hex == hex) {
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * Finds the nearest predefined colour to the specified hex colour. Useful for legacy conversion.
	 * 
	 * @param hex the hex colour
	 * @return the nearest predefined hex colour, never {@code null}
	 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
	 */
	public static PredefinedColour getNearestTo(int hex) {
		HexManipulator.checkRange0(hex);

		PredefinedColour exactMatch = getExactTo(hex);
		if (exactMatch != null) {
			return exactMatch;
		}

		PredefinedColour nearest = null;
		double lowestDist = 0;
		for (PredefinedColour entry : VALUES) {

			double dist = distanceSquared(hex, entry.hex);
			if (dist == 0D) {
				return entry;
			}
			if (nearest == null || dist < lowestDist) {
				nearest = entry;
				lowestDist = dist;
			}
		}
		return nearest;
	}
	
	private static double distanceSquared(int hex1, int hex2) {
		if (hex1 == hex2) {
			return 0D;
		}
		// Negatives okay since they'll be squared
		double rdiff = ((hex1 & 0xFF0000) >> 16) - ((hex2 & 0xFF0000) >> 16);
		double gdiff = ((hex1 & 0x00FF00) >> 8) - ((hex2 & 0x00FF00) >> 8);
		double bdiff = (hex1 & 0x0000FF) - (hex2 & 0x0000FF);

		// https://stackoverflow.com/questions/1847092/
		return (Math.pow(rdiff * 0.3, 2) + Math.pow(gdiff * 0.59, 2) + Math.pow(bdiff * 0.11, 2));
	}

}
