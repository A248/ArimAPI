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

import java.util.ArrayList;
import java.util.List;

import space.arim.universal.util.collections.CollectionsUtil;

/**
 * Helper class for creating Messages.
 * 
 * @author A248
 *
 */
public class MessageBuilder {
	
	private final List<Component> components;
	private ComponentBuilder builder;
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public MessageBuilder() {
		components = new ArrayList<Component>();
	}
	
	/**
	 * Creates a builder with the given components
	 * 
	 * @param components the components
	 */
	public MessageBuilder(List<Component> components) {
		this.components = new ArrayList<Component>(components);
	}
	
	/**
	 * Adds another Component
	 * 
	 * @param component the next component
	 * @return the builder
	 */
	public MessageBuilder append(Component component) {
		reset();
		components.add(component);
		return this;
	}
	
	/**
	 * Adds more Components
	 * 
	 * @param components the next components
	 * @return the builder
	 */
	public MessageBuilder append(Component...components) {
		reset();
		for (Component component : components) {
			this.components.add(component);
		}
		return this;
	}
	
	private ComponentBuilder freshenBuilder() {
		if (builder != null) {
			components.add(builder.build());
			builder = new ComponentBuilder(builder);
		} else {
			builder = new ComponentBuilder();
		}
		return builder;
	}
	
	/**
	 * Resets all formatting established. <br>
	 * Any text following with {@link #add(String)} will be unformatted.
	 * 
	 * @return the builder
	 */
	public MessageBuilder reset() {
		if (builder != null) {
			components.add(builder.build());
			builder = null;
		}
		return this;
	}
	
	/**
	 * Adds the given text, carrying over previous colouring and formatting. <br>
	 * To reset formatting, use {@link #reset()}
	 * 
	 * @param text the content to add
	 * @return the builder
	 */
	public MessageBuilder add(String text) {
		freshenBuilder().text(text);
		return this;
	}
	
	/**
	 * Sets the colour of any future content added with {@link #add(String)}
	 * 
	 * @param colour the colour for following text
	 * @return the builder
	 */
	public MessageBuilder color(Colour colour) {
		if (!builder.getColour().equals(colour)) {
			freshenBuilder().colour(colour);
		}
		return this;
	}
	
	/**
	 * Adds the specified style for any future content
	 * 
	 * @param style the style for following text
	 * @return the builder
	 */
	public MessageBuilder style(Style style) {
		if (!CollectionsUtil.checkForAnyMatches(builder.getStyles(), style::equals)) {
			freshenBuilder().style(style);
		}
		return this;
	}
	
	/**
	 * Removes the specified style for any future content
	 * 
	 * @param style the style to remove for following text
	 * @return the builder
	 */
	public MessageBuilder unstyle(Style style) {
		if (CollectionsUtil.checkForAnyMatches(builder.getStyles(), style::equals)) {
			freshenBuilder().unstyle(style);
		}
		return this;
	}
	
	/**
	 * Builds this MessageBuilder into a fresh Message
	 * 
	 * @return a formed Message
	 */
	public Message build() {
		return new Message(components.toArray(new Component[] {}));
	}
	
}
