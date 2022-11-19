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
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A handle for working with HTTP requests to the Mojang API.
 * 
 * @author A248
 *
 */
public final class HttpMojangApi extends HttpApiBase implements RemoteNameUUIDApi {
	
	private static final String FROM_NAME = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String FROM_UUID = "https://sessionserver.mojang.com/session/minecraft/profile/";

	private HttpMojangApi(HttpClient client) {
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
	public static HttpMojangApi create(HttpClient client) {
		return new HttpMojangApi(client);
	}

	/**
	 * Creates using the default http client
	 *
	 * @return the instance
	 */
	public static HttpMojangApi create() {
		return create(HttpClient.newHttpClient());
	}

	private HttpRequest requestForUUID(UUID uuid) {
		Objects.requireNonNull(uuid);
		URI uri = URI.create(FROM_UUID + UUIDUtil.toShortString(uuid));
		return HttpRequest.newBuilder()
				.uri(uri)
				.build();
	}

	private HttpRequest requestForName(String name) {
		Objects.requireNonNull(name);
		URI uri = URI.create(FROM_NAME + name);
		return HttpRequest.newBuilder()
				.uri(uri)
				.build();
	}

	@Override
	public CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid) {
		return readJson(requestForUUID(uuid), (json) -> (String) json.get("name"));
	}

	@Override
	public CompletableFuture<RemoteApiResult<Void>> lookupNameExistence(UUID uuid) {
		return readResponse(requestForUUID(uuid), BodyHandlers.discarding(), BodyReader.voidReader());
	}

	@Override
	public CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name) {
		return readJson(requestForName(name), (json) -> UUIDUtil.fromShortString((String) json.get("id")));
	}

	@Override
	public CompletableFuture<RemoteApiResult<Void>> lookupUUIDExistence(String name) {
		return readResponse(requestForName(name), BodyHandlers.discarding(), BodyReader.voidReader());
	}

}
