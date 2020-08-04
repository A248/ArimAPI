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
package space.arim.api.configure.configs;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigAccessor;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigDefaultUnsetException;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.ValueTransformer;
import space.arim.api.configure.impl.AbstractConfiguration;
import space.arim.api.configure.impl.SimpleConfigData;

/**
 * A configuration similar to but differing slightly in implementation from {@link PairedConfig}. Whereas the other
 * maintains two {@code ConfigData} instances at all times, one of default values and one of loaded values,
 * {@code MergingConfig} maintains a single {@link ConfigData} which is a reasonable combination of default values
 * and loaded values. <br>
 * <br>
 * When the config is reloaded, the existing {@code ConfigData} is combined with the freshly read one. This merging
 * works such that, for any key present in the raw maps of both {@code ConfigData}, the resulting {@code ConfigData}
 * will use the value from the read {@code ConfigData} if it is of the same type as that in the existing {@code ConfigData}.
 * Additionally, all key value mappings present in the existing {@code ConfigData} but not present in the read
 * {@code ConfigData} are copied to the resulting {@code ConfigData}. <br>
 * <br>
 * Moreover, {@code MergingConfig} solidies the state of the resulting {@code ConfigData}, after a merge, guaranteeing
 * concretely that it cannot be modified, by wrapping all collections by their immutable factory copiers ({@code List.copyOf}
 * and {@code Map.copyOf}). This is done for instances of lists in the values of the {@code ConfigData}, as well as the maps
 * which represent the config structures themselves.
 * 
 * @author A248
 *
 */
public class MergingConfig extends AbstractConfiguration {

	private volatile ConfigData currentData;
	private final Object lock = new Object();
	
	private final ConfigAccessor accessor;
	
	MergingConfig(Path defaultResource, Executor executor, ConfigSerialiser serialiser,
			List<? extends ValueTransformer> transformers, ConfigData defaultData) {
		super(defaultResource, executor, serialiser, transformers);
		currentData = defaultData;
		accessor = new ConfigAccessor() {

			@Override
			public <U> U getObject(String key, Class<U> clazz) {
				U value = currentData.getObject(key, clazz);
				if (value == null) {
					throw new ConfigDefaultUnsetException("Getting object for key " + key + " with class " + clazz.getName());
				}
				return value;
			}

			@Override
			public <U> List<U> getList(String key, Class<U> elementClazz) {
				List<U> list = currentData.getList(key, elementClazz);
				if (list == null) {
					throw new ConfigDefaultUnsetException("Getting list for key " + key + " with element class " + elementClazz.getName());
				}
				return list;
			}
			
		};
	}

	@Override
	public ConfigData getLoadedConfigData() {
		return currentData;
	}

	@Override
	public ConfigAccessor getAccessor() {
		return accessor;
	}

	@Override
	protected void acceptRead(ConfigData successfulReadData) {
		synchronized (lock) {
			this.currentData = merge(successfulReadData);
		}
	}
	
	private ConfigData merge(ConfigData readData) {
		ConfigData currentData = this.currentData;
		Map<String, Object> values = merge(currentData.getValuesMap(), readData.getValuesMap());
		return new SimpleConfigData(values, currentData.getCommentsMap());
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> merge(Map<String, Object> existing, Map<String, Object> mergeWith) {
		Map<String, Object> result = new LinkedHashMap<>(existing);
		for (Map.Entry<String, Object> mergeEntry : mergeWith.entrySet()) {
			String key = mergeEntry.getKey();
			Object value = result.get(key);
			if (value == null) {
				// non-shared key
				continue;
			}
			Object mergeValue = mergeEntry.getValue();
			if (mergeValue == null) {
				// what is caller doing
				continue;
			}
			if (value instanceof Map<?, ?>) {
				if (mergeValue instanceof Map<?, ?>) {
					result.put(key, merge((Map<String, Object>) value, (Map<String, Object>) mergeValue));
				}
			} else if (value.getClass() == mergeValue.getClass()) {
				result.put(key, mergeValue);
			}
		}
		return result;
	}

}
