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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.chat.JsonTag;
import space.arim.api.chat.MessageParserUtil;

import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

/**
 * Parser which may be used to create Json messages using the Kyori Text API.
 * 
 * @author A248
 *
 */
public class KyoriTextParser implements PlatformFormattingParser<TextComponent> {

	@Override
	public TextComponent colour(String msg, FormattingCodePattern formattingPattern) {
		return colourProcessor(msg, formattingPattern.getPattern()).build();
	}

	@Override
	public TextComponent uncoloured(String msg) {
		return TextComponent.of(msg);
	}
	
	@Override
	public TextComponent parseJson(String msg, FormattingCodePattern formattingPattern) {
		return parseJsonProcessor(msg, formattingPattern.getPattern());
	}

	@Override
	public TextComponent parseUncolouredJson(String msg) {
		return parseJsonProcessor(msg, TextComponent::builder);
	}
	
	private static TextComponent parseJsonProcessor(String msg, Pattern processor) {
		return parseJsonProcessor(msg, (node) -> colourProcessor(node, processor));
	}
	
	private static TextComponent parseJsonProcessor(String msg, Function<String, TextComponent.Builder> colourGenerator) {
		TextComponent.Builder parent = TextComponent.builder();
		TextComponent.Builder current = null;

		for (String node : MessageParserUtil.DOUBLE_PIPE_PATTERN.split(msg)) {
			if (node.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getFor(node);
			if (tag == JsonTag.NONE) {
				if (current != null) {
					parent.append(current.build());
				}
				current = colourGenerator.apply(msg);

			} else if (current != null) {
				String value = node.substring(4);
				switch (tag) {
				case TTP:
					current.hoverEvent(HoverEvent.showText(colourGenerator.apply(value).build()));
					break;
				case URL:
					current.clickEvent(ClickEvent.openUrl(value));
					break;
				case CMD:
					current.clickEvent(ClickEvent.runCommand(value));
					break;
				case SGT:
					current.clickEvent(ClickEvent.suggestCommand(value));
					break;
				case INS:
					current.insertion(value);
					break;
				default:
					throw new IllegalStateException("Unknown tag: " + tag);
				}
			}
		}
		return parent.build();
	}
	
	private static TextComponent.Builder colourProcessor(String msg, Pattern processor) {
		TextComponent.Builder builder = TextComponent.builder();
		
		/*
		 * Approach:
		 * Group the input string by segments of text separated by formatting codes.
		 * When a match is found, the current segment is the text before the current match (but after the previous match).
		 * Add the current segment to the builder using the current colour and styles.
		 * Then, update the current colour and styles according to the match.
		 */
		Matcher matcher = processor.matcher(msg);
		int beginIndex = 0; // the starting index of the current segment
		
		// start without any formatting
		Set<TextDecoration> currentStyles = new HashSet<>();
		TextColor currentColour = null;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = msg.substring(beginIndex, matcher.start());

			builder.append(TextComponent.builder(segment).resetStyle().decorations(currentStyles, true)
					.color((currentColour == null) ? TextColor.WHITE : currentColour).build());

			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end() + 1;

			// update the running formatting codes we're using
			String code = matcher.group();
			char codeChar = code.toLowerCase().charAt(1);
			if (codeChar == 'r') {
				// remove all previous styles
				currentStyles.clear();
				currentColour = null;
			} else {
				TextDecoration decor = getKyoriDecor(codeChar);
				if (decor == null) {
					TextColor kyoriColour = getKyoriColour(codeChar);
					if (kyoriColour != null) {
						currentColour = kyoriColour;
					}
				} else {
					currentStyles.add(decor); // add the current style to the list of running styles
				}
				
			}
		}
		return builder;
	}
	
	private static TextDecoration getKyoriDecor(char codeChar) {
		switch (codeChar) {
		case 'k':
			return TextDecoration.OBFUSCATED;
		case 'l':
			return TextDecoration.BOLD;
		case 'm':
			return TextDecoration.STRIKETHROUGH;
		case 'n':
			return TextDecoration.UNDERLINED;
		case 'o':
			return TextDecoration.ITALIC;
		default:
			return null; // NONE
		}
	}
	
	private static TextColor getKyoriColour(char codeChar) {
		switch (codeChar) {
		case '0':
			return TextColor.BLACK;
		case '1':
			return TextColor.DARK_BLUE;
		case '2':
			return TextColor.DARK_GREEN;
		case '3':
			return TextColor.AQUA;
		case '4':
			return TextColor.DARK_RED;
		case '5':
			return TextColor.DARK_PURPLE;
		case '6':
			return TextColor.GOLD;
		case '7':
			return TextColor.GRAY;
		case '8':
			return TextColor.DARK_GRAY;
		case '9':
			return TextColor.DARK_AQUA;
		case 'a':
			return TextColor.GREEN;
		case 'b':
			return TextColor.BLUE;
		case 'c':
			return TextColor.RED;
		case 'd':
			return TextColor.LIGHT_PURPLE;
		case 'e':
			return TextColor.YELLOW;
		case 'f':
			return TextColor.WHITE;
		default:
			return null; // NONE
		}
	}
	
}
