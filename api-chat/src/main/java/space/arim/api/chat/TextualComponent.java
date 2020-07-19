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

/**
 * An immutable part of a message, specifying text, colour, and styles. <br>
 * <br>
 * {@code TextualComponent} should be treated as a "sealed class" and not be subclassed.
 * 
 * @author A248
 *
 */
public class TextualComponent implements TextualComponentInfo {

	private final String text;
	private final int colour;
	private final int styles;
	
	TextualComponent(String text, int colour, int styles) {
		this.text = text;
		this.colour = colour;
		this.styles = styles;
	}
	
	/**
	 * Creates from {@code TextualComponentInfo}. The attributes of the textual component info are
	 * copied.
	 * 
	 * @param info the component info whose attributes to use
	 */
	public TextualComponent(TextualComponentInfo info) {
		int colour = info.getColour();
		HexManipulator.checkRange0(colour);
		this.text = Objects.requireNonNull(info.getText(), "Text must not be null");
		this.colour = colour;
		this.styles = info.getStyles();
		/*
		 * Sealed class protection
		 */
		Class<?> clazz = getClass();
		if (clazz != TextualComponent.class && clazz != JsonComponent.class) {
			throw new IllegalStateException("TextualComponent cannot be subclassed except by JsonComponent");
		}
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
		return "TextualComponent [text=" + text + ", colour=" + colour + ", styles=" + styles + "]";
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
		if (!(object instanceof TextualComponent) || object instanceof JsonComponent) {
			return false;
		}
		TextualComponent other = (TextualComponent) object;
		return colour == other.colour && styles == other.styles && text.equals(other.text);
	}

	/**
	 * Builder for creating textual message components
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements TextualComponentInfo {
		
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
		 * Creates the builder, using the provided existing {@code TextualComponentInfo}, whose attributes
		 * are copied to this builder
		 * 
		 * @param info the component info to use
		 */
		public Builder(TextualComponentInfo info) {
			int colour = info.getColour();
			HexManipulator.checkRange0(colour);
			text = Objects.requireNonNull(info.getText(), "Text must not be null");
			this.colour = colour;
			styles = info.getStyles();
		}

		/**
		 * Sets the text of this builder to the specified text
		 * 
		 * @param text the new text
		 * @return the builder
		 */
		public Builder text(String text) {
			this.text = Objects.requireNonNull(text, "Text must not be null");
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
			HexManipulator.checkRange0(colour);
			this.colour = colour;
			return this;
		}
		
		/**
		 * Sets the styles of this builder to the specified styles
		 * 
		 * @param styles the new styles
		 * @return the builder
		 */
		public Builder styles(int styles) {
			this.styles = styles;
			return this;
		}
		
		/**
		 * Creates a {@link TextualComponent} from the details of this builder
		 * 
		 * @return the built textual component
		 */
		public TextualComponent build() {
			return new TextualComponent(text, colour, styles);
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
			return "TextualComponent.Builder [text=" + text + ", colour=" + colour + ", styles=" + styles + "]";
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
			if (object == null || getClass() != object.getClass()) {
				return false;
			}
			Builder other = (Builder) object;
			return colour == other.colour && styles == other.styles && text.equals(other.text);
		}
		
	}
	
}
