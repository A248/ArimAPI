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

import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link RemoteNameUUIDApi} additionally supporting finding the full name history of a user by their UUID.
 * 
 * @author A248
 *
 */
public interface RemoteNameHistoryApi extends RemoteNameUUIDApi {

	/**
	 * Fetches name history for a UUID, wraps the result and other information in a {@link RemoteApiResult}. <br>
	 * The future will not be null, nor will the result wrapper be null. However, components of the result
	 * may be null. <br>
	 * <br>
	 * The set contained within the result wrapper is a set of entries of strings and longs, the strings
	 * representing player names and the longs the time at which each name was chosen, in unix seconds;
	 * 0 is used for the initial name.
	 * 
	 * @param uuid the uuid of the player whose names to find
	 * @return a completable future, never null, which returns a nonnull api result
	 */
	CompletableFuture<RemoteApiResult<Set<Entry<String, Long>>>> lookupNameHistory(UUID uuid);
	
}
