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
package space.arim.api.chat.manipulator;

import java.awt.Color;

/**
 * Utility to convert from a hex colour integer to and from other formats. <br>
 * <br>
 * Both {@link #getInstance()} and the constructor may be used.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}. In addition to that,
 * this class is terminally deprecated because it introduces a dependency on
 * the {@code java.desktop} module due to usage of {@code java.awt.Color}
 */
@Deprecated(forRemoval = true)
public class ColourManipulator {

	private static final ColourManipulator INSTANCE = new ColourManipulator();
	
	/**
	 * Creates an instance
	 * 
	 */
	public ColourManipulator() {}
	
	/**
	 * Gets the common instance
	 * 
	 * @return the shared instance
	 */
	public static ColourManipulator getInstance() {
		return INSTANCE;
	}
	
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
		assert isInRange(result) : result;
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
		byte[] result = new byte[3];
		toBytes(colour, result, 0);
		return result;
	}
	
	/**
	 * Converts a hex colour into 3 bytes representing the RGB components of the colour. <br>
	 * The bytes may be said to be "unsigned". <br>
	 * <br>
	 * Unlike {@link #toBytes(int)}, this method permits the bytes to be placed into an array
	 * of the caller's choice at whichever start index desired.
	 * 
	 * @param colour the hex colour
	 * @param bytes the array to which to write the resulting bytes, must be of length (3 + offset)
	 * @param offset the index at which to begin writing bytes to the array
	 */
	public void toBytes(int colour, byte[] bytes, int offset) {
		bytes[offset] = (byte) ((colour & 0xFF0000) >> 16);
		bytes[offset + 1] = (byte) ((colour & 0x00FF00) >> 8);
		bytes[offset + 2] = (byte) (colour & 0x0000FF);
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
		assert isInRange(result) : result;
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
	 * Converts 3 bytes to a colour. Same as {@link #fromBytes(byte, byte, byte)}
	 * except that this takes an array
	 * 
	 * @param bytes the byte array, must be at least of length 3
	 * @return the hex colour
	 */
	public int fromBytes(byte[] bytes) {
		return fromBytes(bytes, 0);
	}
	
	/**
	 * Checks the range of a hex colour
	 * 
	 * @param colour the hex colour integer
	 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
	 * @return the same hex colour as the input, for convenience
	 */
	public int checkRange(int colour) {
		if (!isInRange(colour)) {
			throw new IllegalArgumentException("int " + colour + " is outside the range of a hex colour");
		}
		return colour;
	}
	
	/**
	 * Determines whether the colour is in the range of a hex colour
	 * 
	 * @param colour the hex colour integer
	 * @return true if in range, false otherwise
	 */
	public boolean isInRange(int colour) {
		return colour >= 0 && colour <= 0xFFFFFF;
	}
	
}
