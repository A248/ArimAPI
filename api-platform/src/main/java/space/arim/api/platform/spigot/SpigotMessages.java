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
package space.arim.api.platform.spigot;

import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.platform.bungee.BungeeMessages;
import space.arim.api.util.LazySingleton;

/**
 * Spigot messages utility. Use {@link #get()} to get the instance. <br>
 * <br>
 * <b>Since Spigot uses the same chat API as BungeeCord, see {@link BungeeMessages} for all documentation.</b>
 * 
 * @author A248
 *
 */
public class SpigotMessages extends BungeeMessages {
	
	private static final LazySingleton<SpigotMessages> INST = new LazySingleton<SpigotMessages>(SpigotMessages::new);
	
	protected SpigotMessages() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static SpigotMessages get() {
		return INST.get();
	}
	
	/**
	 * Shortcut to convert '&' formatting codes to '§' codes. <br>
	 * Equivalent to <code>SpigotMessages.get().transformFormattingCodes(msg, FormattingCodePattern.get(), '§');</code>
	 * 
	 * @param msg the source message
	 * @return the same message with '&' codes translated
	 */
	public String transformFormattingCodes(String msg) {
		return transformFormattingCodes(msg, FormattingCodePattern.get(), '§');
	}
	
}
