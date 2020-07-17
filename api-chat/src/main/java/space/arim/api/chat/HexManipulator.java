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
 * Utility to convert from a hex colour integer to and from other formats.
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
	 * Converts a hex colour into a {@code java.awt.Color}
	 * 
	 * @param colour the hex colour
	 * @return the AWT color
	 */
	public Color toJavaAwt(int colour) {
		return new Color(colour);
	}
	
	/**
	 * Converts a {@code java.awt.Color} into a hex colour. Inverse operation of {@link #toJavaAwt(int)}
	 * 
	 * @param color the AWT color
	 * @return the hex colour
	 */
	public int fromJavaAwt(Color color) {
		int result = (color.getRGB() & 0xFFFFFF);
		assert result > 0 && result < 0xFFFFFF : result;
		return result;
	}
	
	/**
	 * Converts a hex colour into 3 bytes representing the RGB components of the colour. <br>
	 * The bytes may be said to be "unsigned".
	 * 
	 * @param colour the hex colour
	 * @return a byte array, always of length 3
	 */
	public byte[] toBytes(int colour) {
		return new byte[] {(byte) ((colour & 0xFF0000) >> 16), (byte) ((colour & 0x00FF00) >> 8), (byte) (colour & 0x0000FF)};
	}
	
	/**
	 * Converts 3 bytes to a hex colour. Inverse operation of {@link #toBytes(int)}
	 * 
	 * @param red the red RGB component
	 * @param green the green RGB component
	 * @param blue the blue RGB component
	 * @return the hex colour
	 */
	public int fromBytes(byte red, byte green, byte blue) {
		int result = (Byte.toUnsignedInt(red) << 16) | (Byte.toUnsignedInt(green) << 8) | Byte.toUnsignedInt(blue);
		assert result > 0 && result < 0xFFFFFF : result;
		return result;
	}
	
	/**
	 * Converts 3 bytes to a hex colour. Same as {@link #fromBytes(byte, byte, byte)}
	 * except that this takes an array and an offset
	 * 
	 * @param bytes the byte array, must be at least of length offset + 3
	 * @param offset the offset after which to use the next 3 bytes
	 * @return the hex colour
	 */
	public int fromBytes(byte[] bytes, int offset) {
		return fromBytes(bytes[offset], bytes[offset + 1], bytes[offset + 2]);
	}
	
	/**
	 * Converts 3 bytes to a hex colour. Same as {@link #fromBytes(byte, byte, byte)}
	 * except that this takes an array
	 * 
	 * @param bytes the byte array, must be at least of length 3
	 * @return the hex colour
	 */
	public int fromBytes(byte[] bytes) {
		return fromBytes(bytes[0], bytes[1], bytes[2]);
	}
	
	/**
	 * Convenience method to check the range of a hex colour
	 * 
	 * @param colour the hex colour integer
	 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
	 * @return the same hex colour as the input, for further convenience
	 */
	public int checkRange(int colour) {
		checkRange0(colour);
		return colour;
	}
	
	static void checkRange0(int colour) {
		if (colour > 0xFFFFFF || colour < 0) {
			throw new IllegalArgumentException("int " + colour + " is outside the range of a hex colour");
		}
	}
	
}
