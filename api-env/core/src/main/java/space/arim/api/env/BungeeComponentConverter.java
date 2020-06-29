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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import space.arim.api.chat.ClickAction;
import space.arim.api.chat.Colour;
import space.arim.api.chat.Component;
import space.arim.api.chat.Format;
import space.arim.api.chat.Format.FormatCatalog;
import space.arim.api.chat.HoverAction;
import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.JsonComponentBuilder;
import space.arim.api.chat.Message;
import space.arim.api.chat.ShiftClickAction;
import space.arim.api.chat.Style;

/**
 * Enables compatibility with the BungeeCord/Spigot Chat Component API (CCA). <br>
 * Contains mirror methods for converting between respective API classes. <br>
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
/*
 * WIP
 */
/*public*/ class BungeeComponentConverter implements PlatformMessageConverter<BaseComponent[]> {
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Formatting codes <br>
	 * Converts from a {@link Format} to a <code>ChatColor</code>.
	 * 
	 * @param format the source format
	 * @return the equivalent <code>ChatColor</code>, null if not found or the input is null
	 */
	public ChatColor convert(Format format) {
		return (format != null) ? ChatColor.getByChar(format.identifier()) : null;
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Formatting codes <br>
	 * Converts from a <code>ChatColor</code> to a {@link Format}.
	 * 
	 * @param colour the source format
	 * @return the equivalent <code>Format</code>, null if not found or the input is null
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
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Click Action types <br>
	 * Converts from a {@link ClickAction.Type} to a <code>ClickEvent.Action</code>
	 * 
	 * @param clickActionType the click action type
	 * @return an equivalent <code>ClickEvent.Action</code>, never null unless the input is null
	 */
	// Private because we do not officially support conversion of individual actions
	private ClickEvent.Action convert(ClickAction.Type clickActionType) {
		if (clickActionType == null) {
			return null;
		}
		switch (clickActionType) {
		case RUN_COMMAND:
			return ClickEvent.Action.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return ClickEvent.Action.SUGGEST_COMMAND;
		case OPEN_URL:
			return ClickEvent.Action.OPEN_URL;
		default:
			throw new IllegalStateException("Not implemented for " + clickActionType);
		}
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Click Action types <br>
	 * Converts from a <code>ClickEvent.Action</code> to a {@link ClickAction.Type}
	 * 
	 * @param clickActionType the click event action
	 * @return an equivalent {@link ClickAction.Type}, null if the input is null or not supported
	 */
	// Private because we do not officially support conversion of individual actions
	private ClickAction.Type convert(ClickEvent.Action clickEventAction) {
		if (clickEventAction == null) {
			return null;
		}
		switch (clickEventAction) {
		case RUN_COMMAND:
			return ClickAction.Type.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return ClickAction.Type.SUGGEST_COMMAND;
		case OPEN_URL:
			return ClickAction.Type.OPEN_URL;
		default:
			// There's nothing we can do about this; it isn't available in our API
			return null;
		}
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Message components <br>
	 * Converts from a {@link Component} to a <code>BaseComponent</code>
	 * 
	 * @param component the source component
	 * @return an equivalent <code>BaseComponent</code>, never null unless the input is null
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
			HoverAction hoverAction = json.getHoverAction();
			if (hoverAction != null) {
				comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, convertFrom(hoverAction.getMessage())));
			}
			ClickAction clickAction = json.getClickAction();
			if (clickAction != null) {
				comp.setClickEvent(new ClickEvent(convert(clickAction.getType()), clickAction.getValue()));
			}
			ShiftClickAction shiftClickAction = json.getShiftClickAction();
			if (shiftClickAction != null) {
				comp.setInsertion(shiftClickAction.getInsertion());
			}
		}
		return comp;
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Message components <br>
	 * Converts from a <code>BaseComponent</code> to a {@link Component}
	 * 
	 * @param component the source component
	 * @return an equivalent <code>Component</code>, never null unless the input is null
	 */
	public Component convert(BaseComponent component) {
		if (component == null) {
			return null;
		}
		JsonComponentBuilder builder = new JsonComponentBuilder();
		addAllContent(builder, component);
		return builder.build();
	}
	
	private void addAllContent(JsonComponentBuilder builder, BaseComponent component) {
		HoverEvent hover = component.getHoverEvent();
		if (hover != null) {
			builder.hoverAction(HoverAction.showTooltip(convertTo(hover.getValue())));
		}
		ClickEvent click = component.getClickEvent();
		if (click != null) {
			ClickAction.Type clickActionType = convert(click.getAction());
			if (clickActionType != null) {
				builder.clickAction(new ClickAction(clickActionType, click.getValue()));
			}
		}
		String insertion = component.getInsertion();
		if (insertion != null) {
			builder.shiftClickAction(ShiftClickAction.insertText(insertion));
		}
		Format format = convert(component.getColor());
		builder = builder.colour((format instanceof Colour) ? (Colour) format : null)
				.style(Style.MAGIC, component.isObfuscated()).style(Style.BOLD, component.isBold())
				.style(Style.STRIKETHROUGH, component.isStrikethrough())
				.style(Style.UNDERLINE, component.isObfuscated()).style(Style.ITALIC, component.isObfuscated());
		for (BaseComponent child : component.getExtra()) {
			addAllContent(builder, child);
		}
	}
	
	/**
	 * <b>ArimAPI {@literal -}{@literal >} CCA</b>: Messages <br>
	 * Converts from a {@link Message} to a <code>BaseComponent</code> array
	 * 
	 * @param message the source message
	 * @return an equivalent <code>BaseComponent</code> array, never null unless the input is null
	 */
	@Override
	public BaseComponent[] convertFrom(Message message) {
		if (message == null) {
			return null;
		}
		Component[] source = message.getComponents();
		BaseComponent[] result = new BaseComponent[source.length];
		for (int n = 0; n < source.length; n++) {
			result[n] = convert(source[n]);
		}
		return result;
	}
	
	/**
	 * <b>CCA {@literal -}{@literal >} ArimAPI</b>: Messages <br>
	 * Converts from a <code>BaseComponent</code> array to a {@link Message}
	 * 
	 * @param message the source message
	 * @return an equivalent <code>Message</code>, never null unless the input is null
	 */
	@Override
	public Message convertTo(BaseComponent[] message) {
		if (message == null) {
			return null;
		}
		Component[] result = new Component[message.length];
		for (int n = 0; n < message.length; n++) {
			result[n] = convert(message[n]);
		}
		return new Message(result).clean();
	}
	
}
