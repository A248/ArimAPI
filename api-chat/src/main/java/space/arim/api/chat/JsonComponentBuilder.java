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
 * Helper class for creating individual json components.
 * 
 * @author A248
 *
 */
public class JsonComponentBuilder extends ComponentBuilder implements JsonComponentFramework {
	
	private Message ttp;
	private String url;
	private String cmd;
	private String sgt;
	private String ins;
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public JsonComponentBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given content
	 * 
	 */
	public JsonComponentBuilder(String text) {
		super(text);
	}
	
	/**
	 * Creates a builder based on the given JsonComponent. <br>
	 * The source JsonComponent's information is copied to the JsonComponentBuilder.
	 * 
	 * @param component the source component
	 */
	public JsonComponentBuilder(JsonComponentFramework component) {
		super(component);
		copyJsonAttributes(component);
	}
	
	/**
	 * Creates a builder based on the given Component. <br>
	 * The source Component's information is copied to the JsonComponentBuilder.
	 * 
	 * @param component the source component
	 */
	public JsonComponentBuilder(ComponentFramework component) {
		super(component);
		if (component instanceof JsonComponentFramework) {
			copyJsonAttributes((JsonComponentFramework) component);
		}
	}
	
	private void copyJsonAttributes(JsonComponentFramework component) {
		ttp = component.getTooltip();
		url = component.getUrl();
		cmd = component.getCommand();
		sgt = component.getSuggestion();
		ins = component.getInsertion();
	}
	
	@Override
	public Message getTooltip() {
		return ttp;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getCommand() {
		return cmd;
	}

	@Override
	public String getSuggestion() {
		return sgt;
	}

	@Override
	public String getInsertion() {
		return ins;
	}
	
	@Override
	public JsonComponentBuilder text(String text) {
		return (JsonComponentBuilder) super.text(text);
	}
	
	@Override
	public JsonComponentBuilder colour(Colour colour) {
		return (JsonComponentBuilder) super.colour(colour);
	}
	
	@Override
	public JsonComponentBuilder styles(Style[] styles) {
		return (JsonComponentBuilder) super.styles(styles);
	}
	
	@Override
	public JsonComponentBuilder style(Style style) {
		return (JsonComponentBuilder) super.style(style);
	}
	
	@Override
	public JsonComponentBuilder style(Style style, boolean enable) {
		return (JsonComponentBuilder) super.style(style, enable);
	}
	
	@Override
	public JsonComponentBuilder unstyle(Style style) {
		return (JsonComponentBuilder) super.unstyle(style);
	}
	
	/**
	 * Sets the tooltip of this JsonComponentBuilder to the specified tooltip.
	 * 
	 * @param ttp the tooltip
	 * @return the builder
	 */
	public JsonComponentBuilder tooltip(Message ttp) {
		this.ttp = ttp.stripJson();
		return this;
	}
	
	/**
	 * Equivalent to {@link #tooltip(Message)}
	 * 
	 * @param ttp the tooltip
	 * @return the builder
	 */
	public JsonComponentBuilder ttp(Message ttp) {
		return tooltip(ttp);
	}
	
	/**
	 * Sets the link of this JsonComponentBuilder to the specified link.
	 * 
	 * @param url the link
	 * @return the builder
	 */
	public JsonComponentBuilder url(String url) {
		this.url = (url.startsWith("https://") || url.startsWith("http://")) ? url : "http://" + url;
		return this;
	}
	
	/**
	 * Equivalent to {@link #url(String)}
	 * 
	 * @param url the link
	 * @return the builder
	 */
	public JsonComponentBuilder hyperlink(String url) {
		return url(url);
	}
	
	/**
	 * Sets the command of this JsonComponentBuilder to the specified command.
	 * 
	 * @param cmd the command
	 * @return the builder
	 */
	public JsonComponentBuilder command(String cmd) {
		this.cmd = cmd;
		return this;
	}
	
	/**
	 * Equivalent to {@link #command(String)}
	 * 
	 * @param cmd the command
	 * @return the builder
	 */
	public JsonComponentBuilder cmd(String cmd) {
		return command(cmd);
	}
	
	/**
	 * Sets the suggestion of this JsonComponentBuilder to the specified suggestion.
	 * 
	 * @param sgt the suggestion
	 * @return the builder
	 */
	public JsonComponentBuilder suggest(String sgt) {
		this.sgt = sgt;
		return this;
	}
	
	/**
	 * Equivalent to {@link #suggest(String)}
	 * 
	 * @param sgt the suggestion
	 * @return the builder
	 */
	public JsonComponentBuilder sgt(String sgt) {
		return suggest(sgt);
	}
	
	/**
	 * Sets the insertion of this JsonComponentBuilder to the specified insertion.
	 * 
	 * @param ins the insertion
	 * @return the builder
	 */
	public JsonComponentBuilder insert(String ins) {
		this.ins = ins;
		return this;
	}
	
	/**
	 * Equivalent to {@link #insert(String)}
	 * 
	 * @param ins the insertion
	 * @return the builder
	 */
	public JsonComponentBuilder ins(String ins) {
		return insert(ins);
	}
	
	/**
	 * Builds this JsonComponentBuilder into a fresh JsonComponent
	 * 
	 * @return a formed JsonComponent
	 */
	@Override
	public JsonComponent build() {
		return new JsonComponent(getText(), getColour(), getStyles(), ttp, url, cmd, sgt, ins);
	}
	
	@Override
	public String toString() {
		return toStringMe();
	}
	
}
