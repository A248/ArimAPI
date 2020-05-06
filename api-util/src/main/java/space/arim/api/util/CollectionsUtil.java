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
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class to apply operations to collections and arrays. <br>
 * <br>
 * <b>Designed to reduce boilerplate operations</b>.
 * 
 * @author A248
 *
 */
public final class CollectionsUtil {

	// Prevent instantiation
	private CollectionsUtil() {}
	
	/**
	 * Gets a random element from a collection. <br>
	 * If the input collection is <code>null</code> or empty, <code>null</code> is returned. <br>
	 * <br>
	 * This method is thread safe so long as the collection's iterator is thread safe.
	 * The underlying collection may be concurrently modified without compromising
	 * the integrity of this method call.
	 * 
	 * @param <T> the type of the collection
	 * @param collection the collection
	 * @return a random element from the collection, or <code>null</code> if preconditions are not met
	 */
	public static <T> T random(Collection<T> collection) {
		if (collection == null) {
			return null;
		}
		int n = 0;
		int size = collection.size();
		if (size == 0) {
			return null;
		}
		// alright, the collection is non-empty, get a random index
		int index = ThreadLocalRandom.current().nextInt(size);
		// scan the collection to find the element at the index
		for (Iterator<T> it = collection.iterator(); it.hasNext();) {
			if (n == index) {
				return it.next();
			}
			it.next();
			n++;
		}
		// huh, we must've encountered a concurrency problem, so we'll repeat
		return random(collection);
	}
	
}
