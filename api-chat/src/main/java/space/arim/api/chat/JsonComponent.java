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
 * A part of a message with JSON formatting enabled
 * 
 * @author A248
 *
 */
public class JsonComponent extends TextualComponent implements JsonComponentInfo {

	private final JsonHover hoverAction;
	private final JsonClick clickAction;
	private final JsonInsertion insertionAction;
	
	JsonComponent(String text, int hex, int styles, JsonHover hoverAction, JsonClick clickAction, JsonInsertion insertionAction) {
		super(text, hex, styles);
		this.hoverAction = hoverAction;
		this.clickAction = clickAction;
		this.insertionAction = insertionAction;
	}
	
	@Override
	public JsonHover getHoverAction() {
		return hoverAction;
	}
	
	@Override
	public JsonClick getClickAction() {
		return clickAction;
	}
	
	@Override
	public JsonInsertion getInsertionAction() {
		return insertionAction;
	}
	
	/**
	 * Builder for creating JSON message components
	 * 
	 * @author A248
	 *
	 */
	public static class Builder extends TextualComponent.Builder implements JsonComponentInfo {
		
		private JsonHover hoverAction;
		private JsonClick clickAction;
		private JsonInsertion insertionAction;
		
		/**
		 * Creates the builder without any existing formatting
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Creates the builder, using the provided existing {@code TextualComponentInfo} for default values. <br>
		 * If such component info is {@code JsonComponentInfo}, then its JSON properties will also be used.
		 * 
		 * @param template the component info to use for default values
		 */
		public Builder(TextualComponentInfo template) {
			super(template);
			if (template instanceof JsonComponentInfo) {
				JsonComponentInfo jsonTemp = (JsonComponentInfo) template;

				hoverAction = jsonTemp.getHoverAction();
				clickAction = jsonTemp.getClickAction();
				insertionAction = jsonTemp.getInsertionAction();
			}
		}
		
		@Override
		public Builder text(String text) {
			return (Builder) super.text(text);
		}
		
		@Override
		public Builder colour(int hex) {
			return (Builder) super.colour(hex);
		}
		
		@Override
		public Builder styles(int styles) {
			return (Builder) super.styles(styles);
		}
		
		/**
		 * Sets the hover action of this builder to the specified hover action
		 * 
		 * @param hoverAction the new hover action, or {@code null} for none
		 * @return the builder
		 */
		public Builder hoverAction(JsonHover hoverAction) {
			this.hoverAction = hoverAction;
			return this;
		}
		
		/**
		 * Sets the click action of this builder to the specified click action
		 * 
		 * @param clickAction the new click action, or {@code null} for none
		 * @return the builder
		 */
		public Builder clickAction(JsonClick clickAction) {
			this.clickAction = clickAction;
			return this;
		}
		
		/**
		 * Sets the insertion action of this builder to the specified insertion action
		 * 
		 * @param insertionAction the new insertion action, or {@code null} for none
		 * @return the builder
		 */
		public Builder insertionAction(JsonInsertion insertionAction) {
			this.insertionAction = insertionAction;
			return this;
		}
		
		/**
		 * Creates a {@link JsonComponent} from the details of this builder
		 * 
		 * @return the built JSON component
		 */
		@Override
		public JsonComponent build() {
			return new JsonComponent(getText(), getColour(), getStyles(), hoverAction, clickAction, insertionAction);
		}

		@Override
		public JsonHover getHoverAction() {
			return hoverAction;
		}

		@Override
		public JsonClick getClickAction() {
			return clickAction;
		}

		@Override
		public JsonInsertion getInsertionAction() {
			return insertionAction;
		}
		
	}

}
