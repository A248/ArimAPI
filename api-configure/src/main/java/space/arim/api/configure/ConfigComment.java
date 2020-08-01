/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

import java.util.Arrays;

/**
 * Compact representation of a configuration comment, comprised of leading whitespace and a value. <br>
 * <br>
 * The whitespace is an {@code int} representing the amount of spaces before the comment symbol.
 * The value is the string following the comment symbol.
 * 
 * @author A248
 *
 */
public final class ConfigComment {

	private final int whitespace;
	private final char[] value;
	
	/**
	 * Creates from an amount of leading whitespace and a value
	 * 
	 * @param whitespace the amount of leading whitespace
	 * @param value the value of the comment
	 */
	public ConfigComment(int whitespace, String value) {
		this.whitespace = whitespace;
		this.value = value.toCharArray();
	}
	
	/**
	 * Gets the amount of leading whitespace
	 * 
	 * @return the amount of whitespace
	 */
	public int getWhitespace() {
		return whitespace;
	}
	
	/**
	 * Gets the value of the comment
	 * 
	 * @return the value
	 */
	public String getValue() {
		return String.valueOf(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + whitespace;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ConfigComment)) {
			return false;
		}
		ConfigComment other = (ConfigComment) object;
		return whitespace == other.whitespace && Arrays.equals(value, other.value);
	}
	
	@Override
	public String toString() {
		return "ConfigComment [whitespace=" + whitespace + ", value=" + String.valueOf(value) + "]";
	}

	/**
	 * Converts to a comment string using the specified comment symbol
	 * 
	 * @param commentSymbol the comment symbol
	 * @return a comment string
	 */
	public String toCommentString(char commentSymbol) {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < whitespace; n++) {
			builder.append(' ');
		}
		return builder.append(commentSymbol).append(value).toString();
	}
	
}
