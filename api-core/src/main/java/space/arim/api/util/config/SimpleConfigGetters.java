/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.util.config;

import java.util.List;

public interface SimpleConfigGetters {
	
	<T> T getObject(String key, Class<T> type);
	
	@SuppressWarnings("unchecked")
	default <T> List<T> getObjects(String key, Class<T> type) {
		List<?> obj = getObject(key, List.class);
		return obj.isEmpty() || type.isInstance(obj.get(0)) ? (List<T>) obj : null;
	}
	
	default String getString(String key) {
		return getObject(key, String.class);
	}
	
	default int getInt(String key) {
		return getObject(key, Integer.class);
	}
	
	default boolean getBoolean(String key) {
		return getObject(key, Boolean.class);
	}
	
	default List<String> getStrings(String key) {
		return getObjects(key, String.class);
	}
	
	default List<Integer> getInts(String key) {
		return getObjects(key, Integer.class);
	}
	
}
