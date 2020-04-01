/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.uuid;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import space.arim.universal.registry.Registrable;

/**
 * A service which maps names to UUIDs and vice versa. <br>
 * Uses names, not display names. <br>
 * <br>
 * This is an improved version of {@link UUIDResolver} which avoids checked exceptions and adds
 * {@link #update(UUID, String, boolean)} to improve the uuid/name cache. <br>
 * <br>
 * The general contract regarding UUID/name resolution is that a name may map to several
 * uuids, but a uuid may only map to a single name. This means the behaviour of this name
 * mapping is explicitly nondeterministic: calling name resolution methods ({@link #resolve(String)}
 * and {@link #resolveImmediately(String)}) may produce different results in the span of a short time.
 * This may happen if the implementation has some weird algorithm for resolution. Or, more likely,
 * a new player may join with the given name after the first call, such that the second call returns
 * the new player's uuid instead. <br>
 * <br>
 * On the contrary, calling uuid resolution methods ({@link #resolve(UUID)} and {@link #resolveImmediately(UUID)})
 * should produce the same name provided no name change has been detected. It is still possible that the player
 * has in fact changed his or her name, but this information is not yet available to the implementation. <br>
 * <br>
 * Implementations are not in any way required to update old mappings via calls to the Mojang API
 * (or Mojang API proxies thereof, e.g. https://github.com/Electroid/mojang-api). Thus, some
 * implementations may continue to store months old mappings. This is a feature, not a bug;
 * it enables plugins to have a wide breadth of information from which to draw on. For example,
 * in punishment plugins, a Moderator might find evidence of a rule breaker a month after the event,
 * and desire to punish such player by their name. Ideally, the name of the player maps to the correct UUID.
 * 
 * @author A248
 *
 */
public interface UUIDResolution extends Registrable {

	/**
	 * Resolves a playername to a UUID. <br>
	 * <br>
	 * Implementations may determine whether to query a database, call the Mojang API, or access
	 * any sort of resources available in order to find a UUID whose corresponding playername
	 * is the specified string.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a completable future which returns a corresponding uuid or <code>null</code> if it did not find one
	 */
	CompletableFuture<UUID> resolve(String name);
	
	/**
	 * Avoids blocking operations and directly resolves the uuid
	 * from a cache. This method should never block. <br>
	 * <br>
	 * Note that it is possible for this method to return <code>null</code> even if calling {@link #resolve(String)}
	 * would return a completablefuture which yields a nonnull uuid, since the other method may run network queries
	 * and database operations at will.
	 * 
	 * @param name the name of the player whose uuid to find
	 * @return a corresponding uuid or <code>null</code> if not found without blocking
	 */
	default UUID resolveImmediately(String name) {
		return resolve(name).getNow(null);
	}
	
	/**
	 * Resolves a UUID to a playername. <br>
	 * <br>
	 * Implementations may determine whether to query a database, call the Mojang API, or access
	 * any sort of resources available in order to find the playername of the player represented
	 * by this UUID.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return a completable future which returns the corresponding playername or <code>null</code> if it did not find one
	 */
	CompletableFuture<String> resolve(UUID uuid);
	
	/**
	 * Avoids blocking operations and directly resolves the name
	 * from a cache. This method should never block. <br>
	 * <br>
	 * Note that it is possible for this method to return <code>null</code> even if calling {@link #resolve(UUID)}
	 * would return a completablefuture which yields a nonnull name, since the other method may run network queries
	 * and database operations at will.
	 * 
	 * @param uuid the uuid of the player whose name to find
	 * @return the corresponding playername or <code>null</code> if not found without blocking
	 */
	default String resolveImmediately(UUID uuid) {
		return resolve(uuid).getNow(null);
	}
	
	/**
	 * Suggests that the given UUID maps to the specified name. <br>
	 * Implementations may choose to ignore this method or to incorporate its
	 * results into their uuid/name storage backend. <br>
	 * <br>
	 * There is no contract of this method. Of course, callers gain nothing
	 * by supplying inaccurate information. <br>
	 * <br>
	 * If the <code>force</code> option is specified, it indicates that the caller
	 * has strong knowledge that the UUID correctly maps to the given name. (For example
	 * if the caller has received information from another server in a BungeeCord network
	 * which has specifically encountered the player with the UUID and name)
	 * 
	 * @param uuid the player uuid
	 * @param name the player's actual name
	 * @param force whether to suggest strongly that the UUID map to the given name
	 */
	void update(UUID uuid, String name, boolean force);
	
}
