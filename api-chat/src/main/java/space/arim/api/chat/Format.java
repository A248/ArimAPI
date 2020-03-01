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
 * Some kind of formatting, whether a {@link Colour} or {@link Style}
 * 
 * @author A248
 *
 */
public class Format {

	/**
	 * The formatting code to reset all further formatting. <br>
	 * <br>
	 * <i>In practice</i>, this is not used in ArimAPI's chat objects; rather, reset codes are applied when
	 * converting to platform specific representations (Bungee and Spigot's BaseComponent, or Sponge's Text). <br>
	 * Accordingly, methods in {@link MessageUtil} do not translate <code>RESET</code> codes. <br>
	 * <br>
	 * <b>As such, programmers should not rely on the presence of this Format in a {@link Message} or {@link Component} for detecting a formatting reset.</b> <br>
	 * On the contrary, it is assumed there is a formatting reset between each <code>Component</code> within a <code>Message</code>.
	 */
	public static final Format RESET = new Format('r', true);
	
	private final char identifier;
	private final boolean style;
	
	Format(char identifier, boolean style) {
		this.identifier = identifier;
		this.style = style;
	}
	
	/**
	 * The unique char identifying this Format.
	 * 
	 * @return
	 */
	public char identifier() {
		return identifier;
	}
	
	/**
	 * Whether the format is a colour.
	 * 
	 * @return true if a colour, false if a style
	 */
	public boolean isColour() {
		return !isStyle();
	}
	
	/**
	 * Whether the format is a style.
	 * 
	 * @return true if a style, false if a colour
	 */
	public boolean isStyle() {
		return style;
	}
	
	/**
	 * Each Format is contained within this enum for convenience. <br>
	 * <br>
	 * Using an enum allows operability inside switch statements and annotation values.
	 * 
	 * @author A248
	 *
	 */
	public enum FormatCatalog {
		
		RESET(Format.RESET),
		
		MAGIC(Style.MAGIC),
		BOLD(Style.BOLD),
		STRIKETHROUGH(Style.STRIKETHROUGH),
		UNDERLINE(Style.UNDERLINE),
		ITALIC(Style.ITALIC),
		
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
		
		private final Format format;
		
		private FormatCatalog(Format format) {
			this.format = format;
		}
		
		/**
		 * Gets the {@link Format} corresponding to this enum entry.
		 * 
		 * @return the format itself
		 */
		public Format getFormatValue() {
			return format;
		}
		
		/**
		 * Gets the enum entry corresponding to a {@link Format}.
		 * 
		 * @param format the format itself
		 * @return the enum entry
		 */
		public static FormatCatalog valueOf(Format format) {
			if (format == null) {
				return null;
			}
			for (FormatCatalog entry : FormatCatalog.values()) {
				if (entry.getFormatValue().equals(format)) {
					return entry;
				}
			}
			throw new IllegalStateException("No corresponding enum entry!");
		}
		
	}
	
	@Override
	public int hashCode() {
		return 31 + identifier;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof Format && identifier == ((Format) object).identifier;
	}
	
	@Override
	public String toString() {
		return String.valueOf(new char[] {'&', identifier});
	}
	
	/**
	 * Gets a Format from a specific identifier.
	 * 
	 * @param code the character code
	 * @return the format, or <code>null</code> if not found
	 */
	public static Format fromCode(char code) {
		Format format = fromCodeDirect(code);
		return (format != null) ? format : fromCodeDirect(Character.toLowerCase(code));
	}
	
	private static Format fromCodeDirect(char code) {
		if (code == RESET.identifier()) {
			return RESET;
		}
		Style style = Style.fromCodeDirect(code);
		return (style != null) ? style : Colour.fromCodeDirect(code);
	}
	
}
