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

import space.arim.universal.util.collections.CollectionsUtil;

/**
 * Helper class for creating Messages using JsonComponent.
 * 
 * @author A248
 *
 */
public class MessageJsonBuilder extends AbstractMessageBuilder {

	private final List<ComponentBuilder> builders = new ArrayList<ComponentBuilder>();
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public MessageJsonBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given components
	 * 
	 * @param components the components
	 */
	public MessageJsonBuilder(List<Component> components) {
		super(components);
	}
	
	@Override
	public MessageJsonBuilder append(Component component) {
		reset();
		components.add(component);
		return this;
	}
	
	@Override
	public MessageJsonBuilder append(Component...components) {
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
	public MessageJsonBuilder reset() {
		builders.forEach((builder) -> components.add(builder.build()));
		builders.clear();
		return this;
	}
	
	@Override
	public MessageJsonBuilder add(String text) {
		freshenBuilder().text(text);
		return this;
	}
	
	@Override
	public MessageJsonBuilder colour(Colour colour) {
		if (builders.isEmpty() || !currentBuilder().getColour().equals(colour)) {
			freshenBuilder().colour(colour);
		}
		return this;
	}
	
	@Override
	public MessageJsonBuilder style(Style style) {
		if (builders.isEmpty() || !CollectionsUtil.checkForAnyMatches(currentBuilder().getStyles(), style::equals)) {
			freshenBuilder().style(style);
		}
		return this;
	}
	
	@Override
	public MessageJsonBuilder style(Style style, boolean enable) {
		return (enable) ? style(style) : unstyle(style);
	}
	
	@Override
	public MessageJsonBuilder unstyle(Style style) {
		if (builders.isEmpty() || CollectionsUtil.checkForAnyMatches(currentBuilder().getStyles(), style::equals)) {
			freshenBuilder().unstyle(style);
		}
		return this;
	}
	
	private void forEachJson(Consumer<JsonComponentBuilder> action) {
		for (int n = 0; n < builders.size(); n++) {
			ComponentBuilder builder = builders.get(n);
			if (!(builder instanceof JsonComponentBuilder)) {
				builders.set(n, new JsonComponentBuilder(builder));
			}
			action.accept((JsonComponentBuilder) builders.get(n));
		}
	}
	
	/**
	 * Sets the tooltip of the current JSON block to the specified tooltip.
	 * 
	 * @param ttp the tooltip
	 * @return the builder
	 */
	public MessageJsonBuilder tooltip(Message ttp) {
		forEachJson((builder) -> builder.tooltip(ttp));
		return this;
	}
	
	/**
	 * Equivalent to {@link #tooltip(Message)}
	 * 
	 * @param ttp the tooltip
	 * @return the builder
	 */
	public MessageJsonBuilder ttp(Message ttp) {
		return tooltip(ttp);
	}
	
	/**
	 * Sets the link of the current JSON block to the specified link.
	 * 
	 * @param url the link
	 * @return the builder
	 */
	public MessageJsonBuilder url(String url) {
		forEachJson((builder) -> builder.url(url));
		return this;
	}
	
	/**
	 * Equivalent to {@link #url(String)}
	 * 
	 * @param url the link
	 * @return the builder
	 */
	public MessageJsonBuilder hyperlink(String url) {
		return url(url);
	}
	
	/**
	 * Sets the command of the current JSON block to the specified command.
	 * 
	 * @param cmd the command
	 * @return the builder
	 */
	public MessageJsonBuilder command(String cmd) {
		forEachJson((builder) -> builder.cmd(cmd));
		return this;
	}
	
	/**
	 * Equivalent to {@link #command(String)}
	 * 
	 * @param cmd the command
	 * @return the builder
	 */
	public MessageJsonBuilder cmd(String cmd) {
		return command(cmd);
	}
	
	/**
	 * Sets the suggestion of the current JSON block to the specified suggestion.
	 * 
	 * @param sgt the suggestion
	 * @return the builder
	 */
	public MessageJsonBuilder suggest(String sgt) {
		forEachJson((builder) -> builder.sgt(sgt));
		return this;
	}
	
	/**
	 * Equivalent to {@link #suggest(String)}
	 * 
	 * @param sgt the suggestion
	 * @return the builder
	 */
	public MessageJsonBuilder sgt(String sgt) {
		return suggest(sgt);
	}
	
	/**
	 * Sets the insertion of this JsonComponentBuilder to the specified insertion.
	 * 
	 * @param ins the insertion
	 * @return the builder
	 */
	public MessageJsonBuilder insertion(String ins) {
		forEachJson((builder) -> builder.ins(ins));
		return this;
	}
	
	/**
	 * Equivalent to {@link #insertion(String)}
	 * 
	 * @param ins the insertion
	 * @return the builder
	 */
	public MessageJsonBuilder ins(String ins) {
		return insertion(ins);
	}
	
	@Override
	public Message build() {
		return new Message(components.toArray(new Component[] {}));
	}
	
}
