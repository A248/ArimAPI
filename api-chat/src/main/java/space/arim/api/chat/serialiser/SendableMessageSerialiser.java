/* 
 * ArimAPI-chat
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-chat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-chat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-chat. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat.serialiser;

import space.arim.api.chat.SendableMessage;

/**
 * Simple interface for serialisers of {@link SendableMessage}s. <br>
 * <br>
 * The only requirement is that {@link #deserialise(String)} and {@link #serialise(SendableMessage)} are inverse operations;
 * that the result of one fed to the other produces the original input. <br>
 * 
 * <br>
 * All specifics of the serialised format are decided by implementations.
 * 
 * @author A248
 *
 */
public interface SendableMessageSerialiser {

	/**
	 * Deserialises a message based on the specifics of this deserialiser
	 * 
	 * @param message the deserialised format of the message
	 * @return the deserialised message
	 */
	SendableMessage deserialise(String message);
	
	/**
	 * Serialises a message based on the specifics of this serialiser <br>
	 * <br>
	 * If a sendable message created from another implementation is passed, re deserialising
	 * may not produce the same result
	 * 
	 * @param message the message
	 * @return the serialised format of the message
	 */
	String serialise(SendableMessage message);
	
}
