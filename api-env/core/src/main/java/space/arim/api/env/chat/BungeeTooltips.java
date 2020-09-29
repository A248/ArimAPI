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

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonHover;

final class BungeeTooltips {
	
	private static final boolean LEGACY_HOVER_EVENT;
	
	private BungeeTooltips() {}
	
	static {
		boolean legacyHoverEvent = true;
		try {
			Class.forName("net.md_5.bungee.api.chat.hover.content.Content");
			legacyHoverEvent = false;
		} catch (ClassNotFoundException ignored) {}
		LEGACY_HOVER_EVENT = legacyHoverEvent;
	}
	
	static JsonHover convert(HoverEvent hoverEvent) {
		HoverEvent.Action action = hoverEvent.getAction();
		if (action != HoverEvent.Action.SHOW_TEXT) {
			return null;
		}
		if (LEGACY_HOVER_EVENT) {
			@SuppressWarnings("deprecation")
			BaseComponent[] hoverValue = hoverEvent.getValue();
			if (hoverValue == null) {
				return null;
			}
			List<TextComponent> components = getAllTextComponents(hoverValue);
			List<ChatComponent> contents = new FromBungeeConverter(components).parseToContent();
			return JsonHover.create(contents);
		}
		List<Content> bungeeContents = hoverEvent.getContents();
		if (bungeeContents == null) {
			return null;
		}
		List<ChatComponent> contents = new ArrayList<>();
		for (Content bungeeContent : bungeeContents) {

			if (!(bungeeContent instanceof Text)) {
				continue;
			}
			Object value = ((Text) bungeeContent).getValue();
			if (value instanceof String) {
				contents.add(new ChatComponent.Builder().text((String) value).build());

			} else if (value instanceof BaseComponent[]) {
				BaseComponent[] hoverValue = (BaseComponent[]) value;
				List<TextComponent> components = getAllTextComponents(hoverValue);
				contents.addAll(new FromBungeeConverter(components).parseToContent());

			} else {
				// who knows what happened
			}
		}
		return JsonHover.create(contents);
	}
	
	private static List<TextComponent> getAllTextComponents(BaseComponent[] hoverValue) {
		List<TextComponent> components = new ArrayList<>();
		for (BaseComponent baseComponent : hoverValue) {
			if (baseComponent instanceof TextComponent) {
				components.add((TextComponent) baseComponent);
			}
		}
		return components;
	}
	
	static HoverEvent createTooltip(List<? extends BaseComponent> components) {
		BaseComponent[] value = components.toArray(BaseComponent[]::new);
		if (LEGACY_HOVER_EVENT) {
			@SuppressWarnings("deprecation")
			HoverEvent legacyEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, value);
			return legacyEvent;
		}
		/*
		 * Bungee uses an ArrayList internally by default, but it does not seem to require
		 * that the List be mutable.
		 * If this has issues, switch to ArrayList
		 */
		List<Content> contents = List.of(new Text(value));
		return new HoverEvent(HoverEvent.Action.SHOW_TEXT, contents);
	}
	
}
