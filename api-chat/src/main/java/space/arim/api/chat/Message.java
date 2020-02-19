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
package space.arim.api.chat;

import space.arim.universal.util.collections.ArraysUtil;

/**
 * A sendable message comprised of an array of {@link Component} or {@link JsonComponent} objects
 * 
 * @author A248
 *
 */
public class Message {

	private final Component[] components;
	
	public Message(Component...components) {
		this.components = components;
	}
	
	/**
	 * Removes any JSON formatting from this message
	 * 
	 * @return a fresh Message with JSON removed
	 */
	public Message stripJson() {
		Component[] stripped = new Component[] {};
		for (int n = 0; n < components.length; n++) {
			stripped[n] = (components[n] instanceof JsonComponent) ? ((JsonComponent) components[n]).stripJson() : components[n];
		}
		return new Message(stripped);
	}
	
	/**
	 * Identical to {@link MessageBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class Builder extends MessageBuilder {
		
	}
	
	/**
	 * Identical to {@link MessageJsonBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class JsonBuilder extends MessageJsonBuilder {
		
	}
	
	@Override
	public String toString() {
		return ArraysUtil.toString(components);
	}
	
}
