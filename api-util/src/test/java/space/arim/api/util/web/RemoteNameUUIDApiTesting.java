/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.web;

import java.util.UUID;

import space.arim.api.util.web.RemoteApiResult.ResultType;

import static org.junit.jupiter.api.Assertions.*;

public class RemoteNameUUIDApiTesting {

	private static final String KNOWN_NAME = "A248";
	static final UUID KNOWN_UUID = UUID.fromString("ed5f12cd-6007-45d9-a4b9-940524ddaecf");
	
	private static final String UNKNOWN_NAME = "asygufbhn"; // Who would ever take this name?
	static final UUID UNKNOWN_UUID = UUID.fromString("c003d6d3-6a0b-4a80-890b-dcedf87799b3");
	
	static void testLookupUUID(RemoteNameUUIDApi remote) {
		RemoteApiResult<UUID> knownResult = remote.lookupUUID(KNOWN_NAME).join();
		assertEquals(ResultType.FOUND, knownResult.getResultType());
		assertEquals(KNOWN_UUID, knownResult.getValue());
		assertNull(knownResult.getException());
		
		RemoteApiResult<UUID> unknownResult = remote.lookupUUID(UNKNOWN_NAME).join();
		assertEquals(ResultType.NOT_FOUND, unknownResult.getResultType());
		assertNull(unknownResult.getValue());
		assertNull(unknownResult.getException());
	}
	
	static void testLookupName(RemoteNameUUIDApi remote) {
		RemoteApiResult<String> knownResult = remote.lookupName(KNOWN_UUID).join();
		assertEquals(ResultType.FOUND, knownResult.getResultType());
		assertEquals(KNOWN_NAME, knownResult.getValue());
		assertNull(knownResult.getException());
		
		RemoteApiResult<String> unknownResult = remote.lookupName(UNKNOWN_UUID).join();
		assertEquals(ResultType.NOT_FOUND, unknownResult.getResultType());
		assertNull(unknownResult.getValue());
		assertNull(unknownResult.getException());
	}
	
}
