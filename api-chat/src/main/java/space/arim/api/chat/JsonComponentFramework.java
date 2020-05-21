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

/**
 * Contains common documentation for {@link JsonComponent} and {@link JsonComponentBuilder}
 * 
 * @author A248
 *
 */
public interface JsonComponentFramework extends ComponentFramework {

	/**
	 * Gets the hover action of this JsonComponent
	 * 
	 * @return the action or <code>null</code> if none is set
	 */
	HoverAction getHoverAction();
	
	/**
	 * Checks whether a hover action is set
	 * 
	 * @return true if the JsonComponent has a hover action, false otherwise
	 */
	default boolean hasHoverAction() {
		return getHoverAction() != null;
	}
	
	/**
	 * Gets the click action of this JsonComponent
	 * 
	 * @return the action or <code>null</code> if none is set
	 */
	ClickAction getClickAction();
	
	/**
	 * Checks whether a click action is set
	 * 
	 * @return true if the JsonComponent has a click action, false otherwise
	 */
	default boolean hasClickAction() {
		return getClickAction() != null;
	}
	
	/**
	 * Gets the shift click action of this JsonComponent
	 * 
	 * @return the action or <code>null</code> if none is set
	 */
	ShiftClickAction getShiftClickAction();
	
	/**
	 * Checks whether a shift click action is set
	 * 
	 * @return true if the JsonComponent has a shift click action, false otherwise
	 */
	default boolean hasShiftClickAction() {
		return getShiftClickAction() != null;
	}
	
	/**
	 * Checks whether any JSON features are set. <br>
	 * 
	 * @return true if any action is set, false otherwise
	 */
	default boolean hasAnyJsonFeatures() {
		return hasHoverAction() || hasClickAction() || hasShiftClickAction();
	}
	
}
