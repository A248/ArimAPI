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

import java.util.Set;

/**
 * A specific kind of {@link Component} with JSON support. <br>
 * <br>
 * JsonComponent is immutable; {@link JsonComponentBuilder} should be used for construction.
 * 
 * @author A248
 *
 */
public class JsonComponent extends Component implements JsonComponentFramework {

	private final HoverAction hoverAction;
	private final ClickAction clickAction;
	private final ShiftClickAction shiftClickAction;
	
	JsonComponent(String text, Colour colour, Set<Style> styles, HoverAction hoverAction, ClickAction clickAction, ShiftClickAction shiftClickAction) {
		super(text, colour, styles);
		this.hoverAction = hoverAction;
		this.clickAction = clickAction;
		this.shiftClickAction = shiftClickAction;
	}
	
	private JsonComponent(String text, Colour colour, Style[] styles, HoverAction hoverAction, ClickAction clickAction, ShiftClickAction shiftClickAction) {
		super(text, colour, styles);
		this.hoverAction = hoverAction;
		this.clickAction = clickAction;
		this.shiftClickAction = shiftClickAction;
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
	
	/**
	 * Creates a new Component with all colour formatting removed. <br>
	 * JSON formatting is carried over.
	 * 
	 * @return a fresh Component with colour removed
	 */
	@Override
	public JsonComponent stripColour() {
		return new JsonComponent(text, null, styles, hoverAction, clickAction, shiftClickAction);
	}
	
	/**
	 * Creates a new Component with all styles formatting removed. <br>
	 * JSON formatting is carried over.
	 * 
	 * @return a fresh Component with styles removed
	 */
	@Override
	public JsonComponent stripStyles() {
		return new JsonComponent(text, colour, (Set<Style>) null, hoverAction, clickAction, shiftClickAction);
	}
	
	/**
	 * Creates a new Component with all JSON formatting removed
	 * 
	 * @return a fresh Component with JSON removed
	 */
	public Component stripJson() {
		return new Component(text, colour, styles);
	}
	
	/**
	 * Creates a new Component with all colour, styles, and JSON formatting removed. <br>
	 * 
	 * @return a fresh Component with colour, styles, and JSON removed
	 */
	@Override
	public Component stripAll() {
		return stripJson().stripAll();
	}
	
	/**
	 * Identical to {@link JsonComponentBuilder}
	 * 
	 * @author A248
	 *
	 */
	public class Builder extends JsonComponentBuilder {
		
		/**
		 * Creates an empty builder
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Creates a builder with the given content
		 * 
		 * @param text the content
		 */
		public Builder(String text) {
			super(text);
		}
		
		/**
		 * Creates a builder based on the given JsonComponent. <br>
		 * The source JsonComponent's information is copied to the JsonComponentBuilder.
		 * 
		 * @param component the source component
		 */
		public Builder(JsonComponentFramework component) {
			super(component);
		}
		
		/**
		 * Creates a builder based on the given Component. <br>
		 * The source Component's information is copied to the JsonComponentBuilder.
		 * 
		 * @param component the source component
		 */
		public Builder(ComponentFramework component) {
			super(component);
		}
		
	}

}
