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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A fully formed, immutable message
 * 
 * @author A248
 *
 */
public final class SendableMessage implements SendableMessageInfo {

	private final List<TextualComponent> components;
	
	/**
	 * Creates from a list of {@link TextualComponent}s comprising this message. <br>
	 * The list and its elements must be nonnull. An immutable copy is made of it.
	 * 
	 * @param components the components of this message
	 * @throws NullPointerException if {@code components} or an element in it is null
	 */
	public SendableMessage(List<TextualComponent> components) {
		this(components.toArray(new TextualComponent[] {}));
	}
	
	/**
	 * Creates from an array of {@link TextualComponent}s comprising this message. <br>
	 * The array and its elements must be nonnull. An immutable list is made of it.
	 * 
	 * @param components the components of this message
	 * @throws NullPointerException if {@code components} or an element in it is null
	 */
	public SendableMessage(TextualComponent...components) {
		this.components = List.of(components);
	}
	
	@Override
	public List<TextualComponent> getComponents() {
		return components;
	}
	
	/**
	 * Builder to create {@link SendableMessage}
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements SendableMessageInfo {
		
		private List<TextualComponent> components = new ArrayList<>();
		private List<TextualComponent> componentsView;
		
		/**
		 * Creates the builder
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Adds the specified component to this builder.
		 * 
		 * @param component the component, must not be null
		 * @return the builder
		 * @throws NullPointerException if {@code component} is null
		 */
		public Builder add(TextualComponent component) {
			components.add(component);
			return this;
		}
		
		/**
		 * Adds the specified components to this builder. <br>
		 * Neither the array nor any element may be null
		 * 
		 * @param components the component array
		 * @return the builder
		 * @throws NullPointerException if the array or an element is null
		 */
		public Builder add(TextualComponent...components) {
			for (TextualComponent component : components) {
				this.components.add(Objects.requireNonNull(component, "Component must not be null"));
			}
			return this;
		}
		
		/**
		 * Adds the specified components to this builder. <br>
		 * Neither the list nor any element may be null
		 * 
		 * @param components the component list
		 * @return the builder
		 * @throws NullPointerException if the list or an element is null
		 */
		public Builder add(List<TextualComponent> components) {
			for (TextualComponent component : components) {
				this.components.add(Objects.requireNonNull(component, "Component must not be null"));
			}
			return this;
		}
		
		/**
		 * Builds into a full message
		 * 
		 * @return the built message
		 */
		public SendableMessage build() {
			return new SendableMessage(components);
		}

		@Override
		public List<TextualComponent> getComponents() {
			return (componentsView != null) ? componentsView : (componentsView = Collections.unmodifiableList(components));
		}
		
	}
	
}
