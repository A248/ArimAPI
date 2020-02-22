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

	public static final Colour BLACK = new Colour('0', 0x000000);
	public static final Colour DARK_BLUE = new Colour('1', 0x0000AA);
	public static final Colour DARK_GREEN = new Colour('2', 0x00AA00);
	public static final Colour DARK_AQUA = new Colour('3', 0x00AAAA);
	public static final Colour DARK_RED = new Colour('4', 0xAA0000);
	public static final Colour DARK_PURPLE = new Colour('5', 0xAA00AA);
	public static final Colour GOLD = new Colour('6', 0xFFAA00);
	public static final Colour GRAY = new Colour('7', 0xAAAAAA);
	public static final Colour DARK_GRAY = new Colour('8', 0x555555);
	public static final Colour BLUE = new Colour('9', 0x5555FF);
	public static final Colour GREEN = new Colour('a', 0x55FF55);
	public static final Colour AQUA = new Colour('b', 0x55FFFF);
	public static final Colour RED = new Colour('c', 0xFF5555);
	public static final Colour LIGHT_PURPLE = new Colour('d', 0xFF55FF);
	public static final Colour YELLOW = new Colour('e', 0xFFFF55);
	public static final Colour WHITE = new Colour('f', 0xFFFFFF);
	
	private static final Colour[] VALUES = {BLACK, DARK_BLUE, GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};
	
	private final int hex;
	
	private Colour(char code, int hex) {
		super(code, false);
		this.hex = hex;
	}
	
	/**
	 * Gets the actual hexadecimal colour value of this Colour.
	 * 
	 * @return the hex value
	 */
	public int getHex() {
		return hex;
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
		Colour colour = fromCodeDirect(code);
		return (colour != null) ? colour : fromCodeDirect(Character.toLowerCase(code));
	}
	
	static Colour fromCodeDirect(char code) {
		for (Colour colour : values()) {
			if (code == colour.identifier()) {
				return colour;
			}
		}
		return null;
	}
	
}
