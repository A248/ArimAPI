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

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HexManipulatorTest {

	private HexManipulator manipulator;
	
	@BeforeEach
	public void setup() {
		manipulator = new HexManipulator();
	}
	
	public static int randomHex() {
		return ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
	}
	
	private int randomRgbComponent() {
		return ThreadLocalRandom.current().nextInt(255 + 1);
	}
	
	private Color randomAwtColor() {
		return new Color(randomRgbComponent(), randomRgbComponent(), randomRgbComponent());
	}
	
	private byte[] randomBytes() {
		byte[] bytes = new byte[3];
		ThreadLocalRandom.current().nextBytes(bytes);
		return bytes;
	}
	
	@Test
	public void testToFromJavaAwt() {
		int hex = randomHex();
		Color awtColor = manipulator.toJavaAwt(hex);
		assertEquals(hex, manipulator.fromJavaAwt(awtColor));
	}
	
	@Test
	public void testFromToJavaAwt() {
		Color awtColor = randomAwtColor();
		int hex = manipulator.fromJavaAwt(awtColor);
		assertEquals(awtColor, manipulator.toJavaAwt(hex));
	}
	
	@Test
	public void testToFromBytes() {
		int hex = randomHex();
		byte[] bytes = manipulator.toBytes(hex);
		assertEquals(3, bytes.length);
		assertEquals(hex, manipulator.fromBytes(bytes));
	}
	
	@Test
	public void testFromToBytes() {
		byte[] bytes = randomBytes();
		int hex = manipulator.fromBytes(bytes);
		assertArrayEquals(bytes, manipulator.toBytes(hex));
	}
	
	@Test
	public void testToFromBytesWithOffset() {
		int hex = randomHex();
		int offset = ThreadLocalRandom.current().nextInt(25);
		byte[] bytes = new byte[3 + offset];
		manipulator.toBytes(hex, bytes, offset);
		assertEquals(hex, manipulator.fromBytes(bytes, offset));
	}
	
	@Test
	public void testKnownAwt() {
		Color awtColor = Color.DARK_GRAY; // new Color(64, 64, 64)
		int color = manipulator.fromJavaAwt(awtColor);
		assertEquals(0x404040, color);
		assertEquals(awtColor, manipulator.toJavaAwt(color));
	}
	
	@Test
	public void testKnownBytes() {
		byte[] bytes = new byte[] {(byte) 0x40, (byte) 0x40, (byte) 0x40};
		int color = manipulator.fromBytes(bytes);
		assertEquals(0x404040, color);
		assertArrayEquals(bytes, manipulator.toBytes(color));
	}
	
}
