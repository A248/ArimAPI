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
package space.arim.api.env.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

/**
 * Enables compatibility with the BungeeCord/Spigot Chat Component API. <br>
 * <br>
 * The BungeeCord Component API is also included on Spigot. It is both the BungeeCord proxy's
 * and the Spigot server's JSON message API, a welcoming sight of API unity. The Bungee Component API
 * has breaking changes across versions, making it difficult to maintain compatibility with multiple
 * Bungee Component API versions, such as those present in older versions of Spigot. Moreover, its objects
 * are mutable, requiring cloning should they be safely exposed as public return types, and may be initialised
 * with nullable state, where the behaviour of {@code null} is not well documented. <br>
 * <br>
 * <b>Comparison with ArimAPI</b> <br>
 * <br>
 * <i>Colourful messages</i> <br>
 * With regards to chat colours and style formatting, ArimAPI distinguishes clearly between colouring and styling.
 * {@code ChatColor} makes no such distinction; all of its colours and formatting are contained within. ArimAPI fully
 * supports hex colours, just as Bungee Components do. However, older versions of the latter do not support arbitrary
 * hex colours, in which case conversion uses {@link space.arim.api.chat.PredefinedColour#getNearestTo(int)}. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. Bungee Components have equivalent support for each
 * of these. However, some features from the Bungee API are not available in ArimAPI. <br>
 * <br>
 * For hover actions, ArimAPI only supports displaying tooltip messages. Thus, there is only an equivalent for
 * {@code HoverEvent.Action.SHOW_TEXT}, but other {@code HoverEvent.Action}s are ommitted during conversion. <br>
 * <br>
 * For click actions, ArimAPI only supports urls, commands, and suggestions. There are no equivalents for the others,
 * which are ignored. <br>
 * <br>
 * <i>Nontext Messages</i> <br>
 * ArimAPI only supports components which are text based. Hence, the only subclass of {@code BaseComponent} supported is
 * {@code TextComponent}. When another kind of component is encountered, it is skipped. <br>
 * <br>
 * <i>Conversion Policy</i> <br>
 * When there are no equivalents for a Bungee Component API feature, it is simply discarded from the final result. <br>
 * <br>
 * <i>Nullness Policy</i> <br>
 * The Bungee Component API seems to omit null checking preconditions. Null parameters are rejected by this class, including
 * null array elements. Generally, objects from the Bungee Component API with null fields where null is not a defined result
 * are treated as if null indicates the lack of the relevant feature.
 * 
 * @author A248
 *
 */
public class BungeeComponentConverter implements PlatformMessageAdapter<TextComponent[]> {
	
	/**
	 * Creates an instance
	 * 
	 */
	public BungeeComponentConverter() {}
	
	/*
	 * 
	 * Converting from ArimAPI
	 * 
	 */
	
	private static ClickEvent.Action convertClickType(JsonClick.Type jsonClickType) {
		switch (jsonClickType) {
		case RUN_COMMAND:
			return ClickEvent.Action.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return ClickEvent.Action.SUGGEST_COMMAND;
		case OPEN_URL:
			return ClickEvent.Action.OPEN_URL;
		default:
			throw new IllegalStateException("Not implemented for " + jsonClickType);
		}
	}
	
	private static TextComponent convertComponent(TextualComponent component) {
		TextComponent result = new TextComponent(component.getText());

		result.setColor(BungeeColourConversions.convertColour(component.getColour()));
		result.setObfuscated(component.hasStyle(MessageStyle.MAGIC));
		result.setBold(component.hasStyle(MessageStyle.BOLD));
		result.setStrikethrough(component.hasStyle(MessageStyle.STRIKETHROUGH));
		result.setUnderlined(component.hasStyle(MessageStyle.UNDERLINE));
		result.setItalic(component.hasStyle(MessageStyle.ITALIC));

		if (component instanceof JsonComponent) {
			JsonComponent jsonComp = (JsonComponent) component;

			JsonHover hover = jsonComp.getHoverAction();
			if (hover != null) {
				BaseComponent[] hoverComponents = convertFrom0(hover.getTooltip()).toArray(new BaseComponent[] {});
				result.setHoverEvent(BungeeTooltipConversions.createTooltip(hoverComponents));
			}
			JsonClick click = jsonComp.getClickAction();
			if (click != null) {
				result.setClickEvent(new ClickEvent(convertClickType(click.getType()), click.getValue()));
			}
			JsonInsertion insertion = jsonComp.getInsertionAction();
			if (insertion != null) {
				result.setInsertion(insertion.getValue());
			}
		}
		return result;
	}
	
	private static List<TextComponent> convertFrom0(SendableMessage message) {
		List<TextComponent> result = new ArrayList<>();
		for (TextualComponent component : message.getComponents()) {
			result.add(convertComponent(component));
		}
		return result;
	}
	
	@Override
	public TextComponent[] convertFrom(SendableMessage message) {
		Objects.requireNonNull(message, "Message must not be null");

		return convertFrom0(message).toArray(new TextComponent[] {});
	}
	
	/*
	 * 
	 * Converting to ArimAPI
	 * 
	 */
	
	private static JsonClick.Type convertClickType(ClickEvent.Action clickEventAction) {
		if (clickEventAction == null) {
			return null;
		}
		switch (clickEventAction) {
		case RUN_COMMAND:
			return JsonClick.Type.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return JsonClick.Type.SUGGEST_COMMAND;
		case OPEN_URL:
			return JsonClick.Type.OPEN_URL;
		default:
			// Not supported
			return null;
		}
	}
	
	private static JsonClick convertClick(ClickEvent clickEvent) {
		if (clickEvent == null) {
			return null;
		}
		String value = clickEvent.getValue();
		if (value == null) {
			return null;
		}
		JsonClick.Type type = convertClickType(clickEvent.getAction());
		if (type == null) {
			return null;
		}
		return new JsonClick(type, value);
	}
	
	private static void addAllContent(SendableMessage.Builder parentBuilder, TextComponent component) {
		JsonComponent.Builder builder = new JsonComponent.Builder();
		builder.text(component.getText());

		builder.colour(BungeeColourConversions.convertColour(component.getColor()));
		builder.styles(BungeeColourConversions.convertStyles(component));

		SendableMessage hoverTooltip = BungeeTooltipConversions.convert(component.getHoverEvent());
		if (hoverTooltip != null) {
			builder.hoverAction(new JsonHover(hoverTooltip));
		}
		builder.clickAction(convertClick(component.getClickEvent()));
		String insertion = component.getInsertion();
		if (insertion != null) {
			builder.insertionAction(new JsonInsertion(insertion));
		}
		parentBuilder.add(builder.build());

		List<BaseComponent> extra = component.getExtra();
		if (extra != null) {
			addAllIterativeContent(parentBuilder, extra.toArray(new BaseComponent[] {}));
		}
	}
	
	static void addAllIterativeContent(SendableMessage.Builder parentBuilder, BaseComponent[] components) {
		for (BaseComponent component : components) {
			if (component instanceof TextComponent) {
				addAllContent(parentBuilder, (TextComponent) component);
			}
		}
	}
	
	static SendableMessage convertTo0(BaseComponent[] message) {
		SendableMessage.Builder builder = new SendableMessage.Builder();
		addAllIterativeContent(builder, message);
		return builder.build();
	}
	
	@Override
	public SendableMessage convertTo(TextComponent[] message) {
		ArrayNullnessChecker.check(message, "TextComponent");

		return convertTo0(message);
	}

	/*
	 * 
	 * Other methods
	 * 
	 */
	
	@Override
	public boolean supportsHexColoursFunctionality() {
		return BungeeColourConversions.HAS_HEX_SUPPORT;
	}
	
}
