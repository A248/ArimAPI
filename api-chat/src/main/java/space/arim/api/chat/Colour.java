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
	
	/**
	 * The total amount of colours
	 * 
	 */
	public static final int TOTAL_COLOURS = ColourCatalog.values().length;
	
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
	 * Each Colour is contained within this enum for convenience. <br>
	 * <br>
	 * Using an enum allows operability inside switch statements and annotation values.
	 * 
	 * @author A248
	 *
	 */
	public enum ColourCatalog {
		
		BLACK(Colour.BLACK),
		DARK_BLUE(Colour.DARK_BLUE),
		DARK_GREEN(Colour.DARK_GREEN),
		DARK_AQUA(Colour.DARK_AQUA),
		DARK_RED(Colour.DARK_RED),
		DARK_PURPLE(Colour.DARK_PURPLE),
		GOLD(Colour.GOLD),
		GRAY(Colour.GRAY),
		DARK_GRAY(Colour.DARK_GRAY),
		BLUE(Colour.BLUE),
		GREEN(Colour.GREEN),
		AQUA(Colour.AQUA),
		RED(Colour.RED),
		LIGHT_PURPLE(Colour.LIGHT_PURPLE),
		YELLOW(Colour.YELLOW),
		WHITE(Colour.WHITE);
		
		private final Colour colour;
		
		private ColourCatalog(Colour colour) {
			this.colour = colour;
		}
		
		/**
		 * Gets the {@link Colour} corresponding to this enum entry.
		 * 
		 * @return the colour itself
		 */
		public Colour getColourValue() {
			return colour;
		}
		
		/**
		 * Gets the enum entry corresponding to a {@link Colour}.
		 * 
		 * @param colour the colour itself
		 * @return the enum entry
		 */
		public static ColourCatalog valueOf(Colour colour) {
			if (colour == null) {
				return null;
			}
			for (ColourCatalog entry : ColourCatalog.values()) {
				if (entry.getColourValue().equals(colour)) {
					return entry;
				}
			}
			throw new IllegalStateException("No corresponding enum entry!");
		}
		
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
		for (ColourCatalog colour : ColourCatalog.values()) {
			if (code == colour.getColourValue().identifier()) {
				return colour.getColourValue();
			}
		}
		return null;
	}
	
}
