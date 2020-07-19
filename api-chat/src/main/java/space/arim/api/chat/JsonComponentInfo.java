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
 * Information relating to a {@link JsonComponent} or {@link JsonComponent.Builder}. <br>
 * <br>
 * This interface defines a series of attributes regarding a JSON message, but provide
 * no guarantee of immutability.
 * 
 * @author A248
 *
 */
public interface JsonComponentInfo extends TextualComponentInfo {

	/**
	 * Gets the hover action of this component or builder
	 * 
	 * @return the hover action or {@code null} if there is none
	 */
	JsonHover getHoverAction();
	
	/**
	 * Gets the click action of this component or builder
	 * 
	 * @return the click action or {@code null} if there is none
	 */
	JsonClick getClickAction();
	
	/**
	 * Gets the insertion action of this component or builder
	 * 
	 * @return the insertion action or {@code null} if there is none
	 */
	JsonInsertion getInsertionAction();
	
	/**
	 * Convenience method to determine whether any of the JSON actions of this component or builder
	 * are set. Equivalent to: <br>
	 * <code>getHoverAction() != null || getClickAction() != null || getInsertionAction() != null</code>
	 * 
	 * @return true if any json action is set, false otherwise
	 */
	default boolean hasAnyAction() {
		return getHoverAction() != null || getClickAction() != null || getInsertionAction() != null;
	}

	/**
	 * Provides a string representation of this object, including all of its
	 * properties defined in {@code JsonComponentInfo} and {@code TextualComponentInfo}
	 * 
	 * @return a string representation of this json component info
	 */
	@Override
	String toString();
	
	/**
	 * Determines equality with the specified object consistent with the attributes defined
	 * by this {@code JsonComponentInfo} interface and per such attributes' own {@code equals}
	 * methods.
	 * 
	 * @param object the object to determine equality with
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	boolean equals(Object object);
	
	/**
	 * Determines a hash code for this {@code JsonComponentInfo} consistent with its
	 * attributes defined by this interface.
	 * 
	 * @return a hash code for this json component info
	 */
	@Override
	int hashCode();
	
}
