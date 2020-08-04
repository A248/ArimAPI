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
	 * Fetches a player name from a uuid, wraps the result and other information in a {@link RemoteApiResult}. <br>
	 * The future will not be null, nor will the result wrapper be null. However, components of the result
	 * may be null.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a completable future, never null, which returns a nonnull api result
	 */
	CompletableFuture<RemoteApiResult<String>> lookupName(UUID uuid);
	
	/**
	 * Fetches a player uuid from a name, wraps the result and other information in a {@link RemoteApiResult}. <br>
	 * The future will not be null, nor will the result wrapper be null. However, components of the result
	 * may be null.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a completable future, never null, which returns a nonnull api result
	 */
	CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name);
	
}
