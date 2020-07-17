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
package space.arim.api.chat;

/**
 * A marker class for some action which runs when a player clicks, hovers, or shift clicks on a JSON message.
 * 
 * @author A248
 *
 */
public abstract class JsonAction {

	JsonAction() {
		// Pseudo-sealed class
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract boolean equals(Object object);
	
	@Override
	public abstract int hashCode();
	
}
