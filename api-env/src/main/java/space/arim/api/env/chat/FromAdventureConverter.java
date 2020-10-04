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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;

class FromAdventureConverter extends FromHierarchicalApiConverter<Component, TextComponent, HoverEvent<?>, ClickEvent> {
	
	FromAdventureConverter(TextComponent component) {
		super(List.of(component));
	}
	
	@Override
	ChatComponent convertComponent(TextComponent component) {
		ChatComponent.Builder builder = new ChatComponent.Builder();
		builder.text(component.content());
		// Colours and styles do not take into account parent components, so they must be calculated manually
		builder.colour(AdventureColourConversions.convertColour(
				getFirstNonNullFromHierarchy(component, TextComponent::color)));
		builder.styles(calculateStyles(component));
		return builder.build();
	}
	
	private int calculateStyles(TextComponent component) {
		int styles = 0;
		for (TextDecoration decoration : TextDecoration.values()) {
			Boolean decorState = getFirstNonNullFromHierarchy(component, (comp) -> {
				return AdventureColourConversions.booleanFromState(comp.decoration(decoration));
			});
			if (decorState != null && decorState) {
				int style = AdventureColourConversions.convertDecor(decoration);
				styles |= style;
			}
		}
		return styles;
	}
	
	@Override
	JsonHover convertHover(HoverEvent<?> hoverEvent) {
		if (hoverEvent.action() != HoverEvent.Action.SHOW_TEXT) {
			return null;
		}
		Object component = hoverEvent.value();
		if (!(component instanceof TextComponent)) {
			return null;
		}
		return JsonHover.create(new FromAdventureConverter((TextComponent) component).parseToContent());
	}
	
	private static JsonClick.ClickType convertClickType(ClickEvent.Action clickEventAction) {
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
		String value = clickEvent.value();
		JsonClick.ClickType type = convertClickType(clickEvent.action());
		if (type == null) {
			return null;
		}
		return JsonClick.create(type, value);
	}

	@Override
	HoverEvent<?> getHoverEvent(TextComponent component) {
		return component.hoverEvent();
	}

	@Override
	ClickEvent getClickEvent(TextComponent component) {
		return component.clickEvent();
	}

	@Override
	String getInsertion(TextComponent component) {
		return component.insertion();
	}

	@Override
	Class<TextComponent> getTextComponentClass() {
		return TextComponent.class;
	}

	@Override
	List<? extends Component> getChildren(TextComponent component) {
		return component.children();
	}
	
}
