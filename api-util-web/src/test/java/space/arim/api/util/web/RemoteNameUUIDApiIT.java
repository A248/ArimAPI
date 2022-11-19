/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
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

package space.arim.api.util.web;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class RemoteNameUUIDApiIT {

	private static final String KNOWN_NAME = "A248";
	static final UUID KNOWN_UUID = UUID.fromString("ed5f12cd-6007-45d9-a4b9-940524ddaecf");
	
	private static final String UNKNOWN_NAME = "asygufbhn"; // Who would ever take this name?
	static final UUID UNKNOWN_UUID = UUID.fromString("c003d6d3-6a0b-4a80-890b-dcedf87799b3");

	private static <T> void assertFoundAndEquals(RemoteNameUUIDApi remote, T expected, RemoteApiResult<T> knownResult) {
		Exception ex = knownResult.getException();
		if (ex != null) {
			fail(ex);
		}
		assertEquals(RemoteApiResult.ResultType.FOUND, knownResult.getResultType(), "Using remote " + remote);
		assertEquals(expected, knownResult.getValue());
	}

	private static void assertNotFound(RemoteNameUUIDApi remote, RemoteApiResult<?> unknownResult) {
		Exception ex = unknownResult.getException();
		if (ex != null) {
			fail(ex);
		}
		assertEquals(RemoteApiResult.ResultType.NOT_FOUND, unknownResult.getResultType(), "Using remote " + remote);
		assertNull(unknownResult.getValue());
	}

	@ParameterizedTest
	@ArgumentsSource(RemoteNameUUIDApiProvider.class)
	public void lookupUUID(RemoteNameUUIDApi remote) {

		assertFoundAndEquals(remote, KNOWN_UUID, remote.lookupUUID(KNOWN_NAME).join());
		assertFoundAndEquals(remote, null, remote.lookupUUIDExistence(KNOWN_NAME).join());

		assertNotFound(remote, remote.lookupUUID(UNKNOWN_NAME).join());
		assertNotFound(remote, remote.lookupUUIDExistence(UNKNOWN_NAME).join());
	}

	@ParameterizedTest
	@ArgumentsSource(RemoteNameUUIDApiProvider.class)
	public void lookupName(RemoteNameUUIDApi remote) {

		assertFoundAndEquals(remote, KNOWN_NAME, remote.lookupName(KNOWN_UUID).join());
		assertFoundAndEquals(remote, null, remote.lookupNameExistence(KNOWN_UUID).join());

		assertNotFound(remote, remote.lookupName(UNKNOWN_UUID).join());
		assertNotFound(remote, remote.lookupNameExistence(UNKNOWN_UUID).join());
	}
	
}
