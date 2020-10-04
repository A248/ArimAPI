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
import space.arim.api.chat.SendableMessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

final class ConversionsToAdventure {
	
	private ConversionsToAdventure() {}
	
	private static HoverEvent<?> convertHover(JsonHover hover) {
		if (hover == null) {
			return null;
		}
		TextComponent.Builder builder = Component.text();
		for (ChatComponent content : hover.getContents()) {
			builder.append(convertComponent(content).build());
		}
		return HoverEvent.showText(builder.build());
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
		return ClickEvent.clickEvent(convertClickType(click.getType()), click.getValue());
	}
	
	private static String convertInsertion(JsonInsertion insertion) {
		if (insertion == null) {
			return null;
		}
		return insertion.getValue();
	}
	
	private static TextComponent.Builder convertComponent(ChatComponent component) {
		TextComponent.Builder child = Component.text();

		child.color(AdventureColourConversions.convertColour(component.getColour()));
		for (TextDecoration decor : TextDecoration.values()) {
			child.decoration(decor,
					component.hasStyle(AdventureColourConversions.convertDecor(decor)));
		}
		return child;
	}
	
	private static List<TextComponent> convertSection(JsonSection section) {
		HoverEvent<?> hoverEvent = convertHover(section.getHoverAction());
		ClickEvent clickEvent = convertClick(section.getClickAction());
		String insertion = convertInsertion(section.getInsertionAction());

		List<TextComponent> components = new ArrayList<>();
		for (ChatComponent content : section.getContents()) {
			TextComponent.Builder componentBuilder = convertComponent(content);
			componentBuilder.hoverEvent(hoverEvent);
			componentBuilder.clickEvent(clickEvent);
			componentBuilder.insertion(insertion);
			components.add(componentBuilder.build());
		}
		return components;
	}
	
	static TextComponent convertFrom0(SendableMessage message) {
		TextComponent.Builder parent = Component.text();
		for (JsonSection section : message.getSections()) {
			parent.append(convertSection(section));
		}
		return parent.build();
	}
	
}
