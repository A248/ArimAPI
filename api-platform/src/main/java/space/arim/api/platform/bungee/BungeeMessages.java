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
package space.arim.api.platform.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.api.chat.Colour;
import space.arim.api.chat.Component;
import space.arim.api.chat.Format;
import space.arim.api.chat.Format.FormatCatalog;
import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.platform.AbstractPlatformMessages;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonComponentBuilder;
import space.arim.api.chat.Message;
import space.arim.api.chat.Style;
import space.arim.api.util.LazySingleton;

/**
 * BungeeCord messages utility. Use {@link #get()} to get the instance.
 * <br>
 * Enables compatibility with the BungeeCord/Spigot Chat Component API (CCA). <br>
 * Contains mirror methods for converting between respective API classes. <br>
 * <br>
 * Use {@link #get()} to retrieve the instance for usage. <br>
 * <br>
 * <b>net.md_5.bungee.api.ChatColor vs org.bukkit.ChatColor</b> <br>
 * The Bukkit class org.bukkit.ChatColor is a legacy class retained for backwards compatibility.
 * Since net.md_5.bungee.api.ChatColor is included in Spigot and BungeeCord, plugins should use it instead.
 * Plugins may use <code>colour.asBungee()</code> for conversion, where <i>colour</i> is an instance of org.bukkit.ChatColor.
 * <br>
 * <b>BungeeCord/Spigot Chat Component API</b>: Comparison with ArimAPI <br>
 * <br>
 * <i>Colours and Styles</i> <br>
 * Each API explicitly define chat colours and style formatting. However, ArimAPI distinguishes clearly
 * between a {@link Colour} and {@link Style}, both of which are subclasses of {@link Format}. <br>
 * The CCA makes no such distinction; all of its colours and formatting are contained in <code>ChatColor</code>. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. The CCA has equivalent support for each of these.
 * The CCA includes additional <code>HoverEvent.Action</code> options: <code>HoverEvent.Action.SHOW_ACHIEVEMENT</code>, <code>SHOW_ENTITY</code>, and <code>SHOW_ITEM</code>.
 * For hover actions, ArimAPI only supports displaying tooltip messages. The tooltip, of course, may use colours and styles.
 * Thus, there is ONLY an equivalent for <code>HoverEvent.Action.SHOW_TEXT</code>, but none other of the <code>HoverEvent.Action</code> values. <br>
 * The CCA includes additional <code>ClickEvent.Action</code> options: <code>ClickEvent.Action.CHANGE_PAGE</code> and <code>OPEN_FILE</code>.
 * For click actions, ArimAPI only supports urls, commands, and suggestions. Thus, there are no equivalents for <code>ClickEvent.Action.CHANGE_PAGE</code> and <code>OPEN_FILE</code>.
 * 
 * @author A248
 *
 */
public class BungeeMessages extends AbstractPlatformMessages<BaseComponent[]> {
	
	private static final LazySingleton<BungeeMessages> INST = new LazySingleton<BungeeMessages>(BungeeMessages::new);
	
	protected BungeeMessages() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static BungeeMessages get() {
		return INST.get();
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Formatting codes <br>
	 * Converts from a {@link Format} to a <code>ChatColor</code>.
	 * 
	 * @param format the source format
	 * @return the equivalent <code>ChatColor</code>
	 */
	public ChatColor convert(Format format) {
		return (format != null) ? ChatColor.getByChar(format.identifier()) : null;
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Formatting codes <br>
	 * Converts from a <code>ChatColor</code> to a {@link Format}.
	 * 
	 * @param colour the source format
	 * @return the equivalent <code>Format</code>
	 */
	public Format convert(ChatColor colour) {
		if (colour != null) {
			for (FormatCatalog entry : FormatCatalog.values()) {
				if (convert(entry.getFormatValue()).equals(colour)) {
					return entry.getFormatValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Message components <br>
	 * Converts from a {@link Component} to a <code>BaseComponent</code>
	 * 
	 * @param component the source component
	 * @return an equivalent <code>BaseComponent</code>
	 */
	public BaseComponent convert(Component component) {
		if (component == null) {
			return null;
		}
		TextComponent comp = new TextComponent(component.getText());
		comp.setColor(convert(component.getColour()));
		comp.setObfuscated(component.hasStyle(Style.MAGIC));
		comp.setBold(component.hasStyle(Style.BOLD));
		comp.setStrikethrough(component.hasStyle(Style.STRIKETHROUGH));
		comp.setUnderlined(component.hasStyle(Style.UNDERLINE));
		comp.setItalic(component.hasStyle(Style.ITALIC));
		if (component instanceof JsonComponent) {
			JsonComponent json = (JsonComponent) component;
			if (json.hasTooltip()) {
				comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, convert(json.getTooltip())));
			}
			if (json.hasUrl()) {
				comp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, json.getUrl()));
			} else if (json.hasCommand()) {
				comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, json.getCommand()));
			} else if (json.hasSuggestion()) {
				comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, json.getSuggestion()));
			}
			if (json.hasSuggestion()) {
				comp.setInsertion(json.getInsertion());
			}
		}
		return comp;
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Message components <br>
	 * Converts from a <code>BaseComponent</code> to a {@link Component}
	 * 
	 * @param component the source component
	 * @return an equivalent <code>Component</code>
	 */
	public Component convert(BaseComponent component) {
		if (component == null) {
			return null;
		}
		JsonComponentBuilder builder = new JsonComponentBuilder();
		ClickEvent click = component.getClickEvent();
		if (click != null) {
			switch (click.getAction()) {
			case OPEN_URL:
				builder.url(click.getValue());
				break;
			case RUN_COMMAND:
				builder.command(click.getValue());
				break;
			case SUGGEST_COMMAND:
				builder.suggest(click.getValue());
				break;
			default:
				break;
			}
		}
		Format format = convert(component.getColor());
		HoverEvent hover = component.getHoverEvent();
		return builder.colour((format instanceof Colour) ? (Colour) format : null)
				.style(Style.MAGIC, component.isObfuscated()).style(Style.BOLD, component.isBold())
				.style(Style.STRIKETHROUGH, component.isStrikethrough())
				.style(Style.UNDERLINE, component.isObfuscated()).style(Style.ITALIC, component.isObfuscated())
				.tooltip((hover != null && hover.getAction().equals(HoverEvent.Action.SHOW_TEXT))
						? convert(hover.getValue())
						: null)
				.insert(component.getInsertion()).build();
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Messages <br>
	 * Converts from a {@link Message} to a <code>BaseComponent</code> array
	 * 
	 * @param message the source message
	 * @return an equivalent <code>BaseComponent</code> array
	 */
	@Override
	public BaseComponent[] convert(Message message) {
		return (message == null) ? null : Arrays.stream(message.getComponents()).map(this::convert).toArray(BaseComponent[]::new);
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Messages <br>
	 * Converts from a <code>BaseComponent</code> array to a {@link Message}
	 * 
	 * @param message the source message
	 * @return an equivalent <code>Message</code>
	 */
	@Override
	public Message convert(BaseComponent[] message) {
		return (message == null) ? null : (new Message(Arrays.stream(message).map(this::convert).toArray(Component[]::new))).clean();
	}
	
	@Override
	public BaseComponent[] colour(String msg, FormattingCodePattern formattingPattern) {
		return colourProcessor(msg, formattingPattern.getValue());
	}
	
	@Override
	public BaseComponent[] uncoloured(String msg) {
		return new BaseComponent[] {new TextComponent(msg)};
	}
	
	@Override
	public BaseComponent[] parseJson(String msg, FormattingCodePattern formattingPattern) {
		return parseJsonProcessor(msg, formattingPattern.getValue());
	}
	
	@Override
	public BaseComponent[] parseUncolouredJson(String msg) {
		return parseJsonProcessor(msg, TextComponent::new, this::uncoloured);
	}
	
	private static BaseComponent[] parseJsonProcessor(String msg, Pattern processor) {
		return parseJsonProcessor(msg, (node) -> new TextComponent(colourProcessor(node, processor)), (node) -> colourProcessor(node, processor));
	}
	
	private static BaseComponent[] parseJsonProcessor(String msg, Function<String, TextComponent> nodeGenerator, Function<String, BaseComponent[]> tooltipGenerator) {
		BaseComponent current = null;
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		for (String node : msg.split("||")) {
			TagType tag = jsonTag(node);
			if (tag.equals(TagType.NONE)) {
				if (current != null) {
					components.add(current);
				}
				current = nodeGenerator.apply(node);
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(TagType.TTP)) {
					
					current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltipGenerator.apply(value)));
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
	
}
