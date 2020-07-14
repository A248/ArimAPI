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

import java.util.Objects;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Enables compatibility with the Kyori Adventure API. <br>
 * <br>
 * The Adventure API is used on Velocity and thus may be used to send chat messages directly there. Additionally,
 * it has its own platform adapters used to send messages on other platforms. Adventure API classes are immutable,
 * making it well-suitable for a variety of contexts. Its textual API is similar to the Sponge Text API. As an arguable
 * improvement, Adventure uses annotation based null analysis rather than {@code Optional}s. <br>
 * <br>
 * <b>Comparison with ArimAPI</b> <br>
 * <br>
 * <i>Colourful messages</i> <br>
 * With regards to chat colours and style formatting, Adventure defines styles (a.k.a. "decorations") as tristates.
 * ArimAPI only permits enabled and disabled styles. Unset Adventure decorations are converted to disabled styles.
 * The Adventure API fully supports hex colours; conversion of respective colour representations is simple and well defined. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. Adventure components have equivalent support
 * for each of these. However, some features from the Adventure API are not available in ArimAPI. <br>
 * <br>
 * For hover actions, ArimAPI only supports displaying tooltip messages. Therefore, only {@code HoverEvent.Action#SHOW_TEXT}
 * is supported. Other actions are ignored during conversion. <br>
 * <br>
 * For click actions, ArimAPI only supports urls, commands, and suggestions. There are no equivalents for the others,
 * which are dropped. <br>
 * <br>
 * <i>Nontext Messages</i> <br>
 * ArimAPI only supports components which are text based. Hence, this class converts {@link TextComponent} and not
 * {@link Component}. <br>
 * <br>
 * <i>Conversion Policy</i> <br>
 * When there are no equivalents for an Adventure API feature, it is simply discarded from the final result.
 * 
 * @author A248
 *
 */
public class AdventureTextConverter implements PlatformMessageAdapter<TextComponent> {

	/**
	 * Creates an instance
	 * 
	 */
	public AdventureTextConverter() {}
	
	/*
	 * 
	 * Converting from ArimAPI
	 * 
	 */
	
	private static TextColor convertColour(int hex) {
		return TextColor.of(hex);
	}
	
	private static int convertDecor(TextDecoration decor) {
		return 1 << decor.ordinal();
	}
	
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
		TextComponent.Builder child = TextComponent.builder();

		child.color(convertColour(component.getColour()));
		for (TextDecoration decor : TextDecoration.values()) {
			child.decoration(decor, component.hasStyle(convertDecor(decor)));
		}
		if (component instanceof JsonComponent) {
			JsonComponent jsonComp = (JsonComponent) component;

			JsonHover hover = jsonComp.getHoverAction();
			if (hover != null) {
				child.hoverEvent(HoverEvent.showText(convertFrom0(hover.getTooltip())));
			}
			JsonClick click = jsonComp.getClickAction();
			if (click != null) {
				child.clickEvent(ClickEvent.of(convertClickType(click.getType()), click.getValue()));
			}
			JsonInsertion insertion = jsonComp.getInsertionAction();
			if (insertion != null) {
				child.insertion(insertion.getValue());
			}
		}
		return child.build();
	}
	
	private static TextComponent convertFrom0(SendableMessage message) {
		TextComponent.Builder parent = TextComponent.builder();

		for (TextualComponent component : message.getComponents()) {
			parent.append(convertComponent(component));
		}
		return parent.build();
	}
	
	@Override
	public TextComponent convertFrom(SendableMessage message) {
		Objects.requireNonNull(message, "Message must not be null");

		return convertFrom0(message);
	}
	
	/*
	 * 
	 * Converting to ArimAPI
	 * 
	 */
	
	private static int convertColour(TextColor colour) {
		if (colour == null) {
			return 0;
		}
		return colour.value();
	}
	
	private static int convertDecors(Style style) {
		int styles = 0;
		for (TextDecoration decor : TextDecoration.values()) {
			if (style.hasDecoration(decor)) {
				styles |= convertDecor(decor);
			}
		}
		return styles;
	}
	
	private static JsonClick.Type convertEventAction(ClickEvent.Action clickEventAction) {
		switch (clickEventAction) {
		case RUN_COMMAND:
			return JsonClick.Type.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return JsonClick.Type.SUGGEST_COMMAND;
		case OPEN_URL:
			return JsonClick.Type.OPEN_URL;
		default:
			return null;
		}
	}

	private static void addAllContent(SendableMessage.Builder parentBuilder, TextComponent component) {
		JsonComponent.Builder builder = new JsonComponent.Builder();
		builder.text(component.content());
		Style style = component.style();

		builder.colour(convertColour(component.color()));
		builder.styles(convertDecors(style));

		HoverEvent<?> hoverEvent = style.hoverEvent();
		if (hoverEvent != null && hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
			Object value = hoverEvent.value();
			if (value instanceof TextComponent) {
				builder.hoverAction(new JsonHover(convertTo0((TextComponent) value)));
			}
		}
		ClickEvent clickEvent = style.clickEvent();
		if (clickEvent != null) {
			JsonClick.Type type = convertEventAction(clickEvent.action());
			if (type != null) {
				builder.clickAction(new JsonClick(type, clickEvent.value()));
			}
		}
		String insertion = style.insertion();
		if (insertion != null) {
			builder.insertionAction(new JsonInsertion(insertion));
		}
		parentBuilder.add(builder.build());

		for (Component child : component.children()) {
			if (child instanceof TextComponent) {
				addAllContent(parentBuilder, (TextComponent) child);
			}
		}
	}

	private static SendableMessage convertTo0(TextComponent message) {
		SendableMessage.Builder builder = new SendableMessage.Builder();
		addAllContent(builder, message);
		return builder.build();
	}
	
	@Override
	public SendableMessage convertTo(TextComponent message) {
		Objects.requireNonNull(message, "Message must not be null");

		return convertTo0(message);
	}
	
	/*
	 * 
	 * Other methods
	 * 
	 */
	
	@Override
	public boolean supportsHexColoursFunctionality() {
		return true;
	}

}
