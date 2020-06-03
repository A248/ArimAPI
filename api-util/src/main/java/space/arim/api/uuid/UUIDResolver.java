/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.uuid;

import java.util.UUID;

import space.arim.api.annotation.Blocking;

/**
 * Maps names to UUIDs, or vice versa.
 * 
 * @author A248
 *
 * @deprecated This interface has been long deprecated. {@link UUIDResolution} was its immediate replacement,
 * which has more intuitive method names and does not necessitate throwing exceptions. However, since then,
 * <a href="https://github.com/A248/UUIDVault">UUIDVault</a> has emerged as a complete and improved API
 * for UUID and name resolution. It should be used instead.
 *
 */
@Deprecated
@SuppressWarnings({"deprecation", "removal"})
public interface UUIDResolver {
	
	/**
	 * Use this to get a UUID from a playername
	 * 
	 * <br><br><b>This is a blocking operation if query parameter is true</b>.
	 * If you wish to use querying, run inside an async thread!
	 * 
	 * @param name - the name to be resolved
	 * @param query - whether to check webservers, like the Mojang API
	 * @return the uuid of the corresponding player
	 * @throws PlayerNotFoundException if the name could not be resolved to a uuid
	 */
	@Blocking
	UUID resolveName(String name, boolean query) throws PlayerNotFoundException;
	
	/**
	 * Use this to get a playername from a UUID
	 * 
	 * <br><br><b>This is a blocking operation if query parameter is true</b>.
	 * If you wish to use querying, run inside an async thread!
	 * 
	 * @param uuid - the uuid to be resolved
	 * @param query - whether to check webservers, like the Mojang API
	 * @return the name of the corresponding player
	 * @throws PlayerNotFoundException if the uuid could not be resolved to a name
	 */
	@Blocking
	String resolveUUID(UUID uuid, boolean query) throws PlayerNotFoundException;
	
}
