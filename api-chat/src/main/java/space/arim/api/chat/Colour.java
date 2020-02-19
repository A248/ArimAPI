/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat;

/**
 * A colour determines the colour of all letters following it. <br>
 * <br>
 * Only 1 colour may apply to the same {@link Component}.
 * 
 * @author A248
 *
 */
public class Colour extends Format {

	public static final Colour BLACK = new Colour('0');
	public static final Colour DARK_BLUE = new Colour('1');
	public static final Colour DARK_GREEN = new Colour('2');
	public static final Colour DARK_AQUA = new Colour('3');
	public static final Colour DARK_RED = new Colour('4');
	public static final Colour DARK_PURPLE = new Colour('5');
	public static final Colour GOLD = new Colour('6');
	public static final Colour GRAY = new Colour('7');
	public static final Colour DARK_GRAY = new Colour('8');
	public static final Colour BLUE = new Colour('9');
	public static final Colour GREEN = new Colour('a');
	public static final Colour AQUA = new Colour('b');
	public static final Colour RED = new Colour('c');
	public static final Colour LIGHT_PURPLE = new Colour('d');
	public static final Colour YELLOW = new Colour('e');
	public static final Colour WHITE = new Colour('f');
	
	private static final Colour[] VALUES = {BLACK, DARK_BLUE, GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};
	
	private Colour(char code) {
		super(code, false);
	}
	
	/**
	 * Gets all colours.
	 * 
	 * @return an array of all colours
	 */
	public static Colour[] values() {
		return VALUES;
	}
	
	/**
	 * Gets a Colour from a specific identifier.
	 * 
	 * @param code the character code
	 * @return the colour, or <code>null</code> if not found
	 */
	public static Colour fromCode(char code) {
		for (Colour colour : values()) {
			if (colour.identifier() == code) {
				return colour;
			}
		}
		return null;
	}
	
}
