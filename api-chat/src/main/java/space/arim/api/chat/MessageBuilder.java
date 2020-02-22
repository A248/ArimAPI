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
 * Contains common documentation for {@link SimpleMessageBuilder} and {@link JsonMessageBuilder}
 * 
 * @author A248
 *
 */
public interface MessageBuilder {

	/**
	 * Adds another Component
	 * 
	 * @param component the next component
	 * @return the builder
	 */
	MessageBuilder append(Component component);
	
	/**
	 * Adds more Components
	 * 
	 * @param components the next components
	 * @return the builder
	 */
	MessageBuilder append(Component...components);
	
	/**
	 * Resets all formatting established. <br>
	 * Any text following with {@link #add(String)} will be unformatted.
	 * 
	 * @return the builder
	 */
	MessageBuilder reset();
	
	/**
	 * Adds the given text, carrying over previous colouring and formatting. <br>
	 * To reset formatting, use {@link #reset()}
	 * 
	 * @param text the content to add
	 * @return the builder
	 */
	MessageBuilder add(String text);
	
	/**
	 * Sets the colour of any future content added with {@link #add(String)}
	 * 
	 * @param colour the colour for following text
	 * @return the builder
	 */
	MessageBuilder colour(Colour colour);
	
	/**
	 * Adds the specified style for any future content
	 * 
	 * @param style the style for following text
	 * @return the builder
	 */
	MessageBuilder style(Style style);
	
	/**
	 * Enables the style if specified, otherwise, disables the style.
	 * 
	 * @param style the style
	 * @param enable whether to toggle the style on or off
	 * @return the builder
	 */
	default MessageBuilder style(Style style, boolean enable) {
		return (enable) ? style(style) : unstyle(style);
	}
	
	/**
	 * Removes the specified style for any future content
	 * 
	 * @param style the style to remove for following text
	 * @return the builder
	 */
	MessageBuilder unstyle(Style style);
	
	/**
	 * Builds into a fresh Message
	 * 
	 * @return a formed Message
	 */
	Message build();
	
}
