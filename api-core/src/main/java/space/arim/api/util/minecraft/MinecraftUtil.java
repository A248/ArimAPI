/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.minecraft;

import java.util.ArrayList;
import java.util.UUID;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * * See {@link #parseJson(String)} for Minecraft json message handling <br>
 * * See {@link #expandUUID(String)} for expanding shortened UUIDs
 * 
 * @author A248
 *
 */
public final class MinecraftUtil {

	private MinecraftUtil() {}
	
	private enum TagType {
		NONE,
		TTP,
		URL,
		CMD,
		SGT,
		INS;
		
		static final int TAG_LENGTH = 3;
		
	}
	
	private static TagType jsonTag(String node) {
		if (node.length() < TagType.TAG_LENGTH + 2) {
			return TagType.NONE;
		}
		switch (node.substring(0, 4)) {
		case "ttp:":
			return TagType.TTP;
		case "url:":
			return TagType.URL;
		case "cmd:":
			return TagType.CMD;
		case "sgt:":
			return TagType.SGT;
		case "ins:":
			return TagType.INS;
		default:
			return TagType.NONE;
		}
	}
	
	public static String stripJson(String json) {
		StringBuilder builder = new StringBuilder();
		for (String s : json.split("||")) {
			if (jsonTag(s).equals(TagType.NONE)) {
				builder.append(s);
			}
		}
		return builder.toString();
	}
	
	/**
	 * Identical to <code>ChatColor.translateAlternateColorCodes('&', colorable);</code>. <br>
	 * Does not accept uppercase color codes (e.g., &A instead of &a).
	 * 
	 * @param colorable the string to add color codes to
	 * @return the colored string
	 */
	public static String encode(String colorable) {
		char[] b = colorable.toCharArray();
		for (int n = 0; n < b.length - 1; ++n) {
			if (b[n] == '&' && "0123456789abcdefklmnor".indexOf(b[n + 1]) > -1) {
				b[n] = 167;
			}
		}
		return new String(b);
	}
	
	/**
	 * Similar to {@link #encode(String)}, but additionally converts to a {@link BaseComponent} array for BungeeCord message sending.
	 * 
	 * @param colorable the string to add color codes to
	 * @return the colored BaseComponent array
	 */
	public static BaseComponent[] encodeBungee(String colorable) {
		return TextComponent.fromLegacyText(encode(colorable));
	}
	
	/**
	 * Converts a string formatted according to RezzedUp's json.sk into a {@link BaseComponent} array. <br>
	 * <br>
	 * Colors, tooltip tags, url tags, command tags, suggestion tags, and insertion tags are all parsed.
	 * 
	 * @param json the input string
	 * @return BaseComponent[] sendable json array
	 */
	public static BaseComponent[] parseJson(String json) {
		BaseComponent current = null;
		ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
		for (String node : json.split("||")) {
			TagType tag = jsonTag(node);
			if (tag.equals(TagType.NONE)) {
				if (current != null) {
					components.add(current);
				}
				current = new TextComponent(TextComponent.fromLegacyText(node));
			} else if (current != null) {
				String value = node.substring(4);
				if (tag.equals(TagType.TTP)) {
					current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(value)));
				} else if (tag.equals(TagType.URL)) {
					if (!value.startsWith("https://") && !value.startsWith("http://")) {
						value = "http://" + value;
					}
					current.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
				} else if (tag.equals(TagType.CMD)) {
					current.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
				} else if (tag.equals(TagType.SGT)) {
					current.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
				} else if (tag.equals(TagType.INS)) {
					current.setInsertion(value);
				}
			}
		}
		return components.toArray(new BaseComponent[] {});
	}
	
	/**
	 * Expands a shortened version of a UUID. <br>
	 * <br>
	 * Every UUID has 2 forms. Each form is unique. However, it is simpler to store UUIDs in short form
	 * and expand them into long form. <br>
	 * <br>
	 * Example long form: ed5f12cd-6007-45d9-a4b9-940524ddaecf <br>
	 * Example short form: ed5f12cd600745d9a4b9940524ddaecf <br>
	 * <br>
	 * <b>This method does not parse the UUID. See {@link #expandAndParseUUID(String)} or {@link UUID#fromString(String)} for a full UUID object.
	 * 
	 * @param uuid the string based short uuid
	 * @return the lengthened uuid string
	 */
	public static String expandUUID(String uuid) {
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16)
		+ "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
	
	/**
	 * Equivalent to calling <code>UUID.fromString(MinecraftUtil.expandUUID(uuid))</code> <br>
	 * <br>
	 * See {@link #expandUUID(String)} for details
	 * 
	 * @param uuid the short uuid string
	 * @return a parsed UUID
	 */
	public static UUID expandAndParseUUID(String uuid) {
		return UUID.fromString(expandUUID(uuid));
	}
	
}
