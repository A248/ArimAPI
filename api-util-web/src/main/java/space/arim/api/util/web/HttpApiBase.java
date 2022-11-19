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

import com.google.gson.reflect.TypeToken;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

abstract class HttpApiBase {

	private static final int RATE_LIMIT_STATUS_CODE = 429;
	private static final int NOT_FOUND_STATUS_CODE = 404;
	private static final int NO_CONTENT_STATUS_CODE = 204;

	private final HttpClient client;

	HttpApiBase(HttpClient client) {
		this.client = Objects.requireNonNull(client, "client");
	}

	<B, R> CompletableFuture<RemoteApiResult<R>> readResponse(HttpRequest request,
															  HttpResponse.BodyHandler<B> bodyHandler,
															  BodyReader<B, R> bodyReader) {
		return client.sendAsync(request, bodyHandler).thenApply((response) -> {

			int responseCode = response.statusCode();
			switch (responseCode) {
			// Some APIs use 204, some use 404
			// The Mojang API has flip-flopped on which status code to use. So let's support both universally.
			case NOT_FOUND_STATUS_CODE:
			case NO_CONTENT_STATUS_CODE:
				return RemoteApiResult.notFound();
			case RATE_LIMIT_STATUS_CODE:
				return RemoteApiResult.rateLimited();
			case 200:
				break;
			default:
				return RemoteApiResult.error(new HttpNon200StatusCodeException(responseCode));
			}
			return bodyReader.read(response.body());
		});
	}

	<R> CompletableFuture<RemoteApiResult<R>> readJson(HttpRequest request,
													   Function<Map<String, Object>, R> jsonExtractor) {
		var bodyReader = new BodyReader.OfStreamReader<>((reader) -> {
			Map<String, Object> map = DefaultGson.GSON.fromJson(
					reader, new TypeToken<Map<String, Object>>() {
					}.getType()
			);
			return jsonExtractor.apply(map);
		});
		return readResponse(request, HttpResponse.BodyHandlers.ofInputStream(), bodyReader);
	}

}
