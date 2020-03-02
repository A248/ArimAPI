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
package space.arim.api.chat;

/**
 * A json tag, e.g. a command or tooltip, applies to a specific part of a json message. <br>
 * The <code>JsonTag</code> enum is comprised of all tag types. <br>
 * <br>
 * Note that some tags are mutually exclusive; urls, commands, and suggestions all run on click,
 * so in practice, only the first relevant click event is translated to the plaform specific chat API. <br>
 * <br>
 * For the most part, JsonTag is used for internal implementation, and needn't be messed with.
 * 
 * @author A248
 *
 */
public enum JsonTag {
	
	NONE,
	TTP,
	URL,
	CMD,
	SGT,
	INS;
	
	private static final int TAG_NOTATION_LENGTH = 4;
	
	/**
	 * Parses the tag type of a valid json node. <br>
	 * <br>
	 * If the input starts with the signifying json tag, ignoring case, the resulting mapping is returned, according to: <br>
	 * <i>ttp:</i> {@literal -}{@literal >} <code>JsonTag.TTP</code> <br>
	 * <i>url:</i> {@literal -}{@literal >} <code>JsonTag.URL</code> <br>
	 * <i>cmd:</i> {@literal -}{@literal >} <code>JsonTag.CMD</code> <br>
	 * <i>sgt:</i> {@literal -}{@literal >} <code>JsonTag.SGT</code> <br>
	 * <i>ins:</i> {@literal -}{@literal >} <code>JsonTag.INS</code> <br>
	 * Otherwise, <code>JsonTag.NONE</code>.
	 * 
	 * @param node the input string (a json node)
	 * @return the relevant json tag, never <code>null</code>
	 */
	public static JsonTag getFor(String node) {
		if (node.length() <= JsonTag.TAG_NOTATION_LENGTH) {
			return JsonTag.NONE;
		}
		switch (node.substring(0, 4).toLowerCase()) {
		case "ttp:":
			return JsonTag.TTP;
		case "url:":
			return JsonTag.URL;
		case "cmd:":
			return JsonTag.CMD;
		case "sgt:":
			return JsonTag.SGT;
		case "ins:":
			return JsonTag.INS;
		default:
			return JsonTag.NONE;
		}
	}
}
