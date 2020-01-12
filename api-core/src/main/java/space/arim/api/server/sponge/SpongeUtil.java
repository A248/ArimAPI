/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.server.sponge;

import org.spongepowered.api.text.Text;

import space.arim.api.annotation.Platform;
import space.arim.api.server.ChatUtil;

/**
 * Basic Sponge utility class with more methods added via contribution.
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.SPONGE)
public final class SpongeUtil {

	private SpongeUtil() {}
	
	/**
	 * Adds color to a message according to '&' color codes. <br>
	 * <b>See {@link ChatUtil#colorSponge(String)}</b> for more information.
	 * 
	 * @param colorable the input string
	 * @return a colored Text object
	 */
	public static Text color(String colorable) {
		return ChatUtil.colorSponge(colorable);
	}
	
	/**
	 * Removes color from a message according to '&' color codes. <br>
	 * See {@link ChatUtil#stripColor(String)} for more information.
	 * 
	 * @param colorable the input string
	 * @return an uncoloured string
	 */
	public static String stripColor(String colorable) {
		return ChatUtil.stripColor(colorable);
	}
	
}
