/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import space.arim.universal.util.web.HttpStatusException;

import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;
import space.arim.api.uuid.UUIDResolution;

/**
 * Eases implementation of {@link UUIDResolution}. <br>
 * <br>
 * First checks the implementing classes {@link #resolveFromCache(String)} and
 * {@link #resolveFromCache(UUID)}, which should be nonblocking. <br>
 * If that fails, queries the Ashcon API, and then the Mojang API, asynchronously. <br>
 * <br>
 * When a network query returns a completed result, the object will suggest
 * {@link #update(UUID, String, boolean)} to itself with <code>force = true</code>.
 * 
 * @author A248
 *
 */
public abstract class PlatformUUIDResolution implements UUIDResolution {

	/**
	 * Check the server's online/offline players for UUID/name matches.
	 * 
	 * @param name the player's name
	 * @return the uuid or <code>null</code> if not found
	 */
	protected abstract UUID resolveFromCache(String name);
	
	private UUID resolveFromQuery(String name) {
		try {
			UUID uuid = FetcherUtil.ashconApi(name);
			update(uuid, name, true);
			return uuid;
		} catch (FetcherException | HttpStatusException ex) {
		}
		try {
			UUID uuid = FetcherUtil.mojangApi(name);
			update(uuid, name, true);
			return uuid;
		} catch (FetcherException | HttpStatusException ex) {
		}
		return null;
	}
	
	/**
	 * Check the server's online/offline players for UUID/name matches.
	 * 
	 * @param uuid the player's uuid
	 * @return the name or <code>null</code> if not found
	 */
	protected abstract String resolveFromCache(UUID uuid);
	
	private String resolveFromQuery(UUID uuid) {
		try {
			String name = FetcherUtil.ashconApi(uuid);
			update(uuid, name, true);
			return name;
		} catch (FetcherException | HttpStatusException ignored) {}
		try {
			String name = FetcherUtil.mojangApi(uuid);
			update(uuid, name, true);
			return name;
		} catch (FetcherException | HttpStatusException ignored) {}
		return null;
	}
	
	@Override
	public CompletableFuture<UUID> resolve(String name) {
		UUID uuid = resolveFromCache(name);
		return (uuid != null) ? CompletableFuture.completedFuture(uuid)
				: CompletableFuture.supplyAsync(() -> resolveFromQuery(name));
	}
	
	@Override
	public CompletableFuture<String> resolve(UUID uuid) {
		String name = resolveFromCache(uuid);
		return (name != null) ? CompletableFuture.completedFuture(name)
				: CompletableFuture.supplyAsync(() -> resolveFromQuery(uuid));
	}
	
	@Override
	public void update(UUID uuid, String name, boolean force) {
		
	}
	
}
