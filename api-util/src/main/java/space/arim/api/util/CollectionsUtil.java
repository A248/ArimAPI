/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class to apply operations to collections and arrays.
 * 
 * @author A248
 *
 * @deprecated The only methods in this class, {@link #random(Collection)} and {@link #strongRandom(Collection)},
 * can be easily implemented in a local utility. They provide little value of their own.
 */
@Deprecated(forRemoval = true)
public final class CollectionsUtil {

	// Prevent instantiation
	private CollectionsUtil() {}
	
	/**
	 * Gets a random element from a collection. <br>
	 * If the input collection is null or empty, <code>null</code> is returned
	 * 
	 * @param <T> the type of the collection
	 * @param collection the collection
	 * @return a random element from the collection, or <code>null</code> if the collection is null or empty
	 * @deprecated This method used to have a different implementation than calling <code>collection.toArray()</code> and
	 * drawing a random element from the resulting array. However, such is the easiest and most effective way of
	 * withdrawing a random element from a collection. This method now does exactly that.
	 */
	@Deprecated
	public static <T> T strongRandom(Collection<T> collection) {
		return random(collection);
	}
	
	/**
	 * Gets a random element from a collection. <br>
	 * If the input collection is null or empty, <code>null</code> is returned.
	 * 
	 * @param <T> the type of the collection
	 * @param collection the collection
	 * @return a random element from the collection, or <code>null</code> if the collection is null or empty
	 * @deprecated This method used to have a different implementation than calling <code>collection.toArray()</code> and
	 * drawing a random element from the resulting array. However, such is the easiest and most effective way of
	 * withdrawing a random element from a collection. This method now does exactly that.
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static <T> T random(Collection<T> collection) {
		if (collection == null) {
			return null;
		}
		Object[] array = collection.toArray();
		if (array.length == 0) {
			return null;
		}
		return (T) array[ThreadLocalRandom.current().nextInt(array.length)];
	}
	
}
