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

import java.util.regex.Pattern;

public class AllColourDeserialiserImpl extends LegacyColourDeserialiserImpl {
	
	private static final Pattern COLOUR_PATTERN = Pattern.compile(
			// Legacy colour codes
			"(&[0-9A-Fa-fK-Rk-r])|"
			// Hex codes such as <#00AAFF>
			+ "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)|"
			// and the shorter <#4BC>
			+ "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)");

	AllColourDeserialiserImpl(String content) {
		super(COLOUR_PATTERN, content);
	}
	
	@Override
	void applyFormatting(String formatGroup) {
		switch (formatGroup.length()) {
		case 2:
			super.applyFormatting(formatGroup);
			break;
		case 6:
			// Shortened hex colour code
			char[] shortHex = formatGroup.substring(2, 5).toCharArray();
			char[] fullHex = new char[] {shortHex[0], shortHex[0], shortHex[1], shortHex[1], shortHex[2], shortHex[2]};
			setHexColour(String.valueOf(fullHex));
			break;
		case 9:
			// Full hex colour code
			setHexColour(formatGroup.substring(2, 8));
			break;
		default:
			throw new IllegalStateException("Matched formatting " + formatGroup + " has no known way to be handled");
		}
	}
	
	private void setHexColour(String hex) {
		int colour = Integer.parseInt(hex, 16);
		setColour(colour);
	}

}
