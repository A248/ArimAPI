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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
		this.components = List.copyOf(components);
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
	
	/**
	 * Creates from {@code SendableMessageInfo}. The components of such info are used
	 * in this one.
	 * 
	 * @param info the message info to use
	 */
	public SendableMessage(SendableMessageInfo info) {
		if (info instanceof SendableMessage) {
			components = info.getComponents();
		} else {
			components = List.copyOf(info.getComponents());
		}
	}
	
	/**
	 * Gets the immutable components which comprise this sendable message. Attempts to mutate
	 * the list will throw {@code UnsupportedOperationException}
	 * 
	 */
	@Override
	public List<TextualComponent> getComponents() {
		return components;
	}
	
	/**
	 * Creates a new {@code SendableMessage} which is the concatenation of this one with a
	 * specified another.
	 * 
	 * @param other the other message to concatenate
	 * @return the combined result
	 */
	public SendableMessage concatenate(SendableMessage other) {
		int ourSize = components.size();
		int size = ourSize + other.components.size();
		TextualComponent[] result = new TextualComponent[size];
		for (int n = 0; n < size; n++) {
			if (n < ourSize) {
				result[n] = components.get(n);
			} else {
				result[n] = other.components.get(n - ourSize);
			}
		}
		return new SendableMessage(result);
	}
	
	@Override
	public String toString() {
		return "SendableMessage [components=" + components + "]";
	}
	
	/*
	 * 
	 * Equals and hashCode implementations are designed to ignore empty components.
	 * This is done because empty components do not affect visual output.
	 * 
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (TextualComponent component : components) {
			if (!component.getText().isEmpty()) {
				result = prime * result + component.hashCode();
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof SendableMessage)) {
			return false;
		}
		ListIterator<TextualComponent> it1 = components.listIterator();
		Iterator<TextualComponent> it2 = ((SendableMessage) object).components.iterator();

		for (;;) {
			final boolean hasNext1 = it1.hasNext();
			final boolean hasNext2 = it2.hasNext();
			if (hasNext1 == hasNext2) {
				if (hasNext1) {
					// Both have another component
					// Skip either component if it is empty
					TextualComponent tc1 = it1.next();
					if (tc1.getText().isEmpty()) {
						continue;
					}
					TextualComponent tc2 = it2.next();
					if (tc2.getText().isEmpty()) {
						// Ensure not to forget the last component
						it1.previous();
						continue;
					}
					// If nonempty components are nonequal, fail
					if (!tc1.equals(tc2)) {
						return false;
					}
				} else {
					// Both ended
					return true;
				}
			} else {
				// Only one of them has another component
				TextualComponent further = (hasNext1) ? it1.next() : it2.next();
				if (!further.getText().isEmpty()) {
					return false;
				}
			}
		}
	}
	
	/**
	 * Converts this {@code SendableMessage} into a legacy message string, using legacy colour codes. <br>
	 * <br>
	 * The colour of each {@code TextualComponent} in this {@code SendableMessage} is converted to the nearest
	 * {@link PredefinedColour}. <br>
	 * <br>
	 * All JSON message content is removed.
	 * 
	 * @param legacyColourCharPrefix the legacy colour code character prefix, e.g. '{@literal &}'
	 * @return a legacy message string
	 */
	public String toLegacyMessageString(char legacyColourCharPrefix) {
		char prefix = legacyColourCharPrefix;
		StringBuilder builder = new StringBuilder();
		for (TextualComponent tc : getComponents()) {
			char code = PredefinedColour.getNearestTo(tc.getColour()).getCodeChar();
			builder.append(prefix).append('r').append(prefix).append(code);
			if (tc.hasStyle(MessageStyle.MAGIC)) {
				builder.append(prefix).append('k');
			}
			if (tc.hasStyle(MessageStyle.BOLD)) {
				builder.append(prefix).append('l');
			}
			if (tc.hasStyle(MessageStyle.STRIKETHROUGH)) {
				builder.append(prefix).append('m');
			}
			if (tc.hasStyle(MessageStyle.UNDERLINE)) {
				builder.append(prefix).append('n');
			}
			if (tc.hasStyle(MessageStyle.ITALIC)) {
				builder.append(prefix).append('o');
			}
			builder.append(tc.getText());
		}
		return builder.toString();
	}

	/**
	 * Builder to create {@link SendableMessage}
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements SendableMessageInfo {
		
		private final List<TextualComponent> components;
		
		/**
		 * Creates the builder
		 * 
		 */
		public Builder() {
			components = new ArrayList<>();
		}
		
		/**
		 * Creates, using the provided existing {@code SendableMessageInfo}, whose components
		 * are copied to this builder
		 * 
		 * @param info the message info to use
		 */
		public Builder(SendableMessageInfo info) {
			components = new ArrayList<>(info.getComponents());
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

		/**
		 * Gets the current components in this builder. The returned list is not guaranteed
		 * to be modifiable.
		 * 
		 */
		@Override
		public List<TextualComponent> getComponents() {
			return components;
		}

		@Override
		public String toString() {
			return "SendableMessage.Builder [components=" + components + "]";
		}
		
	}
	
}
