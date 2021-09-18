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

import static space.arim.api.util.web.RemoteApiResultAssertions.assertFoundAndEquals;
import static space.arim.api.util.web.RemoteApiResultAssertions.assertNotFound;

import java.util.UUID;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class RemoteNameUUIDApiIT {

	private static final String KNOWN_NAME = "A248";
	static final UUID KNOWN_UUID = UUID.fromString("ed5f12cd-6007-45d9-a4b9-940524ddaecf");
	
	private static final String UNKNOWN_NAME = "asygufbhn"; // Who would ever take this name?
	static final UUID UNKNOWN_UUID = UUID.fromString("c003d6d3-6a0b-4a80-890b-dcedf87799b3");
	
	@ParameterizedTest
	@ArgumentsSource(RemoteNameHistoryImplProvider.class)
	public void lookupUUID(RemoteNameUUIDApi remote) {

		assertFoundAndEquals(KNOWN_UUID, remote.lookupUUID(KNOWN_NAME).join());
		assertFoundAndEquals(null, remote.lookupUUIDExistence(KNOWN_NAME).join());

		assertNotFound(remote.lookupUUID(UNKNOWN_NAME).join());
		assertNotFound(remote.lookupUUIDExistence(UNKNOWN_NAME).join());
	}
	
	@ParameterizedTest
	@ArgumentsSource(RemoteNameHistoryImplProvider.class)
	public void lookupName(RemoteNameUUIDApi remote) {

		assertFoundAndEquals(KNOWN_NAME, remote.lookupName(KNOWN_UUID).join());
		assertFoundAndEquals(null, remote.lookupNameExistence(KNOWN_UUID).join());

		assertNotFound(remote.lookupName(UNKNOWN_UUID).join());
		assertNotFound(remote.lookupNameExistence(UNKNOWN_UUID).join());
	}
	
}
