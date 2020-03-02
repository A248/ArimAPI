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
package space.arim.api.platform.sponge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import space.arim.universal.util.collections.ArraysUtil;

import space.arim.api.chat.Colour;
import space.arim.api.chat.Colour.ColourCatalog;
import space.arim.api.platform.AbstractPlatformMessages;
import space.arim.api.chat.FormattingCodePattern;
import space.arim.api.chat.Component;
import space.arim.api.chat.Format;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonMessageBuilder;
import space.arim.api.chat.JsonTag;
import space.arim.api.chat.Message;
import space.arim.api.chat.Style;
import space.arim.api.util.LazySingleton;

/**
 * Enables compatibility with the Sponge API. <br>
 * Contains mirror methods for converting between respective API classes. <br>
 * <br>
 * Use {@link #get()} to retrieve the instance for usage. <br>
 * <br>
 * <b>Sponge Text API</b>: Comparison with ArimAPI <br>
 * <br>
 * <i>Colours and Styles</i> <br>
 * ArimAPI uses <code>null</code> values to designate a lack of colouring in a message. The Sponge API avoids <code>null</code> at all costs.
 * Accordingly, its <code>TextColors.NONE</code> and <code>TextStyles.NONE</code> translate to <code>null</code> in ArimAPI.
 * Also, Sponge's <code>TextStyle</code> represents a composite of multiple styles, whereas ArimAPI simply uses an array of <code>Style</code>s. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. The Sponge API has equivalent support for each of these. 
 * The Sponge API includes additional hover event and click event actions.
 * 
 * @author A248
 *
 */
public class SpongeMessages extends AbstractPlatformMessages<Text> {
	
	private static final LazySingleton<SpongeMessages> INST = new LazySingleton<SpongeMessages>(() -> new SpongeMessages());
	
	protected SpongeMessages() {}
	
	/**
	 * Gets the main instance of BungeeMessages
	 * 
	 * @return the instance
	 */
	public static SpongeMessages get() {
		return INST.get();
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Formatting codes <br>
	 * Converts from a {@link Colour} to a <code>TextColor</code>.
	 * 
	 * @param colour the source colour
	 * @return the equivalent <code>TextColor</code>
	 */
	public TextColor convert(Colour colour) {
		if (colour == null) {
			return TextColors.NONE;
		}
		switch (ColourCatalog.valueOf(colour)) {
		case BLACK:
			return TextColors.BLACK;
		case DARK_BLUE:
			return TextColors.DARK_BLUE;
		case DARK_GREEN:
			return TextColors.DARK_GREEN;
		case DARK_AQUA:
			return TextColors.DARK_AQUA;
		case DARK_RED:
			return TextColors.DARK_RED;
		case DARK_PURPLE:
			return TextColors.DARK_PURPLE;
		case GOLD:
			return TextColors.GOLD;
		case GRAY:
			return TextColors.GRAY;
		case DARK_GRAY:
			return TextColors.DARK_GRAY;
		case BLUE:
			return TextColors.BLUE;
		case GREEN:
			return TextColors.GREEN;
		case AQUA:
			return TextColors.AQUA;
		case RED:
			return TextColors.RED;
		case LIGHT_PURPLE:
			return TextColors.LIGHT_PURPLE;
		case YELLOW:
			return TextColors.YELLOW;
		case WHITE:
			return TextColors.WHITE;
		default:
			throw new IllegalStateException("No corresponding TextColor for &" + colour.identifier());
		}
	}
	
	/**
	 * <b>Sponge API {@literal -}{@literal >} ArimAPI</b>: Formatting codes <br>
	 * Converts from a <code>TextColor</code> to a {@link Colour}. <br>
	 * <br>
	 * Note that there is no equivalent in ArimAPI for <code>TextColors.NONE</code> and <code>TextColors.REST</code>,
	 * so <code>null</code> will be returned if either of these <code>TextColor</code>s are passed as parameters.
	 * 
	 * @param colour the source colour
	 * @return the equivalent {@link Colour}, or <code>null</code> if there is none
	 */
	public Colour convert(TextColor colour) {
		if (colour != null) {
			for (ColourCatalog entry : ColourCatalog.values()) {
				if (convert(entry.getColourValue()).getColor().equals(colour.getColor())) {
					return entry.getColourValue();
				}
			}
		}
		return null;
	}
	
	private <T> TextStyle convertStyles(T styles, BiPredicate<T, Style> checker) {
		return (styles == null) ? null : new TextStyle(checker.test(styles, Style.BOLD), checker.test(styles, Style.ITALIC), checker.test(styles, Style.UNDERLINE), checker.test(styles, Style.STRIKETHROUGH), checker.test(styles, Style.MAGIC));
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Formatting codes <br>
	 * Converts from a {@link Style} to a <code>TextStyle</code>.
	 * 
	 * @param styles the source style set
	 * @return an equivalent <code>TextStyle</code>
	 */
	public TextStyle convert(Set<Style> styles) {
		return convertStyles(styles, (styleSet, style) -> styleSet.contains(style));
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Formatting codes <br>
	 * Converts from a {@link Style} to a <code>TextStyle</code>.
	 * 
	 * @param styles the source style array
	 * @return an equivalent <code>TextStyle</code>
	 */
	public TextStyle convert(Style...styles) {
		return convertStyles(styles, ArraysUtil::contains);
	}
	
	/**
	 * <b>Sponge API {@literal -}{@literal >} ArimAPI</b>: Formatting codes <br>
	 * Converts from a <code>TextStyle</code> to a {@link Style}.
	 * 
	 * @param styling the source <code>TextStyle</code>
	 * @return an equivalent set of <code>Style</code>s
	 */
	public Set<Style> convert(TextStyle styling) {
		if (styling == null) {
			return null;
		}
		Set<Style> styles = new HashSet<Style>();
		if (styling.contains(TextStyles.OBFUSCATED)) {
			styles.add(Style.MAGIC);
		}
		if (styling.contains(TextStyles.BOLD)) {
			styles.add(Style.BOLD);
		}
		if (styling.contains(TextStyles.STRIKETHROUGH)) {
			styles.add(Style.STRIKETHROUGH);
		}
		if (styling.contains(TextStyles.UNDERLINE)) {
			styles.add(Style.UNDERLINE);
		}
		if (styling.contains(TextStyles.ITALIC)) {
			styles.add(Style.ITALIC);
		}
		return styles;
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Formatting codes <br>
	 * Converts from a {@link Format} to a <code>TextFormat</code>.
	 * 
	 * @param formats the source format array
	 * @return an equivalent <code>TextFormat</code>
	 */
	public TextFormat convert(Format...formats) {
		if (formats == null) {
			return null;
		} else if (formats.length == 0) {
			Format format = formats[0];
			return (format instanceof Style) ? TextFormat.of(convert(new Style[] {(Style) format})) : (format instanceof Colour) ? TextFormat.of(convert((Colour) format)) : TextFormat.NONE;
		}
		TextColor colour = TextColors.NONE;
		TextStyle style = TextStyles.NONE;
		for (Format format : formats) {
			if (format instanceof Style) {
				style = style.and(convert(new Style[] {(Style) format}));
			} else if (format instanceof Colour) {
				colour = convert((Colour) format);
			}
		}
		return TextFormat.of(colour, style);
	}
	
	/**
	 * <b>Sponge API {@literal -}{@literal >} ArimAPI</b>: Formatting codes <br>
	 * Converts from a <code>TextFormat</code> to a {@link Format}.
	 * 
	 * @param format the source <code>TextFormat</code>
	 * @return an equivalent set of <code>Format</code>s
	 */
	public Set<Format> convert(TextFormat format) {
		if (format == null) {
			return null;
		}
		Set<Format> formatting = new HashSet<Format>();
		formatting.addAll(convert(format.getStyle()));
		Colour colour = convert(format.getColor());
		if (colour != null) {
			formatting.add(colour);
		}
		return formatting;
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Messages <br>
	 * Converts from a {@link Component} to a <code>Text</code>.
	 * 
	 * @param component the source component
	 * @return an equivalent <code>Text</code>
	 */
	public Text convert(Component component) {
		if (component == null) {
			return null;
		}
		Text.Builder builder = Text.builder(component.getText()).color(convert(component.getColour())).style(convert(component.getStyles()));
		if (component instanceof JsonComponent) {
			JsonComponent json = (JsonComponent) component;
			if (json.hasTooltip()) {
				builder.onHover(TextActions.showText(convert(json.getTooltip())));
			}
			if (json.hasUrl()) {
				try {
					builder.onClick(TextActions.openUrl(new URL(json.getUrl())));
				} catch (MalformedURLException ignored) {}
			} else if (json.hasCommand()) {
				builder.onClick(TextActions.runCommand(json.getCommand()));
			} else if (json.hasSuggestion()) {
				builder.onClick(TextActions.suggestCommand(json.getSuggestion()));
			}
			if (json.hasInsertion()) {
				builder.onShiftClick(TextActions.insertText(json.getInsertion()));
			}
		}
		return builder.build();
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} Sponge API</b>: Messages <br>
	 * Converts from a {@link Message} to a <code>Text</code>.
	 * 
	 * @param message the source message
	 * @return an equivalent <code>Text</code>
	 */
	@Override
	public Text convert(Message message) {
		if (message == null) {
			return null;
		}
		Text.Builder builder = Text.builder();
		for (Component component : message.getComponents()) {
			builder.append(convert(component), Text.builder().color(TextColors.RESET).style(TextStyles.RESET).build());
		}
		return builder.build();
	}
	
	private void addAllContent(JsonMessageBuilder builder, Text message) {
		builder.reset().colour(convert(message.getColor())).style(Style.MAGIC, message.getStyle().isObfuscated().orElse(false)).style(Style.BOLD, message.getStyle().isBold().orElse(false)).style(Style.STRIKETHROUGH, message.getStyle().hasStrikethrough().orElse(false)).style(Style.UNDERLINE, message.getStyle().hasUnderline().orElse(false)).style(Style.ITALIC, message.getStyle().isItalic().orElse(false));
		if (message instanceof LiteralText) {
			builder.add(((LiteralText) message).getContent());
		}
		message.getHoverAction().ifPresent((hover) -> {
			if (hover instanceof HoverAction.ShowText) {
				builder.tooltip(convert((Text) hover.getResult()));
			}
		});
		message.getClickAction().ifPresent((click) -> {
			if (click instanceof ClickAction.OpenUrl) {
				builder.url(((URL) click.getResult()).toString());
			} else if (click instanceof ClickAction.RunCommand) {
				builder.command((String) click.getResult());
			} else if (click instanceof ClickAction.SuggestCommand) {
				builder.suggest((String) click.getResult());
			}
		});
		message.getShiftClickAction().ifPresent((shiftclick) -> {
			if (shiftclick instanceof ShiftClickAction.InsertText) {
				builder.insertion((String) shiftclick.getResult());
			}
		});
		for (Text child : message.getChildren()) {
			addAllContent(builder, child);
		}
	}
	
	/**
	 * <b>Sponge API {@literal -}{@literal >} ArimAPI</b>: Messages <br>
	 * Converts from a <code>Text</code> to a {@link Message}.
	 * 
	 * @param message the source message
	 * @return an equivalent <code>Message</code>
	 */
	@Override
	public Message convert(Text message) {
		if (message == null) {
			return null;
		}
		JsonMessageBuilder builder = new JsonMessageBuilder();
		addAllContent(builder, message);
		return builder.cleanBuild();
	}
	
	@Override
	public Text colour(String msg, FormattingCodePattern formattingPattern) {
		return colourProcessor(msg, formattingPattern.getValue()).build();
	}
	
	@Override
	public Text uncoloured(String msg) {
		return Text.of(msg);
	}
	
	@Override
	public Text parseJson(String msg, FormattingCodePattern formattingPattern) {
		return parseJsonProcessor(msg, formattingPattern.getValue());
	}
	
	@Override
	public Text parseUncolouredJson(String msg) {
		return parseJsonProcessor(msg, Text::builder);
	}
	
	private static Text parseJsonProcessor(String msg, Pattern processor) {
		return parseJsonProcessor(msg, (node) -> colourProcessor(node, processor));
	}
	
	private static Text parseJsonProcessor(String msg, Function<String, ? extends Text.Builder> generator) {
		Text.Builder current = null;
		Text.Builder parent = Text.builder();
		for (String node : msg.split("||")) {
			JsonTag tag = JsonTag.getFor(node);
			if (tag.equals(JsonTag.NONE)) {
				if (current != null) {
					parent.append(current.build());
				}
				current = generator.apply(msg);
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(JsonTag.TTP)) {
					current.onHover(TextActions.showText(generator.apply(value).build()));
				} else if (tag.equals(JsonTag.URL)) {
					if (!value.startsWith("https://") && !value.startsWith("http://")) {
						value = "http://" + value;
					}
					try {
						current.onClick(TextActions.openUrl(new URL(value)));
					} catch (MalformedURLException ex) {}
				} else if (tag.equals(JsonTag.CMD)) {
					current.onClick(TextActions.runCommand(value));
				} else if (tag.equals(JsonTag.SGT)) {
					current.onClick(TextActions.suggestCommand(value));
				} else if (tag.equals(JsonTag.INS)) {
					current.onShiftClick(TextActions.insertText(value));
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
		ArrayList<TextStyle> currentStyles = new ArrayList<TextStyle>();
		currentStyles.add(TextStyles.NONE);
		TextColor currentColour = TextColors.NONE;
		
		while (matcher.find()) {
			// get the current segment and add it to the builder
			String segment = msg.substring(beginIndex, matcher.start());
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
