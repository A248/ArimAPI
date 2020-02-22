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

import space.arim.universal.util.collections.ArraysUtil;

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
	
	private static final Format[] values = ArraysUtil.combine(new Format[] {RESET}, Style.values(), Colour.values());
	
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
	 * Gets all formats.
	 * 
	 * @return an array of all formats
	 */
	public static Format[] values() {
		return values;
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
