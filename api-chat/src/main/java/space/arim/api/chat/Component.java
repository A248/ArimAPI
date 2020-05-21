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

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * A part of a {@link Message} with a defined {@link Colour} and {@link Style}. <br>
 * <br>
 * Component is immutable; {@link ComponentBuilder} should be used for construction.
 * 
 * @author A248
 *
 */
public class Component implements ComponentFramework {

	/**
	 * An empty Component
	 * 
	 */
	public static final Component EMPTY = new Component("", null, (Set<Style>) null);
	
	final String text;
	final Colour colour;
	final Style[] styles;
	
	Component(String text, Colour colour, Set<Style> styles) {
		this.text = Objects.requireNonNull(text, "Content of a Component must not be null!");
		this.colour = colour;
		this.styles = Style.fromSetToArray(styles);
	}
	
	private Component(String text, Colour colour, Style[] styles) {
		this.text = Objects.requireNonNull(text, "Content of a Component must not be null!");
		this.colour = colour;
		this.styles = styles;
	}
	
	@Override
	public final String getText() {
		return text;
	}
	
	@Override
	public Colour getColour() {
		return colour;
	}
	
	@Override
	public boolean hasStyle(Style style) {
		for (Style existing : styles) {
			if (existing != null && existing.equals(style)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Creates a new Component with all colour formatting removed
	 * 
	 * @return a fresh Component with colour removed
	 */
	public Component stripColour() {
		return new Component(text, null, styles);
	}
	
	/**
	 * Creates a new Component with all styles formatting removed
	 * 
	 * @return a fresh Component with styles removed
	 */
	public Component stripStyles() {
		return new Component(text, colour, (Set<Style>) null);
	}
	
	/**
	 * Creates a new Component with all colour and styles formatting removed. <br>
	 * 
	 * @return a fresh Component with colour and styles removed
	 */
	public Component stripAll() {
		return new Component(text, null, (Set<Style>) null);
	}
	
	/**
	 * Identical to {@link ComponentBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class Builder extends ComponentBuilder {
		
		/**
		 * Creates an empty builder
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Creates a builder with the given content
		 * 
		 * @param text the content
		 */
		public Builder(String text) {
			super(text);
		}
		
		/**
		 * Creates a builder based on the given Component. <br>
		 * The source Component's information is copied to the Component.Builder.
		 * 
		 * @param component the source component
		 */
		public Builder(ComponentFramework component) {
			super(component);
		}
		
	}
	
	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + Arrays.hashCode(styles);
		result = prime * result + text.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Component) {
			Component other = (Component) object;
			return colour.equals(other.colour) && Arrays.equals(styles, other.styles) && text.equals(other.text);
		}
		return false;
	}*/
	
	@Override
	public String toString() {
		return "Component [text=" + text + ", colour=" + colour + ", styles=" + Arrays.toString(styles) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + Arrays.hashCode(styles);
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Component)) {
			return false;
		}
		Component other = (Component) obj;
		if (colour == null) {
			if (other.colour != null) {
				return false;
			}
		} else if (!colour.equals(other.colour)) {
			return false;
		}
		if (!Arrays.equals(styles, other.styles)) {
			return false;
		}
		if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}
	
}
