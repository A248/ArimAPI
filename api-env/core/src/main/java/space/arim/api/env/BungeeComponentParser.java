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

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.chat.JsonTag;
import space.arim.api.chat.MessageParserUtil;

/**
 * Parser which may be used to create Json messages using the BungeeCord/Spigot Component API.
 * 
 * @author A248
 *
 */
public class BungeeComponentParser implements PlatformFormattingParser<BaseComponent[]> {

	@Override
	public BaseComponent[] colour(String msg, FormattingCodePattern formattingPattern) {
		return colourProcessor(msg, formattingPattern.getPattern());
	}
	
	@Override
	public BaseComponent[] uncoloured(String msg) {
		return new BaseComponent[] {new TextComponent(msg)};
	}
	
	@Override
	public BaseComponent[] parseJson(String msg, FormattingCodePattern formattingPattern) {
		Pattern pattern = formattingPattern.getPattern();
		return parseJsonProcessor(msg, (node) -> new TextComponent(colourProcessor(node, pattern)), (node) -> colourProcessor(node, pattern));
	}
	
	@Override
	public BaseComponent[] parseUncolouredJson(String msg) {
		return parseJsonProcessor(msg, TextComponent::new, this::uncoloured);
	}
	
	private static BaseComponent[] parseJsonProcessor(String msg, Function<String, TextComponent> nodeGenerator,
			Function<String, BaseComponent[]> tooltipGenerator) {
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		BaseComponent current = null;

		for (String node : MessageParserUtil.DOUBLE_PIPE_PATTERN.split(msg)) {
			if (node.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getFor(node);
			if (tag == JsonTag.NONE) {
				if (current != null) {
					components.add(current);
				}
				current = nodeGenerator.apply(node);

			} else if (current != null) {
				String value = node.substring(4);
				switch (tag) {
				case TTP:
					current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltipGenerator.apply(value)));
					break;
				case URL:
					current.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
					break;
				case CMD:
					current.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
					break;
				case SGT:
					current.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
					break;
				case INS:
					current.setInsertion(value);
					break;
				default:
					throw new IllegalStateException("Unknown tag: " + tag);
				}
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	private static BaseComponent[] colourProcessor(String colourable, Pattern processor) {
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		
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
		ChatColor currentColour = null;
		/*
		 * variable indexes
		 * 0 = obfuscated
		 * 1 = bold
		 * 2 = strikethrough
		 * 3 = underline
		 * 4 = italic
		 */
		boolean[] resetStyles = {false, false, false, false, false};
		boolean[] currentStyles = resetStyles.clone();
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = colourable.substring(beginIndex, matcher.start());
			TextComponent current = new TextComponent(segment);
			current.setColor((currentColour == null) ? ChatColor.WHITE : currentColour);
			current.setObfuscated(currentStyles[0]);
			current.setBold(currentStyles[1]);
			current.setStrikethrough(currentStyles[2]);
			current.setUnderlined(currentStyles[3]);
			current.setItalic(currentStyles[4]);
			components.add(current);
			
			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end() + 1;
			
			// update the running formatting codes we're using
			String code = matcher.group();
			char codeChar = code.toLowerCase().charAt(1);
			if (codeChar == 'r') {
				currentStyles = resetStyles.clone();
				currentColour = null;
			} else {
				int styleIndex = getStyleIndex(codeChar);
				if (styleIndex == -1) {
					ChatColor bungeeColour = getBungeeColour(codeChar);
					if (bungeeColour != null) {
						currentColour = bungeeColour;
					}
				} else {
					currentStyles[styleIndex] = true;
				}
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	private static int getStyleIndex(char codeChar) {
		switch (codeChar) {
		case 'k':
			return 0;
		case 'l':
			return 1;
		case 'm':
			return 2;
		case 'n':
			return 3;
		case 'o':
			return 4;
		default:
			return -1;
		}
	}
	
	private static ChatColor getBungeeColour(char codeChar) {
		switch (codeChar) {
		case '0':
			return ChatColor.BLACK;
		case '1':
			return ChatColor.DARK_BLUE;
		case '2':
			return ChatColor.DARK_GREEN;
		case '3':
			return ChatColor.AQUA;
		case '4':
			return ChatColor.DARK_RED;
		case '5':
			return ChatColor.DARK_PURPLE;
		case '6':
			return ChatColor.GOLD;
		case '7':
			return ChatColor.GRAY;
		case '8':
			return ChatColor.DARK_GRAY;
		case '9':
			return ChatColor.DARK_AQUA;
		case 'a':
			return ChatColor.GREEN;
		case 'b':
			return ChatColor.BLUE;
		case 'c':
			return ChatColor.RED;
		case 'd':
			return ChatColor.LIGHT_PURPLE;
		case 'e':
			return ChatColor.YELLOW;
		case 'f':
			return ChatColor.WHITE;
		default:
			return null;
		}
	}
	
}
