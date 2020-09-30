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
 * Converts ArimAPI's {@link SendableMessage} from another API's equivalent class {@code T}. This is the
 * complement to {@link PlatformMessageForwardAdapter}
 * 
 * @author A248
 *
 * @param <T> the platform specific type equivalent to {@link SendableMessage}
 */
public interface PlatformMessageReverseAdapter<T> {

	/**
	 * Converts from the platform specific type <i>to</i> ArimAPI.
	 * 
	 * @param message the source message, must not be null
	 * @return an equivalent {@code SendableMessage}, never {@code null}
	 */
	SendableMessage convert(T message);
	
}
