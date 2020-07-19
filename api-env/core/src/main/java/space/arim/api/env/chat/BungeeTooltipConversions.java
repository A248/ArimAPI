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

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

import space.arim.api.chat.SendableMessage;
import space.arim.api.chat.TextualComponent;

class BungeeTooltipConversions {
	
	private static final boolean LEGACY_HOVER_EVENT;
	
	static {
		boolean legacyHoverEvent = true;
		try {
			Class.forName("net.md_5.bungee.api.chat.hover.content.Content");
			legacyHoverEvent = false;
		} catch (ClassNotFoundException ignored) {}
		LEGACY_HOVER_EVENT = legacyHoverEvent;
	}
	
	static SendableMessage convert(HoverEvent hoverEvent) {
		if (hoverEvent == null) {
			return null;
		}
		HoverEvent.Action action = hoverEvent.getAction();
		if (action == null) {
			return null;
		}
		if (action != HoverEvent.Action.SHOW_TEXT) { // Nothing else supported
			return null;
		}
		if (LEGACY_HOVER_EVENT) {
			@SuppressWarnings("deprecation")
			BaseComponent[] result = hoverEvent.getValue();
			if (!ArrayNullnessChecker.evaluate(result)) {
				return null;
			}
			return BungeeComponentConverter.convertTo0(result);
		}
		List<Content> contents = hoverEvent.getContents();
		if (contents == null) {
			return null;
		}
		SendableMessage.Builder parentBuilder = new SendableMessage.Builder();
		for (Content content : contents) {

			if (!(content instanceof Text)) {
				continue;
			}
			Object value = ((Text) content).getValue();
			if (value instanceof String) {
				parentBuilder.add(new TextualComponent.Builder().text((String) value).build());

			} else if (value instanceof BaseComponent[]) {
				BungeeComponentConverter.addAllIterativeContent(parentBuilder, (BaseComponent[]) value);

			} else {
				// who knows what happened
			}
		}
		return parentBuilder.build();
	}
	
	static HoverEvent createTooltip(BaseComponent[] value) {
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
