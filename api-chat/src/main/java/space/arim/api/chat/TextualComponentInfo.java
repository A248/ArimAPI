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
 * Information relating to a {@link TextualComponent} or {@link TextualComponent.Builder} <br>
 * <br>
 * This interface defines a series of attributes regarding a textual message, but provide
 * no guarantee of immutability.
 * 
 * @author A248
 *
 */
public interface TextualComponentInfo {

	/**
	 * Gets the text of this component or builder.
	 * 
	 * @return the text, never {@code null}
	 */
	String getText();
	
	/**
	 * Gets the hex colour of this component or builder
	 * 
	 * @return the hex colour
	 */
	int getColour();
	
	/**
	 * Gets the styles of this component or builder
	 * 
	 * @return the styles
	 */
	int getStyles();
	
	/**
	 * Convenience method to determine whether this component or builder has the specified style
	 * 
	 * @param style the style
	 * @return true if the style is enabled, false if disabled
	 */
	default boolean hasStyle(int style) {
		return (getStyles() & style) != 0;
	}
	
	/**
	 * Provides a string representation of this object, including all of its
	 * properties defined in {@code TextualComponentInfo}
	 * 
	 * @return a string representation of this textual component info
	 */
	@Override
	String toString();
	
	/**
	 * Determines equality with the specified object consistent with the attributes defined
	 * by this {@code TextualComponentInfo} interface and per such attributes' own {@code equals}
	 * methods.
	 * 
	 * @param object the object to determine equality with
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	boolean equals(Object object);
	
	/**
	 * Determines a hash code for this {@code TextualComponentInfo} consistent with its
	 * attributes defined by this interface.
	 * 
	 * @return a hash code for this textual component info
	 */
	@Override
	int hashCode();
	
}
