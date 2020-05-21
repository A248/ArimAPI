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

import java.util.HashSet;
import java.util.Set;

import space.arim.api.chat.Style.StyleCatalog;

/**
 * Helper class for creating individual components.
 * 
 * @author A248
 *
 */
public class ComponentBuilder implements ComponentFramework {

	String text = "";
	Colour colour;
	Set<Style> styles;
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public ComponentBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given content
	 * 
	 * @param text the content
	 */
	public ComponentBuilder(String text) {
		this.text = text;
	}
	
	/**
	 * Creates a builder based on the given Component. <br>
	 * The source Component's information is copied to the Component.Builder.
	 * 
	 * @param component the source component
	 */
	public ComponentBuilder(ComponentFramework component) {
		text = component.getText();
		colour = component.getColour();
		for (Style style : StyleCatalog.directAccessArray) {
			if (component.hasStyle(style)) {
				style(style);
			}
		}
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public Colour getColour() {
		return colour;
	}
	
	@Override
	public boolean hasStyle(Style style) {
		return styles != null && styles.contains(style);
	}
	
	/**
	 * Sets the content of this ComponentBuilder to the specified text.
	 * 
	 * @param text the text
	 * @return the builder
	 */
	public ComponentBuilder text(String text) {
		this.text = (text == null) ? "" : text;
		return this;
	}
	
	/**
	 * Sets the colour of this ComponentBuilder to the specified colour.
	 * 
	 * @param colour the colour
	 * @return the builder
	 */
	public ComponentBuilder colour(Colour colour) {
		this.colour = colour;
		return this;
	}
	
	/**
	 * Sets the styles of this ComponentBuilder to the specified styles.
	 * 
	 * @param styles the styles
	 * @return the builder
	 */
	public ComponentBuilder styles(Set<Style> styles) {
		this.styles = (styles == null || styles.isEmpty()) ? null : new HashSet<>(styles);
		return this;
	}
	
	/**
	 * Enables the specified style, adding it to the list of styles if absent
	 * 
	 * @param style the style
	 * @return the builder
	 */
	public ComponentBuilder style(Style style) {
		if (styles == null) {
			styles = new HashSet<>();
		}
		styles.add(style);
		return this;
	}
	
	/**
	 * Enables the style if specified, otherwise, disables the style.
	 * 
	 * @param style the style
	 * @param enable whether to toggle the style on or off
	 * @return the builder
	 */
	public ComponentBuilder style(Style style, boolean enable) {
		return (enable) ? style(style) : unstyle(style);
	}
	
	/**
	 * Removes the specified style, removing it from the list of styles if present
	 * 
	 * @param style the style
	 * @return the builder
	 */
	public ComponentBuilder unstyle(Style style) {
		if (styles != null && styles.remove(style) && styles.isEmpty()) {
			styles = null;
		}
		return this;
	}
	
	/**
	 * Builds this ComponentBuilder into a fresh Component
	 * 
	 * @return a formed Component
	 */
	public Component build() {
		return new Component(text, colour, styles);
	}

	@Override
	public String toString() {
		return "ComponentBuilder [text=" + text + ", colour=" + colour + ", styles=" + styles + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((styles == null) ? 0 : styles.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComponentBuilder)) {
			return false;
		}
		ComponentBuilder other = (ComponentBuilder) obj;
		if (colour == null) {
			if (other.colour != null) {
				return false;
			}
		} else if (!colour.equals(other.colour)) {
			return false;
		}
		if (styles == null) {
			if (other.styles != null) {
				return false;
			}
		} else if (!styles.equals(other.styles)) {
			return false;
		}
		if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}
	
}
