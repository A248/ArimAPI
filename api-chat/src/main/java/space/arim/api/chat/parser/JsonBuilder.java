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
package space.arim.api.chat.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

class JsonBuilder extends InternalBuilder {

	private final SendableMessage.Builder parentBuilder = new SendableMessage.Builder();
	private final List<TextualComponent.Builder> builders = new ArrayList<>();
	
	@Override
	void addBuilder(TextualComponent.Builder builder) {
		builders.add(builder);
	}
	
	void concretifyCurrent() {
		for (TextualComponent.Builder builder : builders) {
			parentBuilder.add(builder.build());
		}
		builders.clear();
	}
	
	boolean hasAnyCurrent() {
		return builders.isEmpty();
	}
	
	void modifyCurrent(UnaryOperator<TextualComponent.Builder> operator) {
		for (ListIterator<TextualComponent.Builder> it = builders.listIterator(); it.hasNext();) {
			it.set(operator.apply(it.next()));
		}
	}
	
	@Override
	SendableMessage build() {
		return parentBuilder.build();
	}
	
}
