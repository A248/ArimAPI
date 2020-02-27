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

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Accepts formatted strings and converts them to a {@link Message}. <br>
 * <br>
 * <b>Colour Parsing</b>: <br>
 * * Uses '&' colour codes by default. ({@link #DEFAULT_COLOUR_CHAR}) <br>
 * * Does not add or convert <code>Format.RESET</code> codes, see {@link Format#RESET} for more information. <br>
 * <br>
 * <b>JSON Parsing</b>: <br>
 * * Parses Minecraft JSON messages, not JSON data objects. <br>
 * * Includes colour parsing by default. <br>
 * * Follows RezzedUp's JSON.sk format.
 * 
 * @author A248
 *
 */
public final class MessageUtil {
	
	/**
	 * The default colour code symbol, '&'.
	 * 
	 */
	public static final char DEFAULT_COLOUR_CHAR = '&';
	
	private static final ConcurrentHashMap<Character, Pattern> CHARACTER_PATTERN_CACHE = new ConcurrentHashMap<Character, Pattern>();
	private static final Pattern JSON_NODE_PATTERN = Pattern.compile("||", Pattern.LITERAL);
	
	private static Pattern compileColourPattern(char colourChar) {
		return Pattern.compile(colourChar + "[0-9A-Fa-fK-Rk-r]");
	}
	
	/**
	 * Gets a regex pattern for an arbitrary colour char, such as '&' or '§'. <br>
	 * The pattern will match all valid colour codes constructed using the given char.
	 * 
	 * @param colourChar the colour code character, like '&'
	 * @return a regex pattern for colour codes
	 */
	public static Pattern getColourPattern(char colourChar) {
		return (colourChar == '&' || colourChar == '§') ? getColourPatternCached(colourChar) : compileColourPattern(colourChar);
	}
	
	/**
	 * Gets a regex pattern for an arbitrary colour char, such as '&' or '§'. <br>
	 * The pattern will match all valid colour codes constructed using the given char. <br>
	 * <br>
	 * Unlike {@link #getColourPattern(char)}, this method caches the results in order
	 * to provide faster resolution for successive calls. The cache is thread safe.
	 * 
	 * @param colourChar the colour code character, like '&'
	 * @return a regex pattern for colour codes
	 */
	private static Pattern getColourPatternCached(char colourChar) {
		return CHARACTER_PATTERN_CACHE.computeIfAbsent(colourChar, MessageUtil::compileColourPattern);
	}
	
	private static <T extends MessageBuilder> T addColouredContent(String msg, T builder, Matcher colourMatcher) {
		int previous = 0;
		while (colourMatcher.find()) {
			String content = msg.substring(previous, colourMatcher.start());
			builder.add(content);
			Format format = Format.fromCode(colourMatcher.group().charAt(1));
			if (format instanceof Colour) {
				builder.colour((Colour) format);
			} else if (format instanceof Style) {
				builder.style((Style) format);
			}
			previous = colourMatcher.end() + 1;
		}
		return builder;
	}
	
	private static Message parse(String msg, Pattern processor) {
		return addColouredContent(msg, new SimpleMessageBuilder(), processor.matcher(msg)).build();
	}
	
	/**
	 * Parses a <code>Message</code> from text using colour code formatting, e.g. '&a' or '§6'. <br>
	 * <i>Invalid colour codes are simply ignored</i>.
	 * 
	 * @param msg the source message
	 * @param colourChar the character signifying the start of a colour code
	 * @return a formed Message
	 */
	public static Message parse(String msg, char colourChar) {
		return parse(msg, getColourPatternCached(colourChar));
	}
	
	/**
	 * Parses a <code>Message</code> from text using '&' colour code formatting.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parse(String msg) {
		return parse(msg, DEFAULT_COLOUR_CHAR);
	}
	
	/**
	 * Parses a <code>Message</code> without any colour codes parsed.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parseUncoloured(String msg) {
		return (new SimpleMessageBuilder()).add(msg).build();
	}
	
	private enum TagType {
		NONE,
		TTP,
		URL,
		CMD,
		SGT,
		INS;
		
		static final int TAG_NOTATION_LENGTH = 4;
		
	}
	
	private static TagType getJsonTag(String node) {
		if (node.length() <= TagType.TAG_NOTATION_LENGTH) {
			return TagType.NONE;
		}
		switch (node.substring(0, 4).toLowerCase()) {
		case "ttp:":
			return TagType.TTP;
		case "url:":
			return TagType.URL;
		case "cmd:":
			return TagType.CMD;
		case "sgt:":
			return TagType.SGT;
		case "ins:":
			return TagType.INS;
		default:
			return TagType.NONE;
		}
	}
	
	private static Message parseJson(String msg, BiConsumer<String, ? super MessageBuilder> baseNodeGenerator, Function<String, ? extends Message> tooltipGenerator) {
		JsonMessageBuilder builder = new JsonMessageBuilder();
		for (String node : JSON_NODE_PATTERN.split(msg)) {
			if (node.isEmpty()) {
				continue;
			}
			TagType tag = getJsonTag(node);
			if (tag.equals(TagType.NONE)) {
				baseNodeGenerator.accept(node, builder);
			} else {
				String value = node.substring(4);
				switch (tag) {
				case TTP:
					builder.tooltip(tooltipGenerator.apply(value));
				case URL:
					builder.url(value);
				case CMD:
					builder.command(value);
				case SGT:
					builder.suggest(value);
				case INS:
					builder.insertion(value);
				default:
					break;
				}
			}
		}
		return builder.cleanBuild();
	}
	
	private static Message parseJson(String msg, Pattern processor) {
		return parseJson(msg, (node, builder) -> addColouredContent(node, builder, processor.matcher(msg)), (tooltip) -> parse(tooltip, processor));
	}
	
	/**
	 * Parses a <code>Message</code>, including JSON features, from text using colour code formatting, e.g. '&a' or '§6'. <br>
	 * <i>Invalid colour codes are simply ignored</i>.
	 * 
	 * @param msg the source message
	 * @param colourChar the character signifying the start of a colour code
	 * @return a formed Message
	 */
	public static Message parseJson(String msg, char colourChar) {
		return parseJson(msg, getColourPatternCached(colourChar));
	}
	
	/**
	 * Parses a <code>Message</code>, including JSON features, from text and using '&' colour code formatting.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parseJson(String msg) {
		return parseJson(msg, DEFAULT_COLOUR_CHAR);
	}
	
	/**
	 * Parses a <code>Message</code>, including JSON features, from text, excluding colour code parsing.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parseUncolouredJson(String msg) {
		return parseJson(msg, (node, builder) -> builder.add(node), MessageUtil::parseUncoloured);
	}
	
}
