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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Helper class for creating Messages using JsonComponent.
 * 
 * @author A248
 *
 */
public class JsonMessageBuilder extends AbstractMessageBuilder {

	private final List<ComponentBuilder> builders = new ArrayList<>();
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public JsonMessageBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given components
	 * 
	 * @param components the components
	 */
	public JsonMessageBuilder(List<Component> components) {
		super(components);
	}
	
	@Override
	public JsonMessageBuilder append(Component component) {
		reset();
		components.add(component);
		return this;
	}
	
	@Override
	public JsonMessageBuilder append(Component...components) {
		reset();
		for (Component component : components) {
			this.components.add(component);
		}
		return this;
	}
	
	private ComponentBuilder freshenBuilder() {
		ComponentBuilder builder = new ComponentBuilder();
		builders.add(builder);
		return builder;
	}
	
	private ComponentBuilder currentBuilder() {
		return builders.get(builders.size() - 1);
	}
	
	/**
	 * Resets all formatting, including JSON formatting, established. <br>
	 * Any text following with {@link #add(String)} will be unformatted.
	 * 
	 * @return the builder
	 */
	@Override
	public JsonMessageBuilder reset() {
		builders.forEach((builder) -> components.add(builder.build()));
		builders.clear();
		return this;
	}
	
	@Override
	public JsonMessageBuilder add(String text) {
		freshenBuilder().text(text);
		return this;
	}
	
	@Override
	public JsonMessageBuilder colour(Colour colour) {
		if (builders.isEmpty() || !currentBuilder().getColour().equals(colour)) {
			freshenBuilder().colour(colour);
		}
		return this;
	}
	
	@Override
	public JsonMessageBuilder style(Style style) {
		if (builders.isEmpty() || !currentBuilder().hasStyle(style)) {
			freshenBuilder().style(style);
		}
		return this;
	}
	
	@Override
	public JsonMessageBuilder style(Style style, boolean enable) {
		return (JsonMessageBuilder) super.style(style, enable);
	}
	
	@Override
	public JsonMessageBuilder unstyle(Style style) {
		if (builders.isEmpty() || currentBuilder().hasStyle(style)) {
			freshenBuilder().unstyle(style);
		}
		return this;
	}
	
	private void forEachAsJson(Consumer<JsonComponentBuilder> action) {
		for (int n = 0; n < builders.size(); n++) {
			ComponentBuilder builder = builders.get(n);
			JsonComponentBuilder jsonBuilder;
			if (builder instanceof JsonComponentBuilder) {
				jsonBuilder = (JsonComponentBuilder) builders.get(n);
			} else {
				jsonBuilder = new JsonComponentBuilder(builder);
				builders.set(n, jsonBuilder);
			}
			action.accept(jsonBuilder);
		}
	}
	
	/**
	 * Sets the hover action of the current JSON block to the specified action
	 * 
	 * @param hoverAction the hover action
	 * @return the builder
	 */
	public JsonMessageBuilder hoverAction(HoverAction hoverAction) {
		forEachAsJson((builder) -> builder.hoverAction(hoverAction));
		return this;
	}
	
	/**
	 * Sets the click action of the current JSON block to the specified action
	 * 
	 * @param clickAction the click action
	 * @return the builder
	 */
	public JsonMessageBuilder clickAction(ClickAction clickAction) {
		forEachAsJson((builder) -> builder.clickAction(clickAction));
		return this;
	}
	
	/**
	 * Sets the shift click action of the current JSON block to the specified action
	 * 
	 * @param shiftClickAction the shift click action
	 * @return the builder
	 */
	public JsonMessageBuilder shiftClickAction(ShiftClickAction shiftClickAction) {
		forEachAsJson((builder) -> builder.shiftClickAction(shiftClickAction));
		return this;
	}
	
	/**
	 * Sets the hover action of the current JSON block to display the specified tooltip.
	 * 
	 * @param tooltip the tooltip to display
	 * @return the builder
	 */
	public JsonMessageBuilder showTooltip(Message tooltip) {
		return hoverAction(HoverAction.showTooltip(tooltip));
	}
	
	/**
	 * Sets the click action of the current JSON block to run the specified command
	 * 
	 * @param command the command to run
	 * @return the builder
	 */
	public JsonMessageBuilder runCommand(String command) {
		return clickAction(ClickAction.runCommand(command));
	}
	
	/**
	 * Sets the click action of the current JSON block to suggest the specified command
	 * 
	 * @param command the command to suggest
	 * @return the builder
	 */
	public JsonMessageBuilder suggestCommand(String command) {
		return clickAction(ClickAction.suggestCommand(command));
	}
	
	/**
	 * Sets the click action of the current JSON block to open the specified url
	 * 
	 * @param url the url to open
	 * @return the builder
	 */
	public JsonMessageBuilder openUrl(String url) {
		return clickAction(ClickAction.openUrl(url));
	}
	
	/**
	 * Sets the shift click action of the current JSON block to insert the specified text
	 * 
	 * @param text the text to insert
	 * @return the builder
	 */
	public JsonMessageBuilder insertText(String text) {
		return shiftClickAction(ShiftClickAction.insertText(text));
	}
	
	/**
	 * Builds into a fresh Message, but uses <code>Component</code> objects instead of <code>JsonComponent</code> objects where possible. <br>
	 * <br>
	 * <b>All JsonComponent objects without any JSON features are changed to regular Component objects.</b> <br>
	 * <br>
	 * If built normally with {@link #build}, the result may contain a Component <i>without any</i> tooltips, urls, commands, suggestions, or insertions. <br>
	 * However, programmers may not want Message whose Component array contains JsonComponent objects without any JSON features enabled,
	 * since this conveys the false impression that some JSON features are enabled in the Component (since it is a JsonComponent). <br>
	 * Accordingly, this method is provided to change JsonComponent objects with no JSON in them to regular Component objects prior to building.
	 * 
	 * @return a formed Message with <code>JsonComponent</code> objects converted to regular <code>Component</code> objects where possible
	 */
	public Message cleanBuild() {
		reset();
		int size = components.size();
		Component[] result = new Component[size];
		for (int n = 0; n < size; n++) {
			result[n] = cleanUnusedJson(components.get(n));
		}
		return new Message(result);
	}
	
	private Component cleanUnusedJson(Component component) {
		if (component instanceof JsonComponent) {
			JsonComponent json = (JsonComponent) component;
			if (!json.hasAnyJsonFeatures()) {
				return json.stripJson();
			}
		}
		return component;
	}
	
	@Override
	public Message build() {
		reset();
		return new Message(components.toArray(new Component[] {}));
	}
	
}
