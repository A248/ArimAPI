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
package space.arim.api.platform;

import space.arim.api.chat.Message;

/**
 * Converts ArimAPI's {@link Message} to and from another API's equivalent class <code>T</code>.
 * 
 * @author A248
 *
 * @param <T> the platform specific class equivalent to {@link Message}
 */
public interface PlatformMessageConverter<T> {
	
	/**
	 * Converts <i>from</i> ArimAPI.
	 * 
	 * @param message the source message
	 * @return an equivalent platform specific result
	 */
	T convert(Message message);
	
	/**
	 * Converts <i>to</i> ArimAPI.
	 * 
	 * @param message the source message
	 * @return an equivalent <code>Message</code>
	 */
	Message convert(T message);
	
}
