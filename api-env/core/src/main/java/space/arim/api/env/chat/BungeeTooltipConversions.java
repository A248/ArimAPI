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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

import space.arim.api.chat.JsonComponent;
import space.arim.api.chat.SendableMessage;

class BungeeTooltipConversions {

	private static final Method LEGACY_HOVER_VALUE;
	
	static {
		Method getValueMethod = null;
		try {
			getValueMethod = HoverEvent.class.getDeclaredMethod("getValue");
		} catch (NoSuchMethodException ignored) {}
		LEGACY_HOVER_VALUE = getValueMethod;
	}
	
	private static SendableMessage convertLegacy(HoverEvent hoverEvent) {
		// HoverEvent#getValue was broken, so this cannot compile with normal method calls
		// https://github.com/SpigotMC/BungeeCord/issues/2905

		BaseComponent[] result;
		try {
			Object invoked = LEGACY_HOVER_VALUE.invoke(hoverEvent);
			if (!(invoked instanceof BaseComponent[])) {
				return null;
			}
			result = (BaseComponent[]) invoked;

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
			return null;
		}
		if (!ArrayNullnessChecker.evaluate(result)) {
			return null;
		}
		return BungeeComponentConverter.convertTo0(result);
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
		if (LEGACY_HOVER_VALUE != null) {
			return convertLegacy(hoverEvent);
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
				parentBuilder.add(new JsonComponent.Builder().text((String) value).build());

			} else if (value instanceof BaseComponent[]) {
				BungeeComponentConverter.addAllIterativeContent(parentBuilder, (BaseComponent[]) value);

			} else {
				// who knows what Bungee did now
			}
		}
		return parentBuilder.build();
	}
	
}
