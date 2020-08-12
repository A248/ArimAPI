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

import java.util.List;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigAccessor;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigDefaultUnsetException;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.DefaultResourceProvider;
import space.arim.api.configure.ValueTransformer;
import space.arim.api.configure.impl.AbstractConfiguration;

/**
 * A configuration which uses a pair of {@link ConfigData}, one for default data, one for loaded data. <br>
 * When a value is requested per {@link #getAccessor()} , first the loaded data is checked, then the default data.
 * 
 * @author A248
 *
 */
public class PairedConfig extends AbstractConfiguration {
	
	private final ConfigData defaultData;
	private volatile ConfigData loadedData;
	
	private final ConfigAccessor accessor;
	
	PairedConfig(DefaultResourceProvider defaultResource, Executor executor, ConfigSerialiser serialiser,
			List<? extends ValueTransformer> transformers, ConfigData defaultData) {
		super(defaultResource, executor, serialiser, transformers);
		this.defaultData = defaultData;
		accessor = new ConfigAccessor() {

			@Override
			public <U> U getObject(String key, Class<U> clazz) {
				U loaded = getLoadedConfigData().getObject(key, clazz);
				if (loaded != null) {
					return loaded;
				}
				U defaultVal = getDefaultConfigData().getObject(key, clazz);
				if (defaultVal == null) {
					throw new ConfigDefaultUnsetException("Getting object for key " + key + " with class " + clazz.getName());
				}
				return defaultVal;
			}

			@Override
			public <U> List<U> getList(String key, Class<U> elementClazz) {
				
				List<U> loaded = getLoadedConfigData().getList(key, elementClazz);
				if (loaded != null) {
					return loaded;
				}
				List<U> defaultVal = getDefaultConfigData().getList(key, elementClazz);
				if (defaultVal == null) {
					throw new ConfigDefaultUnsetException("Getting list for key " + key + " with element class " + elementClazz.getName());
				}
				return defaultVal;
			}
			
		};
	}
	
	/**
	 * Gets the default config data of this {@code PairedConfig}
	 * 
	 * @return the default config data
	 */
	public ConfigData getDefaultConfigData() {
		return defaultData;
	}
	
	@Override
	public ConfigData getLoadedConfigData() {
		return loadedData;
	}

	@Override
	public ConfigAccessor getAccessor() {
		return accessor;
	}

	@Override
	protected void acceptRead(ConfigData successfulReadData) {
		loadedData = successfulReadData;
	}

}
