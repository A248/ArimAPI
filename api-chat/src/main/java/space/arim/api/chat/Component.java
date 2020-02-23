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

import space.arim.api.chat.Colour.ColourCatalog;

/**
 * A part of a {@link Message} with a defined {@link Colour} and {@link Style}. <br>
 * <br>
 * Component is immutable; {@link ComponentBuilder} should be used for construction.
 * 
 * @author A248
 *
 */
public class Component implements ComponentFramework {

	final String text;
	final Colour colour;
	final Style[] styles;
	
	Component(String text, Colour colour, Style[] styles) {
		this.text = Objects.requireNonNull(text, "Content of a Component must not be null!");
		this.colour = colour;
		this.styles = Objects.requireNonNull(styles, "Styles of a Component must not be null!");
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
	public Style[] getStyles() {
		return ArraysUtil.copy(styles);
	}
	
	/**
	 * Creates a new Component with all colour formatting removed
	 * 
	 * @return a fresh Component with colour removed
	 */
	public Component stripColour() {
		return new Component(getText(), null, getStyles());
	}
	
	/**
	 * Creates a new Component with all styles formatting removed
	 * 
	 * @return a fresh Component with styles removed
	 */
	public Component stripStyles() {
		return new Component(getText(), getColour(), null);
	}
	
	/**
	 * Creates a new Component with all colour and styles formatting removed. <br>
	 * 
	 * @return a fresh Component with colour and styles removed
	 */
	public Component stripAll() {
		return new Component(getText(), null, null);
	}
	
	/**
	 * Identical to {@link ComponentBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class Builder extends ComponentBuilder {
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + text.hashCode();
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ArraysUtil.unorderedHashCode(styles);
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Component) {
			Component other = (Component) object;
			return text.equals(other.text) && ArraysUtil.unorderedEquals(styles, other.styles) && ColourCatalog.valueOf(colour) == ColourCatalog.valueOf(other.colour);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return toStringMe();
	}
	
}
