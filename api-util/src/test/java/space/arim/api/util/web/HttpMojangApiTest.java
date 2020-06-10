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

import java.net.http.HttpClient;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HttpMojangApiTest {

	private HttpMojangApi remote;
	
	@BeforeEach
	public void setup() {
		remote = new HttpMojangApi(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).build());
	}
	
	@Test
	public void testUUIDLookup() {
		RemoteNameUUIDApiTesting.testLookupUUID(remote);
	}
	
	@Test
	public void testNameLookup() {
		RemoteNameUUIDApiTesting.testLookupName(remote);
	}
	
	@Test
	public void testNameHistoryLookup() {
		RemoteNameHistoryApiTesting.testNameHistory(remote);
	}
	
}
