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
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

class CraftPlayer_getHandle_Method {

	private static final Method METHOD;
	
	static {
		try {
			String nmsVersion = NMS.VERSION;
			Class<?> playerClazz = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");

			METHOD = playerClazz.getMethod("getHandle");
			METHOD.setAccessible(true);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	private CraftPlayer_getHandle_Method() {}
	
	static Object invoke(Player player) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return METHOD.invoke(player);
	}
	
}
