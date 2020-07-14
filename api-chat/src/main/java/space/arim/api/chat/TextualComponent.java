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
 * An immutable part of a message, specifying text, colour, and styles.
 * 
 * @author A248
 *
 */
public class TextualComponent implements TextualComponentInfo {

	private final String text;
	private final int hex;
	private final int styles;
	
	TextualComponent(String text, int hex, int styles) {
		this.text = text;
		this.hex = hex;
		this.styles = styles;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public int getColour() {
		return hex;
	}
	
	@Override
	public int getStyles() {
		return styles;
	}
	
	/**
	 * Builder for creating textual message components
	 * 
	 * @author A248
	 *
	 */
	public static class Builder implements TextualComponentInfo {
		
		private String text = "";
		private int hex;
		private int styles;
		
		/**
		 * Creates the builder. By default it has no formatting and uses an empty {@code String}
		 * as its text.
		 * 
		 */
		public Builder() {
			
		}
		
		/**
		 * Creates the builder, using the provided existing {@code TextualComponentInfo} for default values
		 * 
		 * @param template the component info to use for default values
		 */
		public Builder(TextualComponentInfo template) {
			text = template.getText();
			hex = template.getColour();
			styles = template.getStyles();
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
		 * @param hex the new hex colour
		 * @return the builder
		 * @throws IllegalArgumentException if {@code hex} is outside the range of a hex colour
		 */
		public Builder colour(int hex) {
			HexManipulator.checkRange0(hex);
			this.hex = hex;
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
			return new TextualComponent(text, hex, styles);
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public int getColour() {
			return hex;
		}

		@Override
		public int getStyles() {
			return styles;
		}
		
	}
	
}
