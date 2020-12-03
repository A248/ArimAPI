/* 
 * ArimAPI-util-web
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util-web. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

public class UUIDUtilTest {

	private final UUID uuid = UUID.randomUUID();

	@Test
	public void testToFromByteArray() {
		assertEquals(uuid, UUIDUtil.fromByteArray(UUIDUtil.toByteArray(uuid)));
	}

	@Test
	public void testToBytesLength() {
		assertEquals(16, UUIDUtil.toByteArray(uuid).length);
	}

	@Test
	public void testToBytesOffset() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int bufferSize = random.nextInt(16, 128);
		byte[] bytes = new byte[bufferSize];
		int offset = random.nextInt(bufferSize - 16);

		UUIDUtil.toByteArray(uuid, bytes, offset);
		assertEquals(uuid, UUIDUtil.fromByteArray(bytes, offset));
	}

	@Test
	public void testToFromShortString() {
		assertEquals(uuid, UUIDUtil.fromShortString(UUIDUtil.toShortString(uuid)));
	}

	@Test
	public void testShortStringLength() {
		assertEquals(32, UUIDUtil.toShortString(uuid).length());
	}

	@Test
	public void testContractExpandShortString() {
		String fullUuid = uuid.toString();
		String shortUuid = UUIDUtil.contractFullString(fullUuid);

		assertEquals(fullUuid.replace("-", ""), shortUuid);
		assertEquals(fullUuid, UUIDUtil.expandShortString(shortUuid));
		assertEquals(32, shortUuid.length());
		assertEquals(uuid, UUIDUtil.fromShortString(shortUuid));
	}

}
