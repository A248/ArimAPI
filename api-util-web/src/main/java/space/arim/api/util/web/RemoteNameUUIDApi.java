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
	 * Finds the UUID of the player who presently holds a given name.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a future which yields the result containing the uuid
	 */
	CompletableFuture<RemoteApiResult<UUID>> lookupUUID(String name);
	
}
