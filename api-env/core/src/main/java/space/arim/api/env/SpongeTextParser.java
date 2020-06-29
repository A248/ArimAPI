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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.chat.JsonTag;
import space.arim.api.chat.MessageParserUtil;

/**
 * Parser which may be used to create Json messages using the Sponge textual API.
 * 
 * @author A248
 *
 */
public class SpongeTextParser implements PlatformFormattingParser<Text> {

	@Override
	public Text colour(String msg, FormattingCodePattern formattingPattern) {
		return colourProcessor(msg, formattingPattern.getPattern()).build();
	}
	
	@Override
	public Text uncoloured(String msg) {
		return Text.of(msg);
	}
	
	@Override
	public Text parseJson(String msg, FormattingCodePattern formattingPattern) {
		return parseJsonProcessor(msg, formattingPattern.getPattern());
	}
	
	@Override
	public Text parseUncolouredJson(String msg) {
		return parseJsonProcessor(msg, Text::builder);
	}
	
	private static Text parseJsonProcessor(String msg, Pattern processor) {
		return parseJsonProcessor(msg, (node) -> colourProcessor(node, processor));
	}
	
	private static Text parseJsonProcessor(String msg, Function<String, ? extends Text.Builder> colourGenerator) {
		Text.Builder parent = Text.builder();
		Text.Builder current = null;

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
					current.onHover(TextActions.showText(colourGenerator.apply(value).build()));
					break;
				case URL:
					try {
						current.onClick(TextActions.openUrl(new URL(value)));
					} catch (MalformedURLException ignored) {
						// We can't do anything about this
						// blame Sponge for lack of flexibility
					}
					break;
				case CMD:
					current.onClick(TextActions.runCommand(value));
					break;
				case SGT:
					current.onClick(TextActions.suggestCommand(value));
					break;
				case INS:
					current.onShiftClick(TextActions.insertText(value));
					break;
				default:
					throw new IllegalStateException("Unknown tag: " + tag);
				}
			}
		}
		return parent.build();
	}
	
	private static Text.Builder colourProcessor(String msg, Pattern processor) {
		Text.Builder builder = Text.builder();
		
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
		Set<TextStyle> currentStyles = new HashSet<TextStyle>();
		TextColor currentColour = null;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = msg.substring(beginIndex, matcher.start());

			builder.append(Text.builder(segment).color((currentColour == null) ? TextColors.WHITE : currentColour)
					.style(currentStyles.toArray(new TextStyle[] {})).build());

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
				TextStyle style = getSpongeStyle(codeChar);
				if (style == null) {
					TextColor spongeColour = getSpongeColour(codeChar);
					if (spongeColour != null) {
						currentColour = spongeColour;
					}
				} else {
					currentStyles.add(style);
				}
			}
		}
		return builder;
	}
	
	private static TextStyle getSpongeStyle(char codeChar) {
		switch (codeChar) {
		case 'k':
			return TextStyles.OBFUSCATED;
		case 'l':
			return TextStyles.BOLD;
		case 'm':
			return TextStyles.STRIKETHROUGH;
		case 'n':
			return TextStyles.UNDERLINE;
		case 'o':
			return TextStyles.ITALIC;
		default:
			return null;
		}
	}
	
	private static TextColor getSpongeColour(char codeChar) {
		switch (codeChar) {
		case '0':
			return TextColors.BLACK;
		case '1':
			return TextColors.DARK_BLUE;
		case '2':
			return TextColors.DARK_GREEN;
		case '3':
			return TextColors.AQUA;
		case '4':
			return TextColors.DARK_RED;
		case '5':
			return TextColors.DARK_PURPLE;
		case '6':
			return TextColors.GOLD;
		case '7':
			return TextColors.GRAY;
		case '8':
			return TextColors.DARK_GRAY;
		case '9':
			return TextColors.DARK_AQUA;
		case 'a':
			return TextColors.GREEN;
		case 'b':
			return TextColors.BLUE;
		case 'c':
			return TextColors.RED;
		case 'd':
			return TextColors.LIGHT_PURPLE;
		case 'e':
			return TextColors.YELLOW;
		case 'f':
			return TextColors.WHITE;
		default:
			return null;
		}
	}
	
}
