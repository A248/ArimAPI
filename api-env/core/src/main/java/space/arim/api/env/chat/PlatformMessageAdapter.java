/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.chat;

import space.arim.api.chat.SendableMessage;

/**
 * Converts ArimAPI's {@link SendableMessage} to and from another API's equivalent class {@code T}. <br>
 * <br>
 * Equivalence is determined by visuals. Serialised forms may not be identical.
 * 
 * @author A248
 *
 * @param <T> the platform specific class equivalent to {@link Message}
 */
public interface PlatformMessageAdapter<T> {
	
	/**
	 * Converts <i>from</i> ArimAPI to the platform specific type.
	 * 
	 * @param message the source message, must not be null
	 * @return an equivalent platform specific result, never {@code null}
	 */
	T convertFrom(SendableMessage message);
	
	/**
	 * Converts from the platform specific type <i>to</i> ArimAPI.
	 * 
	 * @param message the source message, must not be null
	 * @return an equivalent {@code SendableMessage}, never {@code null}
	 */
	SendableMessage convertTo(T message);
	
	/**
	 * Whether this platform supports the functionality of full hex colour support. <br>
	 * <br>
	 * Regardless of this flag, ArimAPI textual objects are converted in a predictable manner to
	 * the platform specific type. If this returns {@code false}, hex colours outside the legacy colour
	 * code enum are converted to the nearest such legacy colour.
	 * 
	 * @return true if full hex colours are supported, false otherwise
	 */
	boolean supportsHexColoursFunctionality();
	
}
