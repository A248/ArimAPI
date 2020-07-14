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

import java.awt.Color;

/**
 * Utility to convert from hex to and from other formats, particularly RGB.
 * 
 * @author A248
 *
 */
public class HexManipulator {

	/**
	 * Creates an instance
	 * 
	 */
	public HexManipulator() {}
	
	/**
	 * Converts a hex value into a {@code java.awt.Color}
	 * 
	 * @param hex the hex value
	 * @return the AWT color
	 */
	public Color toJavaAwt(int hex) {
		return new Color(hex);
	}
	
	/**
	 * Converts a {@code java.awt.Color} into a hex value. Inverse operation of {@link #toJavaAwt(int)}
	 * 
	 * @param color the AWT color
	 * @return the hex value
	 */
	public int fromJavaAwt(Color color) {
		int result = (color.getRGB() & 0xFFFFFF);
		assert result > 0 && result < 0xFFFFFF : result;
		return result;
	}
	
	/**
	 * Converts a hex value into 3 bytes representing the RGB components of the colour. <br>
	 * The bytes may be said to be "unsigned".
	 * 
	 * @param hex the hex value
	 * @return a byte array, always of length 3
	 */
	public byte[] toBytes(int hex) {
		return new byte[] {(byte) ((hex & 0xFF0000) >> 16), (byte) ((hex & 0x00FF00) >> 8), (byte) (hex & 0x0000FF)};
	}
	
	/**
	 * Converts 3 bytes to a hex value. Inverse operation of {@link #toBytes(int)}
	 * 
	 * @param red the red RGB component
	 * @param green the green RGB component
	 * @param blue the blue RGB component
	 * @return the hex value
	 */
	public int fromBytes(byte red, byte green, byte blue) {
		int result = (Byte.toUnsignedInt(red) << 16) | (Byte.toUnsignedInt(green) << 8) | Byte.toUnsignedInt(blue);
		assert result > 0 && result < 0xFFFFFF : result;
		return result;
	}
	
	/**
	 * Convenience method to check the range of a hex colour
	 * 
	 * @param hex the hex colour integer
	 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
	 * @return the same hex value as the input, for further convenience
	 */
	public int checkRange(int hex) {
		checkRange0(hex);
		return hex;
	}
	
	static void checkRange0(int hex) {
		if (hex > 0xFFFFFF || hex < 0) {
			throw new IllegalArgumentException("int " + hex + " is outside the range of a hex colour");
		}
	}
	
}
