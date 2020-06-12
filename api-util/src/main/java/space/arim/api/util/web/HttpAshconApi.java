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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.gson.Gson;

import space.arim.api.util.web.RemoteApiResult.ResultType;

/**
 * A handle for working with requests to Electroid's Ashcon API.
 * 
 * @author A248
 *
 */
public class HttpAshconApi implements RemoteNameHistoryApi {

	private static final Gson GSON = DefaultGson.GSON;
	
	private static final String URL_BASE = "https://api.ashcon.app/mojang/v2/user/";
	
	private static final int NOT_FOUND_STATUS_CODE = 404;
	
	private final HttpClient client;
	
	/**
	 * Creates an instance using a configured http client. <br>
	 * <br>
	 * The http client may be used to specify the connection timeout and the
	 * {@link java.util.concurrent.Executor Executor} used to make completable futures.
	 * 
	 * @param client the http client to use
	 */
	public HttpAshconApi(HttpClient client) {
		this.client = client;
	}
	
	private <T> CompletableFuture<RemoteApiResult<T>> queryAshconApi(String nameOrUuid,
			Function<Map<String, Object>, T> mapAcceptorFunction) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(URL_BASE + nameOrUuid)).build();
		return client.sendAsync(request, BodyHandlers.ofInputStream()).thenApply((response) -> {

			switch (response.statusCode()) {
			case NOT_FOUND_STATUS_CODE:
				return new RemoteApiResult<>(null, ResultType.NOT_FOUND, null);
			case 200:
				break;
			default:
				return new RemoteApiResult<>(null, ResultType.ERROR, new HttpNon200StatusCodeException());
			}

			InputStream inputStream = response.body();
			try (inputStream; InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

				@SuppressWarnings("unchecked")
				Map<String, Object> map = GSON.fromJson(reader, Map.class);
				return new RemoteApiResult<>(mapAcceptorFunction.apply(map), ResultType.FOUND, null);
			} catch (IOException ex) {
				return new RemoteApiResult<>(null, ResultType.ERROR, ex);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name) {
		Objects.requireNonNull(name, "Name must not be null");

		return queryAshconApi(name, (result) -> UUID.fromString((String) result.get("uuid")));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return queryAshconApi(uuid.toString().replace("-", ""), (result) -> (String) result.get("username"));
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public CompletableFuture<RemoteApiResult<Set<Entry<String, Long>>>> lookupNameHistory(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return queryAshconApi(uuid.toString().replace("-", ""), (result) -> {
			Set<Entry<String, Long>> nameHistory = new HashSet<>();
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> nameInfo = (List<Map<String, Object>>) result.get("username_history");
			for (Map<String, Object> nameChange : nameInfo) {
				nameHistory.add(new SimpleImmutableEntry<>((String) nameChange.get("username"),
						isoDateToUnixSeconds((String) nameChange.get("changed_at"))));
			}
			return nameHistory;
		});
	}
	
	private static final long isoDateToUnixSeconds(String isoDate) {
		if (isoDate == null) {
			return 0L;
		}
		return Instant.parse(isoDate).getEpochSecond();
	}
	
}
