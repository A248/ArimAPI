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

import java.util.Objects;

import space.arim.universal.util.collections.ArraysUtil;

/**
 * Helper class for creating individual components.
 * 
 * @author A248
 *
 */
public class ComponentBuilder implements ComponentFramework {
	
	private String text = "";
	private Colour colour;
	private Style[] styles = new Style[] {};
	
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
		styles = component.getStyles();
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
	public Style[] getStyles() {
		return ArraysUtil.copy(styles);
	}
	
	/**
	 * Sets the content of this ComponentBuilder to the specified text.
	 * 
	 * @param text the text
	 * @return the builder
	 */
	public ComponentBuilder text(String text) {
		this.text = text;
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
	public ComponentBuilder styles(Style[] styles) {
		this.styles = Objects.requireNonNull(styles, "Styles must not be null!");
		return this;
	}
	
	/**
	 * Enables the specified style, adding it to the list of styles if absent
	 * 
	 * @param style the style
	 * @return the builder
	 */
	public ComponentBuilder style(Style style) {
		styles = ArraysUtil.addIfNotPresent(styles, style);
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
		styles = ArraysUtil.remove(styles, style);
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
		return toStringMe();
	}
	
}
