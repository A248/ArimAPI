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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import space.arim.api.util.web.RemoteApiResult.ResultType;

final class RemoteApiResultAssertions {

	static <T> void assertFoundAndEquals(T expected, RemoteApiResult<T> knownResult) {
		Exception ex = knownResult.getException();
		if (ex != null) {
			fail(ex);
		}
		assertEquals(ResultType.FOUND, knownResult.getResultType());
		assertEquals(expected, knownResult.getValue());
	}
	
	static void assertNotFound(RemoteApiResult<?> unknownResult) {
		Exception ex = unknownResult.getException();
		if (ex != null) {
			fail(ex);
		}
		assertEquals(ResultType.NOT_FOUND, unknownResult.getResultType());
		assertNull(unknownResult.getValue());
	}

}
