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

import java.util.Objects;

import space.arim.api.chat.manipulator.ColourManipulator;

/**
 * An immutable part of a message, specifying text, colour, and styles.
 * 
 * @author A248
 *
 */
public final class ChatComponent implements ChatComponentInfo {

	private final String text;
	private final int colour;
	private final int styles;
	
	private static final ChatComponent EMPTY = new ChatComponent("", 0, 0);
	
	private ChatComponent(String text, int colour, int styles) {
		this.text = Objects.requireNonNull(text, "text");
		this.colour = ColourManipulator.getInstance().checkRange(colour);
		MessageStyle.checkStylesRange(styles);
		this.styles = styles;
	}
	
	/**
	 * Creates from {@code ChatComponentInfo}. The attributes of the chat component info are
	 * copied.
	 * 
	 * @param info the component info whose attributes to use
	 * @return the chat component
	 */
	public static ChatComponent create(ChatComponentInfo info) {
		if (info instanceof ChatComponent) {
			return (ChatComponent) info;
		}
		String text = info.getText();
		if (text.isEmpty()) {
			return EMPTY;
		}
		return new ChatComponent(text, info.getColour(), info.getStyles());
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public int getColour() {
		return colour;
	}
	
	@Override
	public int getStyles() {
		return styles;
	}

	@Override
	public String toString() {
		return "ChatComponent [text=" + text + ", colour=" + colour + ", styles=" + styles + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colour;
		result = prime * result + styles;
		result = prime * result + text.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ChatComponent)) {
			return false;
		}
		ChatComponent other = (ChatComponent) object;
		return colour == other.colour && styles == other.styles && text.equals(other.text);
	}

	/**
	 * Builder for creating textual message components
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements ChatComponentInfo {
		
		private String text = "";
		private int colour;
		private int styles;
		
		/**
		 * Creates the builder. By default it has no formatting and uses an empty {@code String}
		 * as its text.
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Creates the builder, using the provided existing {@code ChatComponentInfo}, whose attributes
		 * are copied to this builder
		 * 
		 * @param info the component info to use
		 */
		public Builder(ChatComponentInfo info) {
			text = Objects.requireNonNull(info.getText(), "text");
			colour = ColourManipulator.getInstance().checkRange(info.getColour());
			styles = info.getStyles();
		}

		/**
		 * Sets the text of this builder to the specified text
		 * 
		 * @param text the new text
		 * @return the builder
		 * @throws NullPointerException if {@code text} is null
		 */
		public Builder text(String text) {
			this.text = Objects.requireNonNull(text, "text");
			return this;
		}
		
		/**
		 * Sets the hex colour of this builder to the specified value
		 * 
		 * @param colour the new hex colour
		 * @return the builder
		 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
		 */
		public Builder colour(int colour) {
			this.colour = ColourManipulator.getInstance().checkRange(colour);
			return this;
		}
		
		/**
		 * Sets the styles of this builder to the specified styles
		 * 
		 * @param styles the new styles
		 * @return the builder
		 * @throws IllegalArgumentException if {@code styles} is outside the range of those defined by {@link MessageStyle}
		 */
		public Builder styles(int styles) {
			MessageStyle.checkStylesRange(styles);
			this.styles = styles;
			return this;
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public int getColour() {
			return colour;
		}

		@Override
		public int getStyles() {
			return styles;
		}
		
		/**
		 * Creates a {@link ChatComponent} from the details of this builder
		 * 
		 * @return the built chat component
		 */
		public ChatComponent build() {
			return ChatComponent.create(this);
		}

		@Override
		public String toString() {
			return "ChatComponent.Builder [text=" + text + ", colour=" + colour + ", styles=" + styles + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + colour;
			result = prime * result + styles;
			result = prime * result + text.hashCode();
			return result;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof Builder)) {
				return false;
			}
			Builder other = (Builder) object;
			return colour == other.colour && styles == other.styles && text.equals(other.text);
		}
		
	}
	
}
