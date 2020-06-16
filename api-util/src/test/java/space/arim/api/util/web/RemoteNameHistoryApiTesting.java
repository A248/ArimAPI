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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

import space.arim.api.util.web.RemoteApiResult.ResultType;

public abstract class RemoteNameHistoryApiTesting extends RemoteNameUUIDApiTesting {

	private static final Set<Entry<String, Long>> expectedNameHistory;
	
	static {
		Set<Entry<String, Long>> expected = new HashSet<>();
		expected.add(new SimpleImmutableEntry<>("A248_1710", 0L));
		expected.add(new SimpleImmutableEntry<>("A248", 1427646885L));
		expected.add(new SimpleImmutableEntry<>("Reqorted", 1460075115L));
		expected.add(new SimpleImmutableEntry<>("_Aero__", 1462749151L));
		expected.add(new SimpleImmutableEntry<>("__Aero__", 1468456319L));
		expected.add(new SimpleImmutableEntry<>("Aerodactyl_", 1498836094L));
		expected.add(new SimpleImmutableEntry<>("A248", 1574114048L));
		expectedNameHistory = Collections.unmodifiableSet(expected);
	}
	
	@Override
	abstract RemoteNameHistoryApi createInstance();
	
	@Test
	void testNameHistory() {
		RemoteNameHistoryApi remote = (RemoteNameHistoryApi) super.remote;

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
