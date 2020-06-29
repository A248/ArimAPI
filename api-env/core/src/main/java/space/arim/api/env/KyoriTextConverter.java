/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

import java.util.EnumSet;
import java.util.Set;

import space.arim.api.chat.Colour;
import space.arim.api.chat.Message;
import space.arim.api.chat.Style.StyleCatalog;
import space.arim.api.chat.Colour.ColourCatalog;
import space.arim.api.chat.Component;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

/**
 * WIP
 * 
 * @author A248
 *
 */
/*public*/ class KyoriTextConverter implements PlatformMessageConverter<TextComponent> {

	private static TextColor convertColour(Colour colour) {
		if (colour == null) {
			return null;
		}
		switch (ColourCatalog.valueOf(colour)) {
		case BLACK:
			return TextColor.BLACK;
		case DARK_BLUE:
			return TextColor.DARK_BLUE;
		case DARK_GREEN:
			return TextColor.DARK_GREEN;
		case DARK_AQUA:
			return TextColor.DARK_AQUA;
		case DARK_RED:
			return TextColor.DARK_RED;
		case DARK_PURPLE:
			return TextColor.DARK_PURPLE;
		case GOLD:
			return TextColor.GOLD;
		case GRAY:
			return TextColor.GRAY;
		case DARK_GRAY:
			return TextColor.DARK_GRAY;
		case BLUE:
			return TextColor.BLUE;
		case GREEN:
			return TextColor.GREEN;
		case AQUA:
			return TextColor.AQUA;
		case RED:
			return TextColor.RED;
		case LIGHT_PURPLE:
			return TextColor.LIGHT_PURPLE;
		case YELLOW:
			return TextColor.YELLOW;
		case WHITE:
			return TextColor.WHITE;
		default:
			throw new IllegalStateException("No corresponding TextColor for &" + colour.identifier());
		}
	}
	
	private static TextDecoration convertStyle(StyleCatalog style) {
		switch (style) {
		case MAGIC:
			return TextDecoration.OBFUSCATED;
		case BOLD:
			return TextDecoration.BOLD;
		case STRIKETHROUGH:
			return TextDecoration.STRIKETHROUGH;
		case UNDERLINE:
			return TextDecoration.UNDERLINED;
		case ITALIC:
			return TextDecoration.ITALIC;
		default:
			throw new IllegalStateException("No corresponding TextDecoration for &" + style.getStyleValue().identifier());
		}
	}
	
	@Override
	public TextComponent convertFrom(Message message) {
		TextComponent.Builder parent = TextComponent.builder();

		// Start without any formatting
		TextColor currentColour = null;
		Set<TextDecoration> currentDecors = EnumSet.noneOf(TextDecoration.class);

		for (Component component : message.getComponents()) {
			TextComponent.Builder child = TextComponent.builder();

			TextColor kyoriColour = convertColour(component.getColour());
			if (kyoriColour != null) {
				currentColour = kyoriColour;
			}
			child.color((currentColour == null) ? TextColor.WHITE : currentColour);

			for (StyleCatalog style : StyleCatalog.values()) {
				if (component.hasStyle(style.getStyleValue())) {
					currentDecors.add(convertStyle(style));
				}
			}
		}
		return parent.build();
	}

	@Override
	public Message convertTo(TextComponent message) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
