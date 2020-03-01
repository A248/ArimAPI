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
 * A style is some modification to the figure or shape of the letters following it. <br>
 * <br>
 * Unlike colours, multiple styles may apply to the same {@link Component}.
 * 
 * @author A248
 *
 */
public class Style extends Format {

	public static final Style MAGIC = new Style('k');
	public static final Style BOLD = new Style('l');
	public static final Style STRIKETHROUGH = new Style('m');
	public static final Style UNDERLINE = new Style('n');
	public static final Style ITALIC = new Style('o');
	
	private Style(char code) {
		super(code, true);
	}
	
	/**
	 * Each Style is contained within this enum for convenience. <br>
	 * <br>
	 * Using an enum allows operability inside switch statements and annotation values.
	 * 
	 * @author A248
	 *
	 */
	public enum StyleCatalog {
		
		MAGIC(Style.MAGIC),
		BOLD(Style.BOLD),
		STRIKETHROUGH(Style.STRIKETHROUGH),
		UNDERLINE(Style.UNDERLINE),
		ITALIC(Style.ITALIC);
		
		private final Style style;
		
		private StyleCatalog(Style style) {
			this.style = style;
		}
		
		/**
		 * Gets the {@link Style} corresponding to this enum entry.
		 * 
		 * @return the style itself
		 */
		public Style getStyleValue() {
			return style;
		}
		
		/**
		 * Gets the enum entry corresponding to a {@link Style}.
		 * 
		 * @param style the style itself
		 * @return the enum entry
		 */
		public static StyleCatalog valueOf(Style style) {
			if (style == null) {
				return null;
			}
			for (StyleCatalog entry : StyleCatalog.values()) {
				if (entry.getStyleValue().equals(style)) {
					return entry;
				}
			}
			throw new IllegalStateException("No corresponding enum entry!");
		}
		
	}
	
	/**
	 * Gets a Style from a specific identifier.
	 * 
	 * @param code the character code
	 * @return the style, or <code>null</code> if not found
	 */
	public static Style fromCode(char code) {
		Style style = fromCodeDirect(code);
		return (style != null) ? style : fromCodeDirect(Character.toLowerCase(code));
	}
	
	static Style fromCodeDirect(char code) {
		for (StyleCatalog style : StyleCatalog.values()) {
			if (code == style.getStyleValue().identifier()) {
				return style.getStyleValue();
			}
		}
		return null;
	}
	
}
