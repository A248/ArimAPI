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

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;
import space.arim.api.chat.JsonInsertion;
import space.arim.api.chat.JsonSection;
import space.arim.api.chat.MessageStyle;
import space.arim.api.chat.SendableMessage;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

final class ConversionsToBungee {

	private ConversionsToBungee() {}
	
	private static TextComponent convertComponent(ChatComponent component) {
		TextComponent result = new TextComponent(component.getText());

		result.setColor(BungeeColourConversions.convertColour(component.getColour()));
		result.setObfuscated(component.hasStyle(MessageStyle.MAGIC));
		result.setBold(component.hasStyle(MessageStyle.BOLD));
		result.setStrikethrough(component.hasStyle(MessageStyle.STRIKETHROUGH));
		result.setUnderlined(component.hasStyle(MessageStyle.UNDERLINE));
		result.setItalic(component.hasStyle(MessageStyle.ITALIC));
		return result;
	}
	
	private static HoverEvent convertHover(JsonHover hover) {
		if (hover == null) {
			return null;
		}
		List<TextComponent> hoverComponents = convertSection(JsonSection.create(hover.getContents()));
		return BungeeTooltips.createTooltip(hoverComponents);
	}
	
	private static ClickEvent.Action convertClickType(JsonClick.ClickType jsonClickType) {
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
	
	private static ClickEvent convertClick(JsonClick click) {
		if (click == null) {
			return null;
		}
		return new ClickEvent(convertClickType(click.getType()), click.getValue());
	}
	
	private static List<TextComponent> convertSection(JsonSection section) {
		HoverEvent hoverEvent = convertHover(section.getHoverAction());
		ClickEvent clickEvent = convertClick(section.getClickAction());
		JsonInsertion insertionAction = section.getInsertionAction();
		String insertion = (insertionAction == null) ? null : insertionAction.getValue();

		List<TextComponent> components = new ArrayList<>();
		for (ChatComponent content : section.getContents()) {
			TextComponent component = convertComponent(content);
			component.setHoverEvent(hoverEvent);
			component.setClickEvent(clickEvent);
			component.setInsertion(insertion);
			components.add(component);
		}
		return components;
	}
	
	static List<TextComponent> convertFrom0(SendableMessage message) {
		List<TextComponent> result = new ArrayList<>();
		for (JsonSection section : message.getSections()) {
			result.addAll(convertSection(section));
		}
		return result;
	}
	
}
