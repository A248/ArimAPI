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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

interface BodyReader<B, R> {

    RemoteApiResult<R> read(B body);

    static BodyReader<Void, Void> voidReader() {
        return (body) -> RemoteApiResult.foundVoid();
    }

    final class OfStreamReader<R> implements BodyReader<InputStream, R> {

        private final Function<InputStreamReader, R> readerAcceptorFunction;

        OfStreamReader(Function<InputStreamReader, R> readerAcceptorFunction) {
            this.readerAcceptorFunction = readerAcceptorFunction;
        }

        @Override
        public RemoteApiResult<R> read(InputStream body) {
            try (body; InputStreamReader reader = new InputStreamReader(body, StandardCharsets.UTF_8)) {

                return RemoteApiResult.found(readerAcceptorFunction.apply(reader));
            } catch (IOException ex) {
                return RemoteApiResult.error(ex);
            }
        }
    }
}
