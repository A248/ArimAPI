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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.gson.Gson;

import space.arim.uuidvault.api.UUIDUtil;

import space.arim.api.util.web.RemoteApiResult.ResultType;

/**
 * A handle for working with HTTP requests to the Mojang API.
 * 
 * @author A248
 *
 */
public class HttpMojangApi implements RemoteNameHistoryApi {
	
	private static final Gson GSON = DefaultGson.GSON;
	
	private static final String FROM_NAME = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String FROM_UUID = "https://api.mojang.com/user/profiles/";
	
	/*
	 * When the Mojang API is rate limiting, it returns a 429 status code.
	 * 
	 * When the request was successful but there are no results, it returns a 204.
	 * 
	 */
	
	private static final int RATE_LIMIT_STATUS_CODE = 429;
	private static final int NOT_FOUND_STATUS_CODE = 204;
	
	private final HttpClient client;
	
	/**
	 * Creates an instance using a configured http client. <br>
	 * <br>
	 * The http client may be used to specify the connection timeout and the
	 * {@link java.util.concurrent.Executor Executor} used to make completable futures. <br>
	 * The Mojang API does not currently support HTTP/2, so it is recommended to use HTTP/1.1 for now.
	 * 
	 * @param client the http client to use
	 */
	public HttpMojangApi(HttpClient client) {
		this.client = client;
	}
	
	/**
	 * Creates an instance using the default http client
	 * 
	 */
	public HttpMojangApi() {
		this(HttpClient.newHttpClient());
	}
	
	private <T> CompletableFuture<RemoteApiResult<T>> queryMojangApi(String uri,
			Function<InputStreamReader, T> readerAcceptorFunction) {
		HttpRequest request = HttpRequest.newBuilder(URI.create(uri)).build();
		return client.sendAsync(request, BodyHandlers.ofInputStream()).thenApply((response) -> {

			int responseCode = response.statusCode();
			switch (responseCode) {
			case RATE_LIMIT_STATUS_CODE:
				return new RemoteApiResult<>(null, ResultType.RATE_LIMITED, null);
			case NOT_FOUND_STATUS_CODE:
				return new RemoteApiResult<>(null, ResultType.NOT_FOUND, null);
			case 200:
				break;
			default:
				return new RemoteApiResult<>(null, ResultType.ERROR, new HttpNon200StatusCodeException(responseCode));
			}

			InputStream inputStream = response.body();
			try (inputStream; InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

				return new RemoteApiResult<>(readerAcceptorFunction.apply(reader), ResultType.FOUND, null);
			} catch (IOException ex) {
				return new RemoteApiResult<>(null, ResultType.ERROR, ex);
			}
		});
	}
	
	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name) {
		Objects.requireNonNull(name, "Name must not be null");

		return queryMojangApi(FROM_NAME + name, (reader) -> {
			@SuppressWarnings("unchecked")
			Map<String, Object> profileInfo = GSON.fromJson(reader, Map.class);
			String shortUuid = profileInfo.get("id").toString();
			return UUIDUtil.fromShortString(shortUuid);
		});
	}
	
	private <T> CompletableFuture<RemoteApiResult<T>> lookupByUUID(UUID uuid, Function<Map<String, Object>[], T> resultMapper) {
		return queryMojangApi(FROM_UUID + uuid.toString().replace("-", "") + "/names", (reader) -> {
			@SuppressWarnings("unchecked")
			Map<String, Object>[] nameInfo = GSON.fromJson(reader, Map[].class);
			return resultMapper.apply(nameInfo);
		});
	}
	
	@Override
	public CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return lookupByUUID(uuid, (nameInfo) -> (String) nameInfo[nameInfo.length - 1].get("name"));
	}

	@Override
	public CompletableFuture<RemoteApiResult<Set<Entry<String, Long>>>> lookupNameHistory(UUID uuid) {
		Objects.requireNonNull(uuid, "UUID must not be null");

		return lookupByUUID(uuid, (nameInfo) -> {
			Set<Entry<String, Long>> nameHistory = new HashSet<>();
			for (Map<String, Object> nameChange : nameInfo) {
				nameHistory.add(Map.entry((String) nameChange.get("name"),
						((Number) nameChange.getOrDefault("changedToAt", 0L)).longValue() / 1000L));
			}
			return nameHistory;
		});
	}
	
}
