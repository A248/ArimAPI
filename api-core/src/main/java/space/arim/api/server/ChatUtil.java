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

import space.arim.api.annotation.Platform;

/**
 * Utility class for formatting chat messages and parsing colors. <br>
 * Accepts uppercase and lowercase chars. <br>
 * <br>
 * <b>Notice:</b> <br>
 * Some methods are platform dependent! It is recommended to instead use {@link SpigotUtil}, {@link BungeeUtil}, or {@link SpongeUtil} depending on your platform. <br>
 * <br>
 * <b>Color Parsing:</b> <br>
 * * Parses '&' color codes. <br>
 * * Spigot usage: {@link #color(String)}. <br>
 * * BungeeCord usage: {@link #colorBungee(String)}. <br>
 * * Sponge usage: {@link #colorSponge(String)}. <br>
 * * Removing color: {@link #stripColor(String)}. <br>
 * * Converting from '§' color codes: {@link #replaceColor(String)}. <br>
 * <br>
 * <b>Json Messages:</b> <br>
 * * Parses json based on RezzedUp's json.sk format <br>
 * * Includes color parsing. <br>
 * * Requires <i>Spigot</i> or <i>BungeeCord</i>. <br>
 * * Usage: {@link #parseJson(String)}. <br>
 * * Removing formatting: {@link #stripJson(String)}
 * 
 * @author A248
 *
 */
public final class ChatUtil {

	private ChatUtil() {}
	
	private static final Pattern RAW_COLOR_PATTERN = Pattern.compile("§[0-9A-Fa-fK-Rk-r]]");
	private static final Pattern COLOR_PATTERN = Pattern.compile("&[0-9A-Fa-fK-Rk-r]]");
	
	/**
	 * Replaces valid '§' color codes. <br>
	 * 
	 * @param colored the input string
	 * @return the same string with '&' color codes instead
	 */
	public static String replaceColor(String colored) {
		return RAW_COLOR_PATTERN.matcher(colored).replaceAll("&$2");
	}
	
	/**
	 * Removes color from a message.
	 * 
	 * @param colorable the string to remove color from
	 * @return an uncoloured string
	 */
	public static String stripColor(String colorable) {
		return COLOR_PATTERN.matcher(colorable).replaceAll("");
	}
	
	/**
	 * Adds color to a message. <br>
	 * The <i>input</i> uses '&' color codes. <br>
	 * The <i>result</i> uses '§' color codes.
	 * 
	 * @param colorable the string to add color to
	 * @return the colored string
	 */
	public static String color(String colorable) {
		return COLOR_PATTERN.matcher(colorable).replaceAll("§$2");
	}
	
	/**
	 * Adds color to a message. <br>
	 * <b>Similar to {@link #color(String)}</b> but additionally converts to a BaseComponent array.
	 * 
	 * @param colorable the input string
	 * @return a colored BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] colorBungee(String colorable) {
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		
		/*
		 * Approach:
		 * Group the input string by segments of text separated by formatting codes.
		 * When a match is found, the current segment is the text before the current match (but after the previous match).
		 * Add the current segment to the builder using the current color and styles.
		 * Then, update the current color and styles according to the match.
		 */
		Matcher matcher = ChatUtil.COLOR_PATTERN.matcher(colorable);
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
			String segment = colorable.substring(beginIndex, matcher.start());
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
				currentColor = getColor(code);
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	private static boolean isStyle(String code) {
		return getStyle(code) != -1;
	}
	
	private static int getStyle(String code) {
		switch (code.toLowerCase()) {
		case "&k":
			return 0;
		case "&l":
			return 1;
		case "&m":
			return 2;
		case "&n":
			return 3;
		case "&o":
			return 4;
		case "&r":
			return 5;
		default:
			return -1;
		}
	}
	
	private static ChatColor getColor(String code) {
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
	 * <b>Usage:</b> <br>
	 * 
	 * @param json the input string
	 * @return a formatted BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] parseJson(String jsonable) {
		return parseColoredJson(color(jsonable));
	}
	
	/**
	 * Converts a colored AND formatted string into a {@link BaseComponent} array. <br>
	 * <br>
	 * See {@link #parseJson(String)}
	 * <b>Colors are parsed according to '§' color codes.</b>
	 * 
	 * @param json the input string
	 * @return a formatted BaseComponent array
	 */
	@Platform({Platform.Type.BUNGEE, Platform.Type.SPIGOT})
	public static BaseComponent[] parseColoredJson(String coloredJsonable) {
		BaseComponent current = null;
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		for (String node : coloredJsonable.split("||")) {
			TagType tag = jsonTag(node);
			if (tag.equals(TagType.NONE)) {
				if (current != null) {
					components.add(current);
				}
				current = new TextComponent(colorBungee(node));
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(TagType.TTP)) {
					current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, colorBungee(value)));
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
	 * Adds color to a message. <br>
	 * <b>Similar to {@link #color(String)}</b> but additionally converts to a Sponge Text object.
	 * 
	 * @param colorable the input string
	 * @return a colored Text object
	 */
	@Platform(Platform.Type.SPONGE)
	public static Text colorSponge(String colorable) {
		Text.Builder builder = Text.builder();
		
		/*
		 * Approach:
		 * Group the input string by segments of text separated by formatting codes.
		 * When a match is found, the current segment is the text before the current match (but after the previous match).
		 * Add the current segment to the builder using the current color and styles.
		 * Then, update the current color and styles according to the match.
		 */
		Matcher matcher = ChatUtil.COLOR_PATTERN.matcher(colorable);
		int beginIndex = 0; // the starting index of the current segment
		
		// start without any formatting
		ArrayList<TextStyle> currentStyles = new ArrayList<TextStyle>();
		currentStyles.add(TextStyles.NONE);
		TextColor currentColor = TextColors.NONE;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = colorable.substring(beginIndex, matcher.start());
			builder.append(Text.builder(segment).color(currentColor).style(currentStyles.toArray(new TextStyle[] {})).build());
			
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
				currentColor = getSpongeColor(code); // just replace the old color with the new running color
			}
		}
		return builder.build();
	}
	
	private static boolean isSpongeStyle(String code) {
		return getSpongeStyle(code) != TextStyles.NONE;
	}
	
	private static TextStyle getSpongeStyle(String code) {
		switch (code.toLowerCase()) {
		case "&k":
			return TextStyles.OBFUSCATED;
		case "&l":
			return TextStyles.BOLD;
		case "&m":
			return TextStyles.STRIKETHROUGH;
		case "&n":
			return TextStyles.UNDERLINE;
		case "&o":
			return TextStyles.ITALIC;
		case "&r":
			return TextStyles.RESET;
		default:
			return TextStyles.NONE;
		}
	}
	
	private static TextColor getSpongeColor(String code) {
		switch (code.toLowerCase()) {
		case "&0":
			return TextColors.BLACK;
		case "&1":
			return TextColors.DARK_BLUE;
		case "&2":
			return TextColors.DARK_GREEN;
		case "&3":
			return TextColors.AQUA;
		case "&4":
			return TextColors.DARK_RED;
		case "&5":
			return TextColors.DARK_PURPLE;
		case "&6":
			return TextColors.GOLD;
		case "&7":
			return TextColors.GRAY;
		case "&8":
			return TextColors.DARK_GRAY;
		case "&9":
			return TextColors.DARK_AQUA;
		case "&a":
			return TextColors.GREEN;
		case "&b":
			return TextColors.BLUE;
		case "&c":
			return TextColors.RED;
		case "&d":
			return TextColors.LIGHT_PURPLE;
		case "&e":
			return TextColors.YELLOW;
		case "&f":
			return TextColors.WHITE;
		case "&r":
			return TextColors.RESET;
		default:
			return TextColors.NONE;
		}
	}
	
}
