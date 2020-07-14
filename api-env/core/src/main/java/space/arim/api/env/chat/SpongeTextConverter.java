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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextStyle;

import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

/**
 * Enables compatibility with the Sponge Text API. <br>
 * <br>
 * The Sponge Text API is, not surprisingly, used for the Sponge platform. . The Text API uses {@code Optional}
 * to handle nullability. It is clearly designed with flexibility for future changes; no enums are ever used,
 * although this makes conversion somewhat more difficult. Instead of an enum of types of a specific JSON action,
 * the Text API uses parameterised generics in concrete subclasses to indicate the kind of result. <br>
 * <br>
 * <b>Comparison with ArimAPI</b> <br>
 * <br>
 * <i>Colourful messages</i> <br>
 * With regards to chat colours and style formatting, the Text API defines styles as tristates: nullable {@code Boolean}s,
 * where true indicates enabled, null unset, and false disabled. In ArimAPI, styles are either enabled or disabled.
 * Unset styles in the Text API are converted to disabled styles in ArimAPI. Since the Sponge Text API does not currently
 * support arbitrary hex colours, {@link space.arim.api.chat.PredefinedColour#getNearestTo(int)} is used.
 * This may change should the feature be implemented in the Sponge API. <br>
 * <br>
 * <i>Json Messages</i> <br>
 * ArimAPI enables tooltips, urls, commands, suggestions, and insertions. Text objects have equivalent support for each
 * of these. However, some features from the Text API are not available in ArimAPI. <br>
 * <br>
 * For hover actions, ArimAPI only supports displaying tooltip messages. Thus, there is only an equivalent for
 * {@code HoverEvent.ShowText}, but other {@code HoverEvent}s are ommitted during conversion. <br>
 * <br>
 * For click actions, ArimAPI only supports urls, commands, and suggestions. There are no equivalents for the others,
 * which are ignored. <br>
 * <br>
 * <i>Nontext Messages</i> <br>
 * ArimAPI only supports components which are text based. Thus, only {@code LiteralText} is supported, and not any other
 * {@code Text} subclasses or implementations, which are skipped if found. <br>
 * <br>
 * <i>Conversion Policy</i> <br>
 * When there are no equivalents for a Text API feature, it is simply discarded from the final result.
 * 
 * @author A248
 *
 */
public class SpongeTextConverter implements PlatformMessageAdapter<LiteralText> {
	
	/**
	 * Creates an instance
	 * 
	 */
	public SpongeTextConverter() {}
	
	/*
	 * 
	 * Converting from ArimAPI
	 * 
	 */
	
	private static ClickAction<?> convertClick(JsonClick jsonClick) {
		if (jsonClick == null) {
			return null;
		}
		switch (jsonClick.getType()) {
		case RUN_COMMAND:
			return TextActions.runCommand(jsonClick.getValue());
		case SUGGEST_COMMAND:
			return TextActions.suggestCommand(jsonClick.getValue());
		case OPEN_URL:
			try {
				return TextActions.openUrl(new URL(jsonClick.getValue()));
			} catch (MalformedURLException ex) {
				// We can't do anything about this
				return null;
			}
		default:
			throw new IllegalStateException("Not implemented for " + jsonClick.getType());
		}
	}
	
	private static Text convertComponent(TextualComponent component) {
		Text.Builder builder = Text.builder(component.getText());

		builder.color(SpongeFormattingConversions.convertColour(component.getColour()));
		builder.style(SpongeFormattingConversions.convertStyles(component));

		if (component instanceof JsonComponent) {
			JsonComponent jsonComp = (JsonComponent) component;

			JsonHover hoverAction = jsonComp.getHoverAction();
			if (hoverAction != null) {
				builder.onHover(TextActions.showText(convertFrom0(hoverAction.getTooltip())));
			}
			builder.onClick(convertClick(jsonComp.getClickAction()));
			JsonInsertion insertionAction = jsonComp.getInsertionAction();
			if (insertionAction != null) {
				builder.onShiftClick(TextActions.insertText(insertionAction.getValue()));
			}
		}
		return builder.build();
	}
	
	private static LiteralText convertFrom0(SendableMessage message) {
		Text.Builder builder = Text.builder();
		for (TextualComponent component : message.getComponents()) {
			builder.append(convertComponent(component));
		}
		return (LiteralText) builder.build();
	}
	
	@Override
	public LiteralText convertFrom(SendableMessage message) {
		Objects.requireNonNull(message, "Message must not be null");

		return convertFrom0(message);
	}
	
	/*
	 * 
	 * Converting to ArimAPI
	 * 
	 */
	
	private static int convertTextStyle(TextStyle style) {
		int styles = 0;
		if (style.isObfuscated().orElse(false)) {
			styles |= MessageStyle.MAGIC;
		}
		if (style.isBold().orElse(false)) {
			styles |= MessageStyle.BOLD;
		}
		if (style.hasStrikethrough().orElse(false)) {
			styles |= MessageStyle.STRIKETHROUGH;
		}
		if (style.hasUnderline().orElse(false)) {
			styles |= MessageStyle.UNDERLINE;
		}
		if (style.isItalic().orElse(false)) {
			styles |= MessageStyle.ITALIC;
		}
		return styles;
	}
	
	private static JsonHover convertHoverAction(HoverAction<?> hoverAction) {
		if (!(hoverAction instanceof HoverAction.ShowText)) {
			return null;
		}
		Object result = hoverAction.getResult();
		if (!(result instanceof LiteralText)) {
			return null;
		}
		return new JsonHover((convertTo0((LiteralText) result)));
	}
	
	private static JsonClick convertClickAction(ClickAction<?> clickAction) {
		if (clickAction instanceof ClickAction.RunCommand) {
			return new JsonClick(JsonClick.Type.RUN_COMMAND, ((ClickAction.RunCommand) clickAction).getResult());
		}
		if (clickAction instanceof ClickAction.SuggestCommand) {
			return new JsonClick(JsonClick.Type.SUGGEST_COMMAND, ((ClickAction.SuggestCommand) clickAction).getResult());
		}
		if (clickAction instanceof ClickAction.OpenUrl) {
			return new JsonClick(JsonClick.Type.OPEN_URL, ((ClickAction.OpenUrl) clickAction).getResult().toExternalForm());
		}
		return null;
	}
	
	private static void addAllContent(SendableMessage.Builder parentBuilder, LiteralText message) {
		JsonComponent.Builder builder = new JsonComponent.Builder();
		builder.text(message.getContent());

		builder.colour(message.getColor().getColor().getRgb());
		builder.styles(convertTextStyle(message.getStyle()));

		builder.hoverAction(convertHoverAction(message.getHoverAction().orElse(null)));
		builder.clickAction(convertClickAction(message.getClickAction().orElse(null)));
		ShiftClickAction<?> insertion = message.getShiftClickAction().orElse(null);
		if (insertion instanceof ShiftClickAction.InsertText) {
			builder.insertionAction(new JsonInsertion(((ShiftClickAction.InsertText) insertion).getResult()));
		}
		parentBuilder.add(builder.build());

		for (Text child : message.getChildren()) {
			if (child instanceof LiteralText) {
				addAllContent(parentBuilder, (LiteralText) child);
			}
		}
	}
	
	private static SendableMessage convertTo0(LiteralText message) {
		SendableMessage.Builder builder = new SendableMessage.Builder();
		addAllContent(builder, message);
		return builder.build();
	}
	
	@Override
	public SendableMessage convertTo(LiteralText message) {
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
		return false;
	}
	
}
