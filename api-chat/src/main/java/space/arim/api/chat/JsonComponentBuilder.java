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
	
	private HoverAction hoverAction;
	private ClickAction clickAction;
	private ShiftClickAction shiftClickAction;
	
	/**
	 * Creates an empty builder
	 * 
	 */
	public JsonComponentBuilder() {
		
	}
	
	/**
	 * Creates a builder with the given content
	 * 
	 * @param text the content
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
	
	@Override
	public HoverAction getHoverAction() {
		return hoverAction;
	}
	
	@Override
	public ClickAction getClickAction() {
		return clickAction;
	}
	
	@Override
	public ShiftClickAction getShiftClickAction() {
		return shiftClickAction;
	}
	
	private void copyJsonAttributes(JsonComponentFramework component) {
		hoverAction = component.getHoverAction();
		clickAction = component.getClickAction();
		shiftClickAction = component.getShiftClickAction();
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
	 * Sets the hover action of this JsonComponentBuilder to the specified action
	 * 
	 * @param hoverAction the hover action
	 * @return the builder
	 */
	public JsonComponentBuilder hoverAction(HoverAction hoverAction) {
		this.hoverAction = hoverAction;
		return this;
	}
	
	/**
	 * Sets the click action of this JsonComponentBuilder to the specified action
	 * 
	 * @param clickAction the click action
	 * @return the builder
	 */
	public JsonComponentBuilder clickAction(ClickAction clickAction) {
		this.clickAction = clickAction;
		return this;
	}
	
	/**
	 * Sets the shift click action of this JsonComponentBuilder to the specified action
	 * 
	 * @param shiftClickAction the shift click action
	 * @return the builder
	 */
	public JsonComponentBuilder shiftClickAction(ShiftClickAction shiftClickAction) {
		this.shiftClickAction = shiftClickAction;
		return this;
	}
	
	/**
	 * Sets the hover action of this JsonComponentBuilder to display the specified tooltip.
	 * 
	 * @param tooltip the tooltip to display
	 * @return the builder
	 */
	public JsonComponentBuilder showTooltip(Message tooltip) {
		return hoverAction(HoverAction.showTooltip(tooltip));
	}
	
	/**
	 * Sets the click action of this JsonComponentBuilder to run the specified command
	 * 
	 * @param command the command to run
	 * @return the builder
	 */
	public JsonComponentBuilder runCommand(String command) {
		return clickAction(ClickAction.runCommand(command));
	}
	
	/**
	 * Sets the click action of this JsonComponentBuilder to suggest the specified command
	 * 
	 * @param command the command to suggest
	 * @return the builder
	 */
	public JsonComponentBuilder suggestCommand(String command) {
		return clickAction(ClickAction.suggestCommand(command));
	}
	
	/**
	 * Sets the click action of this JsonComponentBuilder to open the specified url
	 * 
	 * @param url the url to open
	 * @return the builder
	 */
	public JsonComponentBuilder openUrl(String url) {
		return clickAction(ClickAction.openUrl(url));
	}
	
	/**
	 * Sets the shift click action of this JsonComponentBuilder to insert the specified text
	 * 
	 * @param text the text to insert
	 * @return the builder
	 */
	public JsonComponentBuilder insertText(String text) {
		return shiftClickAction(ShiftClickAction.insertText(text));
	}
	
	/**
	 * Builds this JsonComponentBuilder into a fresh JsonComponent
	 * 
	 * @return a formed JsonComponent
	 */
	@Override
	public JsonComponent build() {
		return new JsonComponent(text, colour, styles, hoverAction, clickAction, shiftClickAction);
	}

	@Override
	public String toString() {
		return "JsonComponentBuilder [hoverAction=" + hoverAction + ", clickAction=" + clickAction
				+ ", shiftClickAction=" + shiftClickAction + ", text=" + text + ", colour=" + colour + ", styles="
				+ styles + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((clickAction == null) ? 0 : clickAction.hashCode());
		result = prime * result + ((hoverAction == null) ? 0 : hoverAction.hashCode());
		result = prime * result + ((shiftClickAction == null) ? 0 : shiftClickAction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj) || !(obj instanceof JsonComponentBuilder)) {
			return false;
		}
		JsonComponentBuilder other = (JsonComponentBuilder) obj;
		if (clickAction == null) {
			if (other.clickAction != null) {
				return false;
			}
		} else if (!clickAction.equals(other.clickAction)) {
			return false;
		}
		if (hoverAction == null) {
			if (other.hoverAction != null) {
				return false;
			}
		} else if (!hoverAction.equals(other.hoverAction)) {
			return false;
		}
		if (shiftClickAction == null) {
			if (other.shiftClickAction != null) {
				return false;
			}
		} else if (!shiftClickAction.equals(other.shiftClickAction)) {
			return false;
		}
		return true;
	}
	
}
