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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A general fetcher of UUIDs and names from a remote api or backend, typically http-based.
 * 
 * @author A248
 *
 */
public interface RemoteNameUUIDApi {

	/**
	 * Finds a player's present name from their UUID.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a future which yields the result containing the name
	 */
	CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid);

	/**
	 * Finds whether a player exists with the given uuid. <br>
	 * <br>
	 * This differs from {@link #lookupName(UUID)} in that it benefits from potential
	 * performance optimizations.
	 *
	 * @param uuid the uuid of the player to find
	 * @return a future which yields the result
	 */
	default CompletableFuture<RemoteApiResult<Void>> lookupNameExistence(UUID uuid) {
		return mapExistence(lookupName(uuid));
	}
	
	/**
	 * Finds the UUID of the player who presently holds a given name.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a future which yields the result containing the uuid
	 */
	CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name);

	/**
	 * Finds whether a player exists who presently holds a given name. <br>
	 * <br>
	 * This differs from {@link #lookupUUID(String)} in that it benefits from potential
	 * performance optimizations.
	 *
	 * @param name the name of the player to find
	 * @return a future which yields the result
	 */
	default CompletableFuture<RemoteApiResult<Void>> lookupUUIDExistence(String name) {
		return mapExistence(lookupUUID(name));
	}

	@SuppressWarnings("deprecation")
	private <T> CompletableFuture<RemoteApiResult<Void>> mapExistence(CompletableFuture<RemoteApiResult<T>> futureResult) {
		return futureResult.thenApply((result) -> {
			switch (result.getResultType()) {
			case FOUND:
				return RemoteApiResult.foundVoid();
			case NOT_FOUND:
				return RemoteApiResult.notFound();
			case RATE_LIMITED:
				return RemoteApiResult.rateLimited();
			case ERROR:
				return RemoteApiResult.error(result.getException());
			case UNKNOWN:
				return new RemoteApiResult<>(null, RemoteApiResult.ResultType.UNKNOWN, result.getException());
			default:
				throw new IncompatibleClassChangeError("Did not expect " + result.getResultType());
			}
		});
	}
	
}
