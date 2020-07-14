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
package space.arim.api.chat.parser;

enum JsonTag {
	
	NONE,
	TTP,
	CMD,
	SGT,
	URL,
	INS;
	
	/**
	 * Parses the tag type of a valid json node. <br>
	 * <br>
	 * If the input starts with the signifying json tag,, the resulting mapping is returned, according to: <br>
	 * <i>ttp:</i> {@literal -}{@literal >} {@code TTP} <br>
	 * <i>cmd:</i> {@literal -}{@literal >} {@code CMD} <br>
	 * <i>sgt:</i> {@literal -}{@literal >} {@code SGT} <br>
	 * <i>url:</i> {@literal -}{@literal >} {@code URL} <br>
	 * <i>ins:</i> {@literal -}{@literal >} {@code INS} <br>
	 * Otherwise, {@code NONE}.
	 * 
	 * @param node the input string (a json node)
	 * @return the relevant json tag, never {@code null}
	 */
	static JsonTag getFor(String node) {
		if (node.length() <= 4) {
			return NONE;
		}
		switch (node.substring(0, 4)) {
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
