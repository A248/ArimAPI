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
package space.arim.api.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Various String-based utilities.
 * 
 * @author A248
 *
 */
public final class StringsUtil {
	
	private static final ThreadLocal<SimpleDateFormat> BASIC_DATE_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MM-yyyy"));
	
	private StringsUtil() {}
	
	/**
	 * Utilises class <code>com.google.common.net.InetAddresses</code> <br>
	 * <br>
	 * <b>If that class is not on the classpath do not call this method!</b>
	 * 
	 * @param address - the address to validate
	 * @return true if valid, false otherwise
	 */
	public static boolean validAddress(String address) {
		return com.google.common.net.InetAddresses.isInetAddress(address);
	}
	
	public static String capitaliseProperly(String input) {
		return (input == null) ? input : (input.length() == 1) ? input.toUpperCase() : Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
	
	public static <T> T[] chopOffOne(T[] input) {
		return copyRange(input, 1, input.length);
	}
	
	private static <T> T[] copyRange(T[] input, int start, int end) {
		return Arrays.copyOfRange(Objects.requireNonNull(input, "Input array must not be null!"), start, end);
	}
	
	public static String concatRange(List<String> input, char separator, int start, int end) {
		return concatRange(Objects.requireNonNull(input, "Input list must not be null!").toArray(new String[] {}), separator, start, end);
	}
	
	public static String concatRange(String[] input, char separator, int start, int end) {
		StringBuilder builder = new StringBuilder();
		String[] selected = copyRange(Objects.requireNonNull(input, "Input array must not be null!"), start, end);
		for (String m : selected) {
			if (m != null && !m.isEmpty()) {
				builder.append(separator).append(m);
			}
		}
		return builder.length() == 0 ? "" : builder.toString().substring(1);
	}
	
	public static String concat(List<String> input, char separator) {
		return concatRange(input, separator, 0, input.size());
	}
	
	public static String concat(String[] input, char separator) {
		return concatRange(input, separator, 0, input.length);
	}
	
	/**
	 * Checks to ensure a string is not empty according to {@link String#isEmpty()} <br>
	 * Similar to {@link Objects#requireNonNull(Object)}. <br>
	 * Includes a null check.
	 * 
	 * @param input the string to check
	 * @return the string
	 * @throws IllegalArgumentException if the input string is empty
	 * @throws NullPointerException if the input string is null
	 */
	public static String requireNonEmpty(String input) {
		if (Objects.requireNonNull(input).isEmpty()) {
			throw new IllegalArgumentException();
		}
		return input;
	}
	
	/**
	 * Checks to ensure a string is not empty according to {@link String#isEmpty()} <br>
	 * Similar to {@link Objects#requireNonNull(Object, String)}. <br>
	 * Includes a null check.
	 * 
	 * @param input the string to check
	 * @param message the message to use for thrown exceptions if either requirement is not met
	 * @return the string
	 * @throws IllegalArgumentException if the input string is empty
	 * @throws NullPointerException if the input string is null
	 */
	public static String requireNonEmpty(String input, String message) {
		if (Objects.requireNonNull(input, message).isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return input;
	}
	
	public static String basicTodaysDate() {
		return BASIC_DATE_FORMATTER.get().format(new Date());
	}
	
}
