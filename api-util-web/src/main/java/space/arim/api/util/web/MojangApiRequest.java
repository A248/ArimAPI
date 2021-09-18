/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

final class MojangApiRequest {

    private final HttpClient client;
    private final URI uri;

    /*
     * When the Mojang API is rate limiting, it returns a 429 status code.
     *
     * When the request was successful but there are no results, it returns a 204.
     *
     */

    private static final int RATE_LIMIT_STATUS_CODE = 429;
    private static final int NOT_FOUND_STATUS_CODE = 204;

    MojangApiRequest(HttpClient client, URI uri) {
        this.client = client;
        this.uri = uri;
    }

    <B, R> CompletableFuture<RemoteApiResult<R>> queryMojangApi(HttpResponse.BodyHandler<B> bodyHandler,
                                                                BodyReader<B, R> bodyReader) {

        HttpRequest request = HttpRequest.newBuilder(uri).build();
        return client.sendAsync(request, bodyHandler).thenApply((response) -> {

            int responseCode = response.statusCode();
            switch (responseCode) {
            case RATE_LIMIT_STATUS_CODE:
                return RemoteApiResult.rateLimited();
            case NOT_FOUND_STATUS_CODE:
                return RemoteApiResult.notFound();
            case 200:
                break;
            default:
                return RemoteApiResult.error(new HttpNon200StatusCodeException(responseCode));
            }
            return bodyReader.read(response.body());
        });
    }
}
