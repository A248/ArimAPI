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
import java.util.Collection;
import java.util.Date;
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
	
	/**
	 * Capitalises the first letter of the input string. <br>
	 * If the input string is null, null is returned.
	 * 
	 * @param input the input string
	 * @return the same string with the first letter capitalised
	 */
	public static String capitaliseProperly(String input) {
		return (input == null) ? input : (input.length() == 1) ? input.toUpperCase() : Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}
	
	/**
	 * Removes the first element in an array and returns the updated array
	 * (the source array is not modified. <br>
	 * <br>
	 * Identical to calling <code>Arrays.copyOfRange(input, 1, input.length)</code>,
	 * or <code>space.arim.universal.util.ArraysUtil.contractAndRemove(input, 0)</code>
	 * 
	 * @param <T> the element type of the array
	 * @param input the input array
	 * @return the same array with the first element removed
	 * 
	 * @deprecated There are too many alternatives to this method with the exact same
	 * functionality, but more readable method names.
	 */
	@Deprecated
	public static <T> T[] chopOffOne(T[] input) {
		return Arrays.copyOfRange(input, 1, input.length);
	}
	
	/**
	 * Same as {@link #concat(String[], char)} but for a Collection
	 * 
	 * @param input the source to concatenate
	 * @param separator the char separator
	 * @return a concatenated result
	 */
	public static String concat(Collection<String> input, char separator) {
		return concat(input.toArray(new String[] {}), separator);
	}
	
	/**
	 * Joins the elements of the source array with the specified separator. <br>
	 * Skips null or empty elements entirely, ignoring them as if they didn't exist.
	 * 
	 * @param input the source array
	 * @param separator the char separator
	 * @return a concatenated result
	 */
	public static String concat(String[] input, char separator) {
		if (input.length == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String m : input) {
			if (m != null && !m.isEmpty()) {
				builder.append(separator).append(m);
			}
		}
		return builder.substring(1);
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
	
	/**
	 * Expands a 32-length short form of a UUID to its long form. <br>
	 * Both forms are unique, but some remote APIs (such as the Mojang API) return short forms.
	 * 
	 * @param shortUuid the shortened uuid form
	 * @return the expanded uuid form, which is parsable with {@link java.util.UUID#fromString(String)}
	 */
	public static String expandShortenedUUID(String shortUuid) {
		return shortUuid.substring(0, 8) + "-" + shortUuid.substring(8, 12) + "-" + shortUuid.substring(12, 16)
		+ "-" + shortUuid.substring(16, 20) + "-" + shortUuid.substring(20, 32);
	}
	
	public static String basicTodaysDate() {
		return BASIC_DATE_FORMATTER.get().format(new Date());
	}
	
}
