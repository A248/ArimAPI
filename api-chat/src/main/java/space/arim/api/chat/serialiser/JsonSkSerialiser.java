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
 * Implementation of {@link SendableMessageSerialiser} using the JSON.sk format for parsing Json features, with support for
 * legacy colour codes, and hex colour codes. <br>
 * <br>
 * Hex colour codes are denoted by the pseudo regex <pre>{@literal <#(6*(A-F|0-9))>}</pre>. Furthermore, 3 character hex
 * values are permitted. For example, all of the following are accepted: <br>
 * <pre>
 * {@literal &a, &1, &f}
 * {@literal <#AA00AA>}
 * {@literal <#00FFFF>}
 * {@literal <#0FF>} (equivalent to the previous)
 * </pre>
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public class JsonSkSerialiser implements SendableMessageSerialiser {
	
	private static final JsonSkSerialiser INSTANCE = new JsonSkSerialiser();
	
	private JsonSkSerialiser() {}
	
	/**
	 * Gets the instance
	 * 
	 * @return the instance
	 */
	public static JsonSkSerialiser getInstance() {
		return INSTANCE;
	}
	
	@Override
	public SendableMessage deserialise(String content) {
		return new JsonSkDeserialiserImpl(content).deserialise();
	}

	@Override
	public String serialise(SendableMessage message) {
		return new JsonSkSerialiserImpl(message).serialise();
	}
	
}
