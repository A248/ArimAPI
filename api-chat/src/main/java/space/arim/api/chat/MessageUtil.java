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

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Accepts formatted strings and converts them to a {@link Message}. <br>
 * <br>
 * <b>Colour Parsing</b>: <br>
 * * Uses '{@literal &}' colour codes by default. ({@link #DEFAULT_FORMATTING_CHAR}) <br>
 * * Does not add or convert <code>Format.RESET</code> codes, see {@link Format#RESET} for more information. <br>
 * * To use a colour code char other than the default '{@literal &}', use {@link FormattingCodePattern}.
 * <br>
 * <b>Json Message Parsing</b>: <br>
 * * Parses Minecraft Json messages, not JSON data objects. <br>
 * * Includes colour parsing by default. <br>
 * * Follows RezzedUp's JSON.sk format. <br>
 * 
 * @author A248
 *
 */
public final class MessageUtil {
	
	/**
	 * The default formatting code symbol, '{@literal &}'.
	 * 
	 */
	public static final char DEFAULT_FORMATTING_CHAR = '&';
	
	/**
	 * A regex pattern matching double pipes, '||'. <br>
	 * Double pipes are used to signify the start of another json node.
	 * 
	 */
	public static final Pattern DOUBLE_PIPE_PATTERN = Pattern.compile("||", Pattern.LITERAL);
	
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
	 * Parses a <code>Message</code> from text using colour code formatting, e.g. '{@literal &}a' or '§6'. <br>
	 * <i>Invalid colour codes are simply ignored</i>.
	 * 
	 * @param msg the source message
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return a formed Message
	 */
	public static Message parse(String msg, FormattingCodePattern colourPattern) {
		return parse(msg, colourPattern.getValue());
	}
	
	/**
	 * Parses a <code>Message</code> from text using '{@literal &}' colour code formatting. <br>
	 * <i>Invalid colour codes are simply ignored</i>.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parse(String msg) {
		return parse(msg, FormattingCodePattern.get());
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
	
	private static Message parseJson(String msg, BiConsumer<String, ? super MessageBuilder> baseNodeGenerator, Function<String, ? extends Message> tooltipGenerator) {
		JsonMessageBuilder builder = new JsonMessageBuilder();
		for (String node : DOUBLE_PIPE_PATTERN.split(msg)) {
			if (node.isEmpty()) {
				continue;
			}
			JsonTag tag = JsonTag.getFor(node);
			if (tag.equals(JsonTag.NONE)) {
				baseNodeGenerator.accept(node, builder);
			} else {
				String value = node.substring(4);
				switch (tag) {
				case TTP:
					builder.showTooltip(tooltipGenerator.apply(value));
					break;
				case URL:
					builder.openUrl(value);
					break;
				case CMD:
					builder.runCommand(value);
					break;
				case SGT:
					builder.suggestCommand(value);
					break;
				case INS:
					builder.insertText(value);
					break;
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
	 * Parses a <code>Message</code>, including JSON features, from text using colour code formatting, e.g. '{@literal &}a' or '§6'. <br>
	 * <i>Invalid colour codes are simply ignored</i>. <br>
	 * <br>
	 * The colour pattern to use <b>MUST</b> be fetched as specified  in the main documentation ({@link MessageUtil}).
	 * 
	 * @param msg the source message
	 * @param colourPattern the pattern corresponding to the colour code char
	 * @return a formed Message
	 */
	public static Message parseJson(String msg, FormattingCodePattern colourPattern) {
		return parseJson(msg, colourPattern.getValue());
	}
	
	/**
	 * Parses a <code>Message</code>, including JSON features, from text and using '{@literal &}' colour code formatting.
	 * 
	 * @param msg the source message
	 * @return a formed Message
	 */
	public static Message parseJson(String msg) {
		return parseJson(msg, FormattingCodePattern.get());
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
