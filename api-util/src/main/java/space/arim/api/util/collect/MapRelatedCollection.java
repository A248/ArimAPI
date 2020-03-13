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
package space.arim.api.util.collect;

import java.util.Collection;
import java.util.Map;

abstract class MapRelatedCollection<E, K, V> implements Collection<E> {
	
	private final Map<K, V> map;
	
	MapRelatedCollection(Map<K, V> map) {
		this.map = map;
	}
	
	protected Map<K, V> getMap() {
		return map;
	}
	
	@Override
	public int size() {
		return getMap().size();
	}
	
	@Override
	public boolean isEmpty() {
		return getMap().isEmpty();
	}
	
}
