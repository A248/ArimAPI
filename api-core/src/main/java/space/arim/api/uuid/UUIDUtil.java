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
 * * See {@link #expand(String)} for expanding shortened UUIDs
 * 
 * @author A248
 *
 */
public final class UUIDUtil {

	private UUIDUtil() {}
	
	/**
	 * The inverse operation of {@link #expand(String)}
	 * 
	 * @param uuid the uuid to shorten
	 * @return a shortened uuid string
	 */
	public static String shorten(UUID uuid) {
		return uuid.toString().replace("-", "");
	}
	
	/**
	 * Expands a shortened version of a UUID. <br>
	 * <br>
	 * Every UUID has 2 forms. Each form is unique. However, it is simpler to store UUIDs in short form
	 * and expand them into long form when needed. <br>
	 * <br>
	 * Example long form: ed5f12cd-6007-45d9-a4b9-940524ddaecf <br>
	 * Example short form: ed5f12cd600745d9a4b9940524ddaecf <br>
	 * <br>
	 * <b>This method does not parse the UUID. See {@link #expandAndParse(String)} or {@link UUID#fromString(String)} for a full UUID object. </b>
	 * 
	 * @param uuid the string based short uuid
	 * @return the lengthened uuid string
	 */
	public static String expand(String uuid) {
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16)
		+ "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
	
	/**
	 * Equivalent to calling <code>UUID.fromString(UUIDUtil.expand(uuid))</code> <br>
	 * <br>
	 * See {@link #expand(String)} for details
	 * 
	 * @param uuid the short uuid string
	 * @return a parsed UUID
	 */
	public static UUID expandAndParse(String uuid) {
		return UUID.fromString(expand(uuid));
	}
	
}
