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

import space.arim.universal.util.Stringable;
import space.arim.universal.util.collections.ArraysUtil;
import space.arim.universal.util.collections.CollectionsUtil;

/**
 * Contains common documentation for {@link Component} and {@link ComponentBuilder}
 * 
 * @author A248
 *
 */
public interface ComponentFramework extends Stringable {

	/**
	 * Gets the content of the Component
	 * 
	 * @return the string content, never <code>null</code>
	 */
	String getText();
	
	/**
	 * Gets the colour
	 * 
	 * @return the colour, <code>null</code> indicates not set
	 */
	Colour getColour();
	
	/**
	 * Checks whether a colour is set
	 * 
	 * @return true if the Component has a colour, false otherwise
	 */
	default boolean hasColour() {
		return getColour() != null;
	}
	
	/**
	 * Gets the styles on the Component. <br>
	 * The result is a copy; modifying it will not modify the Component's styles.
	 * 
	 * @return the styles, empty for none set
	 */
	Style[] getStyles();
	
	/**
	 * Checks whether the specified style is set
	 * 
	 * @param style the style to check for
	 * @return true if the style is set, false otherwise
	 */
	default boolean hasStyle(Style style) {
		return CollectionsUtil.checkForAnyMatches(getStyles(), style::equals);
	}
	
	@Override
	default String toStringMe() {
		return "{text:" + getText() + ",colour:" + getColour() + ",style:" + ArraysUtil.toString(getStyles()) + "}";
	}
	
}
