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

import space.arim.omnibus.util.UUIDUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A handle for working with requests to the MCHeads API.
 * 
 * @author A248
 *
 */
public final class HttpMcHeadsApi extends HttpApiBase implements RemoteNameUUIDApi {

	private static final String URL_BASE = "https://mc-heads.net/minecraft/profile/";

	private HttpMcHeadsApi(HttpClient client) {
		super(client);
	}

	/**
	 * Creates using a configured http client. <br>
	 * <br>
	 * The http client may be used to specify the connection timeout and the
	 * {@link java.util.concurrent.Executor Executor} used to make completable futures.
	 *
	 * @param client the http client to use
	 * @return the instance
	 */
	public static HttpMcHeadsApi create(HttpClient client) {
		return new HttpMcHeadsApi(client);
	}

	/**
	 * Creates using the default http client
	 *
	 * @return the instance
	 */
	public static HttpMcHeadsApi create() {
		return create(HttpClient.newHttpClient());
	}

	private <T> CompletableFuture<RemoteApiResult<T>> queryMcHeadsApi(String nameOrUuid,
			Function<Map<String, Object>, T> jsonExtractor) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(URL_BASE + nameOrUuid))
				.setHeader(
						"User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.102 Safari/537.36"
				)
				.build();
		return readJson(request, jsonExtractor);
	}

	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name) {
		Objects.requireNonNull(name, "name");

		return queryMcHeadsApi(name,
				(json) -> UUIDUtil.fromShortString((String) json.get("id")));
	}

	@Override
	public CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid) {
		Objects.requireNonNull(uuid, "uuid");

		String uuidString = UUIDUtil.toShortString(uuid);
		return queryMcHeadsApi(uuidString, (json) -> (String) json.get("name"));
	}

}
