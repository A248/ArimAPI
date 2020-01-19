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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.universal.util.collections.CollectionsUtil;

import space.arim.api.annotation.Platform;
import space.arim.api.util.StringsUtil;

/**
 * Utility class for formatting chat messages and parsing colours. <br>
 * Accepts uppercase and lowercase chars. <br>
 * <br>
 * <b>Notice:</b> <br>
 * Some methods are platform dependent! It is recommended to instead use {@link SpigotUtil}, {@link BungeeUtil}, or {@link SpongeUtil} depending on your platform. <br>
 * <br>
 * <b>Colour Parsing:</b> <br>
 * * Parses '&' colour codes. <br>
 * * Spigot usage: {@link #colour(String)}. <br>
 * * BungeeCord usage: {@link #colourBungee(String)}. <br>
 * * Sponge usage: {@link #colourSponge(String)}. <br>
 * * Removing colour: {@link #stripColour(String)}. <br>
 * * Converting from '§' colour codes: {@link #replaceColour(String)}. <br>
 * * A string is considered <i>colored</i> when it uses '§' color codes. <br>
 * <br>
 * <b>Json Messages:</b> <br>
 * * Parses json based on RezzedUp's json.sk format <br>
 * * Includes colour parsing. <br>
 * * Requires <i>Spigot</i> or <i>BungeeCord</i>. <br>
 * * Usage: {@link #parseJson(String)}. <br>
 * * Removing formatting: {@link #stripJson(String)}
 * 
 * @author A248
 *
 */
public final class ChatUtil {

	private ChatUtil() {}
	
	private static final Pattern SECTION_PATTERN = Pattern.compile("§[0-9A-Fa-fK-Rk-r]]");
	private static final Pattern AMPERSAND_PATTERN = Pattern.compile("&[0-9A-Fa-fK-Rk-r]]");
	
	/**
	 * Replaces valid '§' colour codes. <br>
	 * 
	 * @param coloured the input string
	 * @return the same string with '&' colour codes instead
	 */
	public static String replaceColour(String coloured) {
		return SECTION_PATTERN.matcher(coloured).replaceAll("&$2");
	}
	
	/**
	 * Removes colour from a message.
	 * 
	 * @param colourable the string to remove colour from
	 * @return an uncoloured string
	 */
	public static String stripColour(String colourable) {
		return AMPERSAND_PATTERN.matcher(colourable).replaceAll("");
	}
	
	/**
	 * Adds colour to a message. <br>
	 * The <i>input</i> uses '&' colour codes. <br>
	 * The <i>result</i> uses '§' colour codes.
	 * 
	 * @param colourable the string to add colour to
	 * @return the coloured string
	 */
	public static String colour(String colourable) {
		return AMPERSAND_PATTERN.matcher(colourable).replaceAll("§$2");
	}
	
	/**
	 * Adds colour to a message then centers it precisely. <br>
	 * See {@link #colour(String)} <br>
	 * See {@link #center(String)}
	 * 
	 * @param colourable the string to operate on
	 * @return the coloured and centered string
	 */
	public static String colourAndCenter(String colourable) {
		return center(colour(colourable));
	}
	
	/**
	 * Adds colour to a message. <br>
	 * <b>Similar to {@link #colour(String)}</b> but additionally converts to a BaseComponent array.
	 * 
	 * @param colourable the input string
	 * @return a coloured BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] colourBungee(String colourable) {
		return colourBungeeProcessor(colourable, AMPERSAND_PATTERN);
	}
	
	/**
	 * Adds colour to a message then centers it precisely. <br>
	 * See {@link #colour(String)} <br>
	 * See {@link #center(String)}
	 * 
	 * @param colourable the string to operate on
	 * @return the coloured and centered string
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] colourAndCenterBungee(String colourable) {
		return colourBungeeProcessor(center(colour(colourable)), SECTION_PATTERN);
	}
	
	private static BaseComponent[] colourBungeeProcessor(String colourable, Pattern processor) {
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
		ChatColor currentColor = ChatColor.WHITE;
		/*
		 * variable indexes
		 * 0 = obfuscated
		 * 1 = bold
		 * 2 = strikethrough
		 * 3 = underline
		 * 4 = italic
		 */
		boolean[] resetStyles = {false, false, false, false, false};
		boolean[] styles = resetStyles;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = colourable.substring(beginIndex, matcher.start());
			TextComponent current = new TextComponent(segment);
			current.setColor(currentColor);
			current.setObfuscated(styles[0]);
			current.setBold(styles[1]);
			current.setStrikethrough(styles[2]);
			current.setUnderlined(styles[3]);
			current.setItalic(styles[4]);
			components.add(current);
			
			// prepare for the next segment by updating the starting index
			beginIndex = matcher.end() + 1;
			
			// update the running formatting codes we're using
			String code = matcher.group();
			if (isStyle(code)) {
				int styleIndex = getStyle(code);
				if (styleIndex == 5) {
					styles = resetStyles;
				} else {
					styles[styleIndex] = true;
				}
			} else {
				currentColor = getColour(code);
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	private static boolean isStyle(String code) {
		return getStyle(code) != -1;
	}
	
	private static int getStyle(String code) {
		switch (code.toLowerCase().charAt(1)) {
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
		case 'r':
			return 5;
		default:
			return -1;
		}
	}
	
	private static ChatColor getColour(String code) {
		return ChatColor.getByChar(code.toLowerCase().charAt(1));
	}
	
	private enum TagType {
		NONE,
		TTP,
		URL,
		CMD,
		SGT,
		INS;
		
		static final int TAG_LENGTH = 3;
		
	}
	
	private static TagType jsonTag(String node) {
		if (node.length() < TagType.TAG_LENGTH + 2) {
			return TagType.NONE;
		}
		switch (node.substring(0, 4)) {
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
	
	/**
	 * Removes all Json message formatting
	 * 
	 * @param json the input string
	 * @return a string stripped of all json tags
	 */
	public static String stripJson(String json) {
		StringBuilder builder = new StringBuilder();
		for (String s : json.split("||")) {
			if (jsonTag(s).equals(TagType.NONE)) {
				builder.append(s);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Parses Json messages based on RezzedUp's json.sk format. <br>
	 * <br>
	 * The following json tags are parsed: <code>ttp</code>, <code>url</code>, <code>cmd</code>, <code>sgt</code>, and <code>ins</code>. <br>
	 * <b>Colours are parsed according to '&' colour codes.</b>
	 * 
	 * @param json the input string
	 * @return a formatted BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] parseJson(String jsonable) {
		return parseColouredJsonProcessor(jsonable, AMPERSAND_PATTERN);
	}
	
	/**
	 * Converts a coloured AND formatted string into a {@link BaseComponent} array. <br>
	 * <br>
	 * See {@link #parseJson(String)}
	 * <b>Colours are parsed according to '§' colour codes.</b>
	 * 
	 * @param json the input string
	 * @return a formatted BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] parseColouredJson(String colouredJsonable) {
		return parseColouredJsonProcessor(colouredJsonable, SECTION_PATTERN);
	}
	
	private static BaseComponent[] parseColouredJsonProcessor(String jsonable, Pattern processor) {
		BaseComponent current = null;
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		for (String node : jsonable.split("||")) {
			TagType tag = jsonTag(node);
			if (tag.equals(TagType.NONE)) {
				if (current != null) {
					components.add(current);
				}
				current = new TextComponent(colourBungeeProcessor(node, processor));
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(TagType.TTP)) {
					current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, colourBungee(value)));
				} else if (tag.equals(TagType.URL)) {
					if (!value.startsWith("https://") && !value.startsWith("http://")) {
						value = "http://" + value;
					}
					current.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
				} else if (tag.equals(TagType.CMD)) {
					current.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
				} else if (tag.equals(TagType.SGT)) {
					current.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
				} else if (tag.equals(TagType.INS)) {
					current.setInsertion(value);
				}
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	/**
	 * Adds colour to a message. <br>
	 * <b>Similar to {@link #colour(String)}</b> but additionally converts to a Sponge Text object.
	 * 
	 * @param colourable the input string
	 * @return a coloured Text object
	 */
	@Platform(Platform.Type.SPONGE)
	public static Text colourSponge(String colourable) {
		return colourSpongeProcessor(colourable, AMPERSAND_PATTERN);
	}
	
	private static Text colourSpongeProcessor(String colourable, Pattern processor) {
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
		return builder.build();
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
	
	private static StringBuilder centeredMessageBuffer(String message) {
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
				continue;
			} else if (previousCode) {
				previousCode = false;
				isBold = c == 'l' || c == 'L';
				if (isBold) {
					continue;
				}
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		int halvedMessageSize = messagePxSize/2;
		int toCompensate = DefaultFontInfo.CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		StringBuilder builder = new StringBuilder();
		for (int compensated = 0; compensated < toCompensate; compensated += spaceLength) {
			builder.append(' ');
		}
		return builder;
	}
	
	/**
	 * Centers a message after it has been coloured with '§' colour codes. <br>
	 * <br>
	 * The centering is precise, taking into account individual character lengths and applied colour codes. <br>
	 * Messages with line breaks (<code>'\n'</code>) are treated properly; each individual line is centered.
	 * 
	 * @param message the message to center
	 * @return a centered message
	 */
	private static String center(String message){
		if (message == null || message.isEmpty()) {
			return message;
		} else if (message.contains("\n")) {
			/* 
			 * Handling with multi-line messages:
			 * 1. take the message apart according to new lines.
			 * 2. center each message.
			 * 3. concatenate the results.
			 */
			return StringsUtil.concat(CollectionsUtil.wrapAll(message.split("\n"), ChatUtil::center), '\n');
		}
		return centeredMessageBuffer(message).append(message).toString();
	}
	
	private enum DefaultFontInfo {

		A('A', 5),
		a('a', 5),
		B('B', 5),
		b('b', 5),
		C('C', 5),
		c('c', 5),
		D('D', 5),
		d('d', 5),
		E('E', 5),
		e('e', 5),
		F('F', 5),
		f('f', 4),
		G('G', 5),
		g('g', 5),
		H('H', 5),
		h('h', 5),
		I('I', 3),
		i('i', 1),
		J('J', 5),
		j('j', 5),
		K('K', 5),
		k('k', 4),
		L('L', 5),
		l('l', 1),
		M('M', 5),
		m('m', 5),
		N('N', 5),
		n('n', 5),
		O('O', 5),
		o('o', 5),
		P('P', 5),
		p('p', 5),
		Q('Q', 5),
		q('q', 5),
		R('R', 5),
		r('r', 5),
		S('S', 5),
		s('s', 5),
		T('T', 5),
		t('t', 4),
		U('U', 5),
		u('u', 5),
		V('V', 5),
		v('v', 5),
		W('W', 5),
		w('w', 5),
		X('X', 5),
		x('x', 5),
		Y('Y', 5),
		y('y', 5),
		Z('Z', 5),
		z('z', 5),
		NUM_1('1', 5),
		NUM_2('2', 5),
		NUM_3('3', 5),
		NUM_4('4', 5),
		NUM_5('5', 5),
		NUM_6('6', 5),
		NUM_7('7', 5),
		NUM_8('8', 5),
		NUM_9('9', 5),
		NUM_0('0', 5),
		EXCLAMATION_POINT('!', 1),
		AT_SYMBOL('@', 6),
		NUM_SIGN('#', 5),
		DOLLAR_SIGN('$', 5),
		PERCENT('%', 5),
		UP_ARROW('^', 5),
		AMPERSAND('&', 5),
		ASTERISK('*', 5),
		LEFT_PARENTHESIS('(', 4),
		RIGHT_PERENTHESIS(')', 4),
		MINUS('-', 5),
		UNDERSCORE('_', 5),
		PLUS_SIGN('+', 5),
		EQUALS_SIGN('=', 5),
		LEFT_CURL_BRACE('{', 4),
		RIGHT_CURL_BRACE('}', 4),
		LEFT_BRACKET('[', 3),
		RIGHT_BRACKET(']', 3),
		COLON(':', 1),
		SEMI_COLON(';', 1),
		DOUBLE_QUOTE('"', 3),
		SINGLE_QUOTE('\'', 1),
		LEFT_ARROW('<', 4),
		RIGHT_ARROW('>', 4),
		QUESTION_MARK('?', 5),
		SLASH('/', 5),
		BACK_SLASH('\\', 5),
		LINE('|', 1),
		TILDE('~', 5),
		TICK('`', 2),
		PERIOD('.', 1),
		COMMA(',', 1),
		SPACE(' ', 3),
		DEFAULT('a', 4);
		
		private final static int CENTER_PX = 154;
		
		private char character;
		private int length;
		
		private DefaultFontInfo(char character, int length) {
			this.character = character;
			this.length = length;
		}
		
		private char getCharacter(){
			return this.character;
		}
		
		int getLength(){
			return this.length;
		}
		
		int getBoldLength(){
			if(this == DefaultFontInfo.SPACE) return this.getLength();
			return this.length + 1;
		}
		
		static DefaultFontInfo getDefaultFontInfo(char c){
			for(DefaultFontInfo dFI : DefaultFontInfo.values()){
				if(dFI.getCharacter() == c) return dFI;
			}
			return DefaultFontInfo.DEFAULT;
		}
	}
}
