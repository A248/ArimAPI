/* 
 * ArimAPI-chat
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-chat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-chat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-chat. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.chat.serialiser;

import java.util.Locale;

enum JsonTag {
	
	/**
	 * No tag specified
	 */
	NONE,
	/**
	 * Explicitly no tag, "nil:"
	 */
	NIL,
	/**
	 * Tooltip tag, "ttp:"
	 */
	TTP,
	/**
	 * Command tag, "cmd:"
	 */
	CMD,
	/**
	 * Suggestion tag, "sgt:"
	 */
	SGT,
	/**
	 * URL tag, "url:"
	 */
	URL,
	/**
	 * Insertion tag, "ins:" 
	 */
	INS;
	
	static JsonTag getTag(String segment) {
		if (segment.length() <= 4) {
			return NONE;
		}
		switch (segment.substring(0, 4).toLowerCase(Locale.ROOT)) {
		case "nil:":
			return NIL;
		case "ttp:":
			return TTP;
		case "cmd:":
			return CMD;
		case "sgt:":
			return SGT;
		case "url:":
			return URL;
		case "ins:":
			return INS;
		default:
			return NONE;
		}
	}
}
