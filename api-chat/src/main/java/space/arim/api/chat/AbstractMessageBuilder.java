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

import java.util.ArrayList;
import java.util.List;

abstract class AbstractMessageBuilder implements MessageBuilder {

	final List<Component> components;
	
	AbstractMessageBuilder() {
		components = new ArrayList<>();
	}
	
	AbstractMessageBuilder(List<Component> components) {
		this.components = new ArrayList<>(components);
	}

	@Override
	public String toString() {
		return "AbstractMessageBuilder [components=" + components + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((components == null) ? 0 : components.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractMessageBuilder)) {
			return false;
		}
		AbstractMessageBuilder other = (AbstractMessageBuilder) obj;
		if (!components.equals(other.components)) {
			return false;
		}
		return true;
	}
	
}
