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
 * Information relating to a {@link ChatComponent} or {@link ChatComponent.Builder} <br>
 * <br>
 * Defines a series of attributes regarding a textual message component, but provides no guarantee of immutability
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.chat}
 */
@Deprecated
public interface ChatComponentInfo extends Emptyable {

	/**
	 * Gets the text of this component or builder.
	 * 
	 * @return the text, never {@code null}
	 */
	String getText();
	
	/**
	 * Whether this component or builder is empty
	 * 
	 */
	@Override
	default boolean isEmpty() {
		return getText().isEmpty();
	}
	
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
	
}
