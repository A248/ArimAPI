/* 
 * ArimAPI-util-web
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util-web. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.web;

import java.util.UUID;

/**
 * Utility class for manipulating valid UUIDs. Allows conversions between
 * {@link UUID}s, full UUID strings, short UUID strings, and byte arrays. <br>
 * <br>
 * All methods do not permit null, and may throw {@code NullPointerException}
 * otherwise. <br>
 * <br>
 * <b>A Note on Preconditions</b> <br>
 * So that it may be used to operate on large volumes of data, this class does
 * not check preconditions. It assumes all UUIDs, strings, and byte arrays are
 * valid representations. Malformed input may lead to unexpected behaviour.
 * Callers are encouraged to validate their own input. <br>
 * <br>
 * <b>UUID Forms</b> <br>
 * This class recognises 3 forms of UUIDs besides {@code java.util.UUID}: <br>
 * 1. Full UUID strings. This the common string based representation of a UUID
 * as defined by {@link UUID#toString()}. UUIDs in this form may be converted
 * back to a {@code java.util.UUID} via the JDK's
 * {@link UUID#fromString(String)}. <br>
 * 2. Short UUID strings. These are the same as full UUID strings except that
 * they are not hyphenated. <br>
 * 3. Byte arrays. These must be 16 bytes in length. <br>
 * <br>
 * Methods are provided for efficient conversion between: <br>
 * 1. Full UUID strings and short UUID strings. <br>
 * 2. Short UUID strings and {@code java.util.UUID}. <br>
 * 3. Byte arrays and {@code java.util.UUID}. <br>
 * <br>
 * Where applicable, conversion methods are designed to be at least as
 * performant as more roundabout approaches. For example,
 * {@link #fromShortString(String)} should be faster than combining
 * {@link #expandShortString(String)} and {@link UUID#fromString(String)}.
 * 
 * @author A248
 *
 * @deprecated Please use {@link space.arim.omnibus.util.UUIDUtil}, which is nearly identical, instead.
 */
@Deprecated(forRemoval = true)
public class UUIDUtil {

	/*
	 * 
	 * UUID String Conversions
	 * 
	 */

	/**
	 * Expands a shortened version of a UUID to the full string form. Inverse
	 * operation of {@link #contractFullString(String)}
	 * 
	 * @param shortUuid the short uuid string
	 * @return the full uuid string
	 * @throws IllegalArgumentException if {@code shortUuid} is not of length 32
	 */
	public static String expandShortString(String shortUuid) {
		return space.arim.omnibus.util.UUIDUtil.expandShortString(shortUuid);
	}

	/**
	 * Contracts the full form of a UUID string to its shortened form. This is the
	 * inverse operation of {@link #expandShortString(String)}, and should be
	 * identical to replacing/removing all hyphens in the full uuid string, e.g.
	 * <code>fullUuid.replace("{@literal -}", "")</code> for a valid full UUID
	 * string called {@code fullUuid}.
	 * 
	 * @param fullUuid the full uuid string
	 * @return the short uuid string
	 * @throws IllegalArgumentException if {@code fullUuid} is not of length 36
	 */
	public static String contractFullString(String fullUuid) {
		return space.arim.omnibus.util.UUIDUtil.contractFullString(fullUuid);
	}

	/*
	 * 
	 * Short form conversions
	 * 
	 */

	/**
	 * Converts a {@code UUID} to its short form string representation. This is the
	 * inverse operation of {@link #fromShortString(String)}. <br>
	 * <br>
	 * The returned string will always be of length 32.
	 * 
	 * @param uuid the UUID
	 * @return the short uuid string
	 */
	public static String toShortString(UUID uuid) {
		return space.arim.omnibus.util.UUIDUtil.toShortString(uuid);
	}

	/**
	 * Converts a short form uuid string to a {@code UUID}. This is the inverse
	 * operation of {@link #toShortString(UUID)}
	 * 
	 * @param shortUuid the short uuid string
	 * @return the UUID
	 * @throws IllegalArgumentException if {@code shortUuid} is not of length 32
	 * @throws NumberFormatException if {@code shortUuid} is not a valid short uuid string
	 */
	public static UUID fromShortString(String shortUuid) {
		return space.arim.omnibus.util.UUIDUtil.fromShortString(shortUuid);
	}

	/*
	 * 
	 * Byte Array Conversions
	 * 
	 */

	/**
	 * Creates a byte array and writes a UUID to it. This would be the inverse
	 * operation of {@link #fromByteArray(byte[])}
	 * 
	 * @param uuid the UUID
	 * @return the byte array, will always be length 16
	 */
	public static byte[] toByteArray(UUID uuid) {
		return space.arim.omnibus.util.UUIDUtil.toByteArray(uuid);
	}

	/**
	 * Writes a UUID to a byte array at the specified offset. This would be the
	 * inverse operation of {@link #fromByteArray(byte[], int)}
	 * 
	 * @param uuid      the UUID
	 * @param byteArray the byte array to write to, must be at least of length
	 *                  (offset + 16)
	 * @param offset    the offset in the byte array after which to write bytes
	 * @throws IndexOutOfBoundsException if the byte array is not of the right size
	 */
	public static void toByteArray(UUID uuid, byte[] byteArray, int offset) {
		space.arim.omnibus.util.UUIDUtil.toByteArray(uuid, byteArray, offset);
	}

	/**
	 * Reads a UUID from a byte array. This is the inverse operation of
	 * {@link #toByteArray(UUID)}.
	 * 
	 * @param byteArray the byte array to read from, must be at least of length 16
	 * @return the UUID
	 * @throws IndexOutOfBoundsException if the byte array is not of the right size
	 */
	public static UUID fromByteArray(byte[] byteArray) {
		return space.arim.omnibus.util.UUIDUtil.fromByteArray(byteArray);
	}

	/**
	 * Reads a UUID from a byte array with a specified offset. This is the inverse
	 * operation of {@link #toByteArray(UUID, byte[], int)}
	 * 
	 * @param byteArray the byte array to read from, must be at least of length
	 *                  (offset + 16)
	 * @param offset    the offset after which to begin reading bytes
	 * @return the UUID
	 * @throws IndexOutOfBoundsException if the byte array is not of the right size
	 */
	public static UUID fromByteArray(byte[] byteArray, int offset) {
		return space.arim.omnibus.util.UUIDUtil.fromByteArray(byteArray, offset);
	}

}
