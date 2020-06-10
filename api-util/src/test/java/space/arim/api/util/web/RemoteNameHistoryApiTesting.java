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

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import space.arim.api.util.collect.ImmutableEntry;
import space.arim.api.util.web.RemoteApiResult.ResultType;

public class RemoteNameHistoryApiTesting {

	private static final Set<Entry<String, Long>> expectedNameHistory;
	
	static {
		Set<Entry<String, Long>> expected = new HashSet<>();
		expected.add(new ImmutableEntry<>("A248_1710", 0L));
		expected.add(new ImmutableEntry<>("A248", 1427646885L));
		expected.add(new ImmutableEntry<>("Reqorted", 1460075115L));
		expected.add(new ImmutableEntry<>("_Aero__", 1462749151L));
		expected.add(new ImmutableEntry<>("__Aero__", 1468456319L));
		expected.add(new ImmutableEntry<>("Aerodactyl_", 1498836094L));
		expected.add(new ImmutableEntry<>("A248", 1574114048L));
		expectedNameHistory = expected;
	}
	
	static void testNameHistory(RemoteNameHistoryApi remote) {
		RemoteApiResult<Set<Entry<String, Long>>> knownResult = remote.lookupNameHistory(RemoteNameUUIDApiTesting.KNOWN_UUID).join();
		assertEquals(ResultType.FOUND, knownResult.getResultType());
		assertEquals(expectedNameHistory, knownResult.getValue());
		assertNull(knownResult.getException());

		RemoteApiResult<Set<Entry<String, Long>>> unknownResult = remote.lookupNameHistory(RemoteNameUUIDApiTesting.UNKNOWN_UUID).join();
		assertEquals(ResultType.NOT_FOUND, unknownResult.getResultType());
		assertNull(unknownResult.getValue());
		assertNull(unknownResult.getException());
	}
	
}
