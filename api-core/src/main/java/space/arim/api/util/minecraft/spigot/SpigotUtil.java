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
package space.arim.api.util.minecraft.spigot;

import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.util.web.FetcherException;
import space.arim.api.util.web.FetcherUtil;

/**
 * Basic Spigot utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
public final class SpigotUtil {

	private SpigotUtil() {}
	
	/**
	 * Retrieves the latest version of a posted spigot plugin according to its resourceId. <br>
	 * <br>
	 * E.g., for AdvancedBan: <br>
	 * * plugin url = https://www.spigotmc.org/resources/advancedban.8695/ <br>
	 * * resourceId = 8695 <br>
	 * * Code: <code>String latestVersion = SpigotUtil.getLatestSpigotPluginVersion(8695);</code>
	 * 
	 * @param resourceId the spigot plugin id
	 * @return String the latest version
	 * @throws FetcherException if a miscellaneous web problem occured, such as IOException
	 * @throws HttpStatusException if the http status code was not 200
	 */
	public static String getLatestSpigotPluginVersion(int resourceId) throws FetcherException, HttpStatusException {
		return FetcherUtil.getLatestSpigotPluginVersion(resourceId);
	}
	
}
