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

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

final class EmptyableEqualsAndHash {

	private EmptyableEqualsAndHash() {}
	
	static <T extends Emptyable> int hashCode(List<T> emptyables) {
		final int prime = 31;
		int result = 1;
		for (Emptyable emptyable : emptyables) {
			if (!emptyable.isEmpty()) {
				result = prime * result + emptyable.hashCode();
			}
		}
		return result;
	}
	
	static <T extends Emptyable> boolean equals(List<T> list1, List<T> list2) {
		ListIterator<T> it1 = list1.listIterator();
		Iterator<T> it2 = list2.iterator();

		for (;;) {
			final boolean hasNext1 = it1.hasNext();
			final boolean hasNext2 = it2.hasNext();
			if (hasNext1 == hasNext2) {
				if (hasNext1) {
					// Both have another component
					// Skip either component if it is empty
					T element1 = it1.next();
					if (element1.isEmpty()) {
						continue;
					}
					T element2 = it2.next();
					if (element2.isEmpty()) {
						// Ensure not to forget the last element
						it1.previous();
						continue;
					}
					// If nonempty elements are nonequal, fail
					if (!element1.equals(element2)) {
						return false;
					}
				} else {
					// Both ended
					return true;
				}
			} else {
				// Only one of the iterators has another element
				T further = (hasNext1) ? it1.next() : it2.next();
				if (!further.isEmpty()) {
					return false;
				}
			}
		}
	}
	
}
