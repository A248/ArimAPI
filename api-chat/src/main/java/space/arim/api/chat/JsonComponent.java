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
public final class JsonComponent extends TextualComponent implements JsonComponentInfo {

	private final JsonHover hoverAction;
	private final JsonClick clickAction;
	private final JsonInsertion insertionAction;
	
	/**
	 * Creates from {@code JsonComponentInfo}. The attributes of the json component info are
	 * copied.
	 * 
	 * @param info the component info whose attributes to use
	 */
	public JsonComponent(JsonComponentInfo info) {
		super(info);
		this.hoverAction = info.getHoverAction();
		this.clickAction = info.getClickAction();
		this.insertionAction = info.getInsertionAction();
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
	
	@Override
	public String toString() {
		return "TextualComponent [text=" + getText() + ", colour=" + getColour() + ", styles=" + getStyles()
				+ ", hoverAction=" + hoverAction + ", clickAction=" + clickAction + ", insertionAction="
				+ insertionAction + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hoverAction == null) ? 0 : hoverAction.hashCode());
		result = prime * result + ((clickAction == null) ? 0 : clickAction.hashCode());
		result = prime * result + ((insertionAction == null) ? 0 : insertionAction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof JsonComponent)) {
			return false;
		}
		JsonComponent other = (JsonComponent) object;
		return getColour() == other.getColour() && getStyles() == other.getStyles() && getText().equals(other.getText())
				&& ((hoverAction == null) ? other.hoverAction == null : hoverAction.equals(other.hoverAction))
				&& ((clickAction == null) ? other.clickAction == null : clickAction.equals(other.clickAction))
				&& ((insertionAction == null) ? other.insertionAction == null : insertionAction.equals(other.insertionAction));
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
		 * Creates the builder, using the provided existing {@code TextualComponentInfo}, whose attributes
		 * are copied to this builder. <br>
		 * <br>
		 * If such component info is {@code JsonComponentInfo}, then its JSON properties will also be used.
		 * 
		 * @param info the component info to use for default values
		 */
		public Builder(TextualComponentInfo info) {
			super(info);
			if (info instanceof JsonComponentInfo) {
				JsonComponentInfo jsonInfo = (JsonComponentInfo) info;

				hoverAction = jsonInfo.getHoverAction();
				clickAction = jsonInfo.getClickAction();
				insertionAction = jsonInfo.getInsertionAction();
			}
		}
		
		/**
		 * Creates the builder, using the provided existing {@code JsonComponentInfo}, whose attributes
		 * are copied to this builder
		 * 
		 * @param info the component info to use
		 */
		public Builder(JsonComponentInfo info) {
			super(info);
			hoverAction = info.getHoverAction();
			clickAction = info.getClickAction();
			insertionAction = info.getInsertionAction();
		}
		
		@Override
		public Builder text(String text) {
			return (Builder) super.text(text);
		}
		
		@Override
		public Builder colour(int colour) {
			return (Builder) super.colour(colour);
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
			return new JsonComponent(this);
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
		
		@Override
		public String toString() {
			return "JsonComponent.Builder [text=" + getText() + ", colour=" + getColour() + ", styles=" + getStyles()
					+ ", hoverAction=" + hoverAction + ", clickAction=" + clickAction + ", insertionAction="
					+ insertionAction + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((clickAction == null) ? 0 : clickAction.hashCode());
			result = prime * result + ((hoverAction == null) ? 0 : hoverAction.hashCode());
			result = prime * result + ((insertionAction == null) ? 0 : insertionAction.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null || getClass() != object.getClass()) {
				return false;
			}
			Builder other = (Builder) object;
			return getColour() == other.getColour() && getStyles() == other.getStyles() && getText().equals(other.getText())
					&& ((hoverAction == null) ? other.hoverAction == null : hoverAction.equals(other.hoverAction))
					&& ((clickAction == null) ? other.clickAction == null : clickAction.equals(other.clickAction))
					&& ((insertionAction == null) ? other.insertionAction == null : insertionAction.equals(other.insertionAction));
		}
		
	}

}
