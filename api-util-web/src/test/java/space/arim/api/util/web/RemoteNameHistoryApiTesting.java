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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

public abstract class RemoteNameHistoryApiTesting extends RemoteNameUUIDApiTesting {

	private static final Set<Entry<String, Long>> expectedNameHistory;
	
	static {
		expectedNameHistory = Set.of(
				Map.entry("A248_1710", 0L),
				Map.entry("A248", 1427646885L),
				Map.entry("Reqorted", 1460075115L),
				Map.entry("_Aero__", 1462749151L),
				Map.entry("__Aero__", 1468456319L),
				Map.entry("Aerodactyl_", 1498836094L),
				Map.entry("A248", 1574114048L));
	}
	
	@Override
	abstract RemoteNameHistoryApi createInstance();
	
	@Test
	void testNameHistory() {
		RemoteNameHistoryApi remote = (RemoteNameHistoryApi) super.remote;

		RemoteApiResult<Set<Entry<String, Long>>> knownResult = remote.lookupNameHistory(RemoteNameUUIDApiTesting.KNOWN_UUID).join();
		assertFoundAndEquals(expectedNameHistory, knownResult);

		RemoteApiResult<Set<Entry<String, Long>>> unknownResult = remote.lookupNameHistory(RemoteNameUUIDApiTesting.UNKNOWN_UUID).join();
		assertNotFound(unknownResult);
	}
	
}
