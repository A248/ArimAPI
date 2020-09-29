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

import java.util.List;

import space.arim.api.chat.ChatComponent;
import space.arim.api.chat.JsonClick;
import space.arim.api.chat.JsonHover;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

class FromBungeeConverter extends FromHierarchicalApiConverter<BaseComponent, TextComponent, HoverEvent, ClickEvent> {
	
	FromBungeeConverter(List<TextComponent> message) {
		super(message);
	}
	
	@Override
	ChatComponent convertComponent(TextComponent component) {
		ChatComponent.Builder builder = new ChatComponent.Builder();
		builder.text(component.getText());
		// Colours and styles take into account parent components per bungee-chat API
		builder.colour(BungeeColourConversions.convertColour(component.getColor()));
		builder.styles(BungeeColourConversions.convertStyles(component));
		return builder.build();
	}
	
	@Override
	JsonHover convertHover(HoverEvent hoverEvent) {
		return BungeeTooltips.convert(hoverEvent);
	}
	
	private static JsonClick.ClickType convertClickType(ClickEvent.Action clickEventAction) {
		if (clickEventAction == null) {
			return null;
		}
		switch (clickEventAction) {
		case RUN_COMMAND:
			return JsonClick.ClickType.RUN_COMMAND;
		case SUGGEST_COMMAND:
			return JsonClick.ClickType.SUGGEST_COMMAND;
		case OPEN_URL:
			return JsonClick.ClickType.OPEN_URL;
		default:
			// Not supported
			return null;
		}
	}
	
	@Override
	JsonClick convertClick(ClickEvent clickEvent) {
		if (clickEvent == null) {
			return null;
		}
		String value = clickEvent.getValue();
		if (value == null) {
			return null;
		}
		JsonClick.ClickType type = convertClickType(clickEvent.getAction());
		if (type == null) {
			return null;
		}
		return JsonClick.create(type, value);
	}

	@Override
	HoverEvent getHoverEvent(TextComponent component) {
		return component.getHoverEvent();
	}

	@Override
	ClickEvent getClickEvent(TextComponent component) {
		return component.getClickEvent();
	}

	@Override
	String getInsertion(TextComponent component) {
		return component.getInsertion();
	}

	@Override
	Class<TextComponent> getTextComponentClass() {
		return TextComponent.class;
	}

	@Override
	List<? extends BaseComponent> getChildren(TextComponent component) {
		List<? extends BaseComponent> extra = component.getExtra();
		if (extra == null) {
			return List.of();
		}
		return extra;
	}
	
}
