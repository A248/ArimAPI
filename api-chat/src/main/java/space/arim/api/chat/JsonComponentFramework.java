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

import space.arim.universal.util.collections.ArraysUtil;

/**
 * Contains common documentation for {@link JsonComponent} and {@link JsonComponentBuilder}
 * 
 * @author A248
 *
 */
public interface JsonComponentFramework extends ComponentFramework {

	/**
	 * Gets the tooltip of this JsonComponent
	 * 
	 * @return the tooltip or <code>null</code> if none is set
	 */
	Message getTooltip();
	
	/**
	 * Checks whether a tooltip is set
	 * 
	 * @return true if the JsonComponent has a tooltip, false otherwise
	 */
	default boolean hasTooltip() {
		return getTooltip() != null;
	}
	
	/**
	 * Gets the link of this JsonComponent
	 * 
	 * @return the link or <code>null</code> if none is set
	 */
	String getUrl();
	
	/**
	 * Checks whether a url is set
	 * 
	 * @return true if the JsonComponent has a url, false otherwise
	 */
	default boolean hasUrl() {
		return getUrl() != null;
	}
	
	/**
	 * Gets the command of this JsonComponent
	 * 
	 * @return the command or <code>null</code> if none is set
	 */
	String getCommand();
	
	/**
	 * Checks whether a command is set
	 * 
	 * @return true if the JsonComponent has a command, false otherwise
	 */
	default boolean hasCommand() {
		return getCommand() != null;
	}
	
	/**
	 * Gets the suggestion of this JsonComponent
	 * 
	 * @return the suggestion or <code>null</code> if none is set
	 */
	String getSuggestion();
	
	/**
	 * Checks whether a suggestion is set
	 * 
	 * @return true if the JsonComponent has a suggestion, false otherwise
	 */
	default boolean hasSuggestion() {
		return getSuggestion() != null;
	}
	
	/**
	 * Gets the insertion of this JsonComponent
	 * 
	 * @return the insertion or <code>null</code> if none is set
	 */
	String getInsertion();
	
	/**
	 * Checks whether an insertion is set
	 * 
	 * @return true if the JsonComponent has an insertion, false otherwise
	 */
	default boolean hasInsertion() {
		return getInsertion() != null;
	}
	
	@Override
	default String toStringMe() {
		return "{text:" + getText() + ",colour:" + getColour() + ",style:" + ArraysUtil.toString(getStyles()) + ",ttp:" + getTooltip() + ",url:" + getUrl() + ",cmd:" + getCommand() + ",sgt:" + getSuggestion() + ",ins:" + getInsertion() + "}";
	}
	
}
