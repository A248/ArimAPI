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
package space.arim.api.configure.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;

class BaseConfigData implements ConfigData {

	private final Map<String, Object> values;
	private final Map<String, List<ConfigComment>> comments;
	
	private static final Pattern NODE_SEPARATOR_PATTERN = Pattern.compile(".", Pattern.LITERAL);
	
	BaseConfigData(Map<String, Object> values, Map<String, List<ConfigComment>> comments) {
		this.values = Objects.requireNonNull(values, "Values must not be null");
		this.comments = Objects.requireNonNull(comments, "Comments must not be null");
	}
	
	/**
	 * Gets a configuration value from a nested map, or null if it does not exist
	 * or is not of type <code>T</code>.
	 * 
	 * @param <T> the type of the object to retrieve
	 * @param map the map of (nested) keys and values to search
	 * @param key the key path at which to retrieve the object
	 * @param clazz the class of the type to retrieve, used internally for instance checks
	 * @return the object if it exists and is of the specified instance, <code>null</code> otherwise
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getFromNestedMap(Map<String, Object> map, String key, Class<T> clazz) {
		if (key.indexOf('.') == -1) {
			Object value = map.get(key);
			return (clazz.isInstance(value)) ? (T) value : null;
		}
		String[] keyParts = NODE_SEPARATOR_PATTERN.split(key);
		Map<String, Object> currentMap = map;

		int lastIndex = keyParts.length - 1;
		for (int n = 0; n < keyParts.length; n++) {

			String subKey = keyParts[n];
			Object subValue = currentMap.get(subKey);

			if (n == lastIndex) {
				if (clazz.isInstance(subValue)) {
					return (T) subValue;
				}

			} else {
				if (!(subValue instanceof Map<?, ?>)) {
					return null;
				}
				currentMap = (Map<String, Object>) subValue;
			}
		}
		return null;
	}

	@Override
	public <U> U getObject(String key, Class<U> clazz) {
		return getFromNestedMap(values, key, clazz);
	}
	
	@Override
	public <U> List<U> getList(String key, Class<U> elementClazz) {
		List<?> list = getFromNestedMap(values, key, List.class);
		if (list == null) {
			return null;
		}
		for (Object e : list) {
			if (!elementClazz.isInstance(e)) {
				return null;
			}
		}
		@SuppressWarnings("unchecked")
		List<U> result = (List<U>) list;
		return result;
	}

	@Override
	public Set<String> getKeys() {
		return values.keySet();
	}

	@Override
	public Set<String> getKeys(String key) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = getFromNestedMap(values, key, Map.class);
		if (map == null) {
			return Set.of();
		}
		return map.keySet();
	}

	@Override
	public Map<String, Object> getValuesMap() {
		return values;
	}

	@Override
	public Map<String, List<ConfigComment>> getCommentsMap() {
		return comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + values.hashCode();
		result = prime * result + comments.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ConfigData)) {
			return false;
		}
		ConfigData other = (ConfigData) object;
		return values.equals(other.getValuesMap()) && comments.equals(other.getCommentsMap());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [values=" + values + ", comments=" + comments + "]";
	}
	
}
