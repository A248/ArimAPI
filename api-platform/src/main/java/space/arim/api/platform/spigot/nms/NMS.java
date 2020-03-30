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
package space.arim.api.platform.spigot.nms;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Global utility for operations which require use of craftbukkit or NMS classes. <br>
 * <br>
 * All features are loaded in a modular fashion via static initialisation. <br>
 * Thus if a method is never called it will do no harm. <br>
 * <br>
 * Once loaded, reflection is cached so as to improve performance. <br>
 * Note that exceptions are passed to the caller so as to give programmers
 * control to decide in which fashion to handle reflection related exceptions.
 * 
 * @author A248
 *
 */
public class NMS {

	static final String VERSION;
	
	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName(); // e.g. "org.bukkit.craftbukkit.v1_8_R3"
		VERSION = packageName.substring("org.bukkit.craftbukkit.".length());
	}
	
	// Prevent instantiation
	private NMS() {}
	
	/**
	 * Gets the global NMS version, such as "v1_8_R3" or "v1_14_R1".
	 * 
	 * @return the runtime nms version
	 */
	public static String getVersion() {
		return VERSION;
	}
	
	/**
	 * Gets a player's ping, that is, the latency between the client and the host.
	 * 
	 * @param player the player
	 * @return the player's ping
	 * @throws InvocationTargetException reflection exception
	 * @throws IllegalAccessException reflection exception
	 * @throws IllegalArgumentException reflection exception
	 */
	public static int getPing(Player player) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return EntityPlayer_ping_Field.invoke(CraftPlayer_getHandle_Method.invoke(player));
	}
	
}
