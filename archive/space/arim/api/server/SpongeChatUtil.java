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
package space.arim.api.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import space.arim.api.annotation.Platform;

/**
 * Utility class for formatting chat messages and parsing colours. <br>
 * Accepts uppercase and lowercase color chars. <br>
 * <br>
 * Sponge complement to {@link ChatUtil} <br>
 * <br>
 * <b>Colour Parsing:</b> <br>
 * * Parses '{@literal &}' colour codes. <br>
 * * Usage: {@link #colour(String)}. <br>
 * * Removing colour: {@link #stripColour(String)}. <br>
 * * Converting from '§' colour codes: {@link #replaceColour(String)}. <br>
 * * A string is considered <i>colored</i> when it uses '§' color codes. <br>
 * <br>
 * <b>Json Messages:</b> <br>
 * * Parses json based on RezzedUp's json.sk format <br>
 * * Includes colour parsing. <br>
 * * Usage: {@link #parseJson(String)}. <br>
 * * Removing formatting: {@link #stripJson(String)}
 * 
 * @author A248
 *
 */
@Platform(Platform.Type.SPONGE)
public class SpongeChatUtil {

	private SpongeChatUtil() {}
	
	public static Text parseJson(String jsonable) {
		return parseColouredJsonSpongeProcessor(jsonable, ChatUtil.AMPERSAND_PATTERN);
	}
	
	public static Text parseColouredJson(String colouredJsonable) {
		return parseColouredJsonSpongeProcessor(colouredJsonable, ChatUtil.SECTION_PATTERN);
	}
	
	private static Text parseColouredJsonSpongeProcessor(String jsonable, Pattern processor) {
		Text.Builder current = null;
		Text.Builder parent = Text.builder();
		for (String node : jsonable.split("||")) {
			TagType tag = ChatUtil.jsonTag(node);
			if (tag.equals(TagType.NONE)) {
				if (current != null) {
					parent.append(current.build());
				}
				current = colourProcessor(jsonable, processor);
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(TagType.TTP)) {
					current.onHover(TextActions.showText(colourProcessor(value, processor).build()));
				} else if (tag.equals(TagType.URL)) {
					if (!value.startsWith("https://") && !value.startsWith("http://")) {
						value = "http://" + value;
					}
					try {
						current.onClick(TextActions.openUrl(new URL(value)));
					} catch (MalformedURLException ex) {}
				} else if (tag.equals(TagType.CMD)) {
					current.onClick(TextActions.runCommand(value));
				} else if (tag.equals(TagType.SGT)) {
					current.onClick(TextActions.suggestCommand(value));
				} else if (tag.equals(TagType.INS)) {
					current.onShiftClick(TextActions.insertText(value));
				}
			}
		}
		return parent.build();
	}
	
	@Platform(Platform.Type.SPONGE)
	public static Text colour(String colourable) {
		return colourProcessor(colourable, ChatUtil.AMPERSAND_PATTERN).build();
	}
	
	private static Text.Builder colourProcessor(String colourable, Pattern processor) {
		Text.Builder builder = Text.builder();
		
		/*
		 * Approach:
		 * Group the input string by segments of text separated by formatting codes.
		 * When a match is found, the current segment is the text before the current match (but after the previous match).
		 * Add the current segment to the builder using the current colour and styles.
		 * Then, update the current colour and styles according to the match.
		 */
		Matcher matcher = processor.matcher(colourable);
		int beginIndex = 0; // the starting index of the current segment
		
		// start without any formatting
		ArrayList<TextStyle> currentStyles = new ArrayList<TextStyle>();
		currentStyles.add(TextStyles.NONE);
		TextColor currentColour = TextColors.NONE;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = colourable.substring(beginIndex, matcher.start());
			builder.append(Text.builder(segment).color(currentColour).style(currentStyles.toArray(new TextStyle[] {})).build());
			
			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end() + 1;
			
			// update the running formatting codes we're using
			String code = matcher.group();
			if (isSpongeStyle(code)) {
				TextStyle style = getSpongeStyle(code);
				if (style == TextStyles.RESET) { // if the style is reset, it removes all previous styles
					currentStyles.clear();
				}
				currentStyles.add(style); // add the current style to the list of running styles
			} else {
				currentColour = getSpongeColour(code); // just replace the old colour with the new running colour
			}
		}
		return builder;
	}
	
	private static boolean isSpongeStyle(String code) {
		return getSpongeStyle(code) != TextStyles.NONE;
	}
	
	private static TextStyle getSpongeStyle(String code) {
		switch (code.toLowerCase().charAt(1)) {
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
		case 'r':
			return TextStyles.RESET;
		default:
			return TextStyles.NONE;
		}
	}
	
	private static TextColor getSpongeColour(String code) {
		switch (code.toLowerCase().charAt(1)) {
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
		case 'r':
			return TextColors.RESET;
		default:
			return TextColors.NONE;
		}
	}
	
}
