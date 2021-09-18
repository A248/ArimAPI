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

import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A handle for working with HTTP requests to the Mojang API. <br>
 * <br>
 * Subclassing this is deprecated. Use the {@code create} methods to obtain an instance.
 * 
 * @author A248
 *
 */
public class HttpMojangApi implements RemoteNameHistoryApi {
	
	private static final String FROM_NAME = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String FROM_UUID = "https://api.mojang.com/user/profiles/";
	
	private final HttpClient client;
	
	/**
	 * Creates an instance using a configured http client. <br>
	 * <br>
	 * The http client may be used to specify the connection timeout and the
	 * {@link java.util.concurrent.Executor Executor} used to make completable futures.
	 * 
	 * @param client the http client to use
	 * @deprecated Use {@link #create(HttpClient)} instead. Subclassing is deprecated without replacement.
	 */
	@Deprecated
	public HttpMojangApi(HttpClient client) {
		this.client = client;
	}
	
	/**
	 * Creates an instance using the default http client
	 *
	 * @deprecated Use {@link #create()} instead. Subclassing is deprecated without replacement.
	 */
	@Deprecated
	public HttpMojangApi() {
		this(HttpClient.newHttpClient());
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
	public static HttpMojangApi create(HttpClient client) {
		return new HttpMojangApi(Objects.requireNonNull(client, "client"));
	}

	/**
	 * Creates using the default http client
	 *
	 * @return the instance
	 */
	public static HttpMojangApi create() {
		return create(HttpClient.newHttpClient());
	}

	private <R> CompletableFuture<RemoteApiResult<R>> queryMojangApiReadInputStream(
			URI uri, Function<InputStreamReader, R> readerAcceptorFunction) {
		return new MojangApiRequest(client, uri).queryMojangApi(
				BodyHandlers.ofInputStream(), new BodyReader.OfStreamReader<>(readerAcceptorFunction));
	}

	private URI urlForUUID(UUID uuid) {
		String uuidString = space.arim.omnibus.util.UUIDUtil.toShortString(uuid);
		return URI.create(FROM_UUID + uuidString + "/names");
	}

	private URI urlForName(String name, long timestamp) {
		String url = FROM_NAME + name;
		if (timestamp != -1L) {
			url = url + "?at=" + timestamp;
		}
		return URI.create(url);
	}
	
	private <T> CompletableFuture<RemoteApiResult<T>> lookupByUUID(UUID uuid,
																   Function<Map<String, Object>[], T> resultMapper) {
		return queryMojangApiReadInputStream(urlForUUID(uuid), (reader) -> {
			Map<String, Object>[] nameInfo = DefaultGson.GSON.fromJson(reader,
					new TypeToken<Map<String, Object>[]>() {}.getType());
			return resultMapper.apply(nameInfo);
		});
	}
	
	@Override
	public CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid) {
		Objects.requireNonNull(uuid, "uuid");

		return lookupByUUID(uuid, (nameInfo) -> (String) nameInfo[nameInfo.length - 1].get("name"));
	}

	@Override
	public CompletableFuture<RemoteApiResult<Void>> lookupNameExistence(UUID uuid) {
		Objects.requireNonNull(uuid, "uuid");

		return new MojangApiRequest(client, urlForUUID(uuid)).queryMojangApi(
				BodyHandlers.discarding(), BodyReader.voidReader());
	}

	@Override
	public CompletableFuture<RemoteApiResult<Set<Entry<String, Long>>>> lookupNameHistory(UUID uuid) {
		Objects.requireNonNull(uuid, "uuid");

		return lookupByUUID(uuid, (nameInfo) -> {
			Set<Entry<String, Long>> nameHistory = new HashSet<>();
			for (Map<String, Object> nameChange : nameInfo) {
				nameHistory.add(Map.entry((String) nameChange.get("name"),
						((Number) nameChange.getOrDefault("changedToAt", 0L)).longValue() / 1000L));
			}
			return nameHistory;
		});
	}

	private CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name, long timestamp) {
		Objects.requireNonNull(name, "name");

		return queryMojangApiReadInputStream(urlForName(name, timestamp), (reader) -> {
			Map<String, Object> profileInfo = DefaultGson.GSON.fromJson(reader,
					new TypeToken<Map<String, Object>>() {}.getType());
			String shortUuid = profileInfo.get("id").toString();
			return space.arim.omnibus.util.UUIDUtil.fromShortString(shortUuid);
		});
	}

	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name) {
		return lookupUUID(name, -1L);
	}

	@Override
	public CompletableFuture<RemoteApiResult<Void>> lookupUUIDExistence(String name) {
		Objects.requireNonNull(name, "name");

		return new MojangApiRequest(client, urlForName(name, -1L)).queryMojangApi(
				BodyHandlers.discarding(), BodyReader.voidReader());
	}

	/**
	 * Finds the UUID of the player who had a name at a certain time in the past.
	 *
	 * @param name the name of the player whose uuid to find
	 * @param timestamp the time at which this name was held
	 * @return a future which yields the result containing the uuid
	 * @deprecated This feature is currently broken, possibly intentionally, in the HTTP API endpoint.
	 * It does not seem that it will be fixed, but rather that support will be dropped entirely.
	 * For more information, refer to https://bugs.mojang.com/browse/WEB-3367
	 */
	@Deprecated
	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUIDAtTimestamp(String name, Instant timestamp) {
		return lookupUUID(name, timestamp.getEpochSecond());
	}
	
}
