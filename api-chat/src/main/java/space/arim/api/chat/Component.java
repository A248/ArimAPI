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

/**
 * A part of a {@link Message} with a defined {@link Colour} and {@link Style}. <br>
 * <br>
 * Component is immutable; {@link ComponentBuilder} should be used for construction.
 * 
 * @author A248
 *
 */
public class Component implements ComponentFramework {

	private final String text;
	private final Colour colour;
	private final Style[] styles;
	
	Component(String text, Colour colour, Style[] styles) {
		this.text = Objects.requireNonNull(text, "Content of a Component must not be null");
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
	public Style[] getStyles() {
		return styles;
	}
	
	@Override
	public String toString() {
		return toStringMe();
	}
	
	/**
	 * Identical to {@link ComponentBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class Builder extends ComponentBuilder {
		
	}
	
}
