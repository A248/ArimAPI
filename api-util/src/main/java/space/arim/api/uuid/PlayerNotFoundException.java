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

/**
 * An exception thrown by {@link UUIDResolver} to indicate that no UUID or name was found.
 * 
 * @deprecated UUIDResolver is itself deprecated and superseded by {@link UUIDResolution}
 * 
 * @author A248
 *
 */
@Deprecated
public class PlayerNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1659138619614361712L;
	
	public PlayerNotFoundException(UUID playeruuid) {
		super("Player by uuid " + playeruuid.toString() + " could not be found through the cache, internal Bukkit API, nor external Mojang API.");
	}
	
	public PlayerNotFoundException(String name) {
		super("Player by name " + name + " could not be found through the cache, internal Bukkit API, nor external Mojang API.");
	}
	
	public PlayerNotFoundException(UUID uuid, Exception cause) {
		super("Player by uuid " + uuid.toString() + " could not be found.", cause);
	}
	
	public PlayerNotFoundException(String name, Exception cause) {
		super("Player by name " + name + " could not be found.", cause);
	}

}
