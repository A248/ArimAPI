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
package space.arim.api.configure;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * A general interface for all configurations.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface Configuration {
	
	/**
	 * Gets the currently loaded configuration data. <br>
	 * <br>
	 * The config data should never be modified, nor should any collections within the config data be modified.
	 * To replace or modify config data when it is loaded, use a {@link ValueTransformer}.
	 * 
	 * @return the currently loaded config data
	 * @throws ConfigNotLoadedException if the config data has never been loaded with {@link #readConfig(Path)}
	 */
	ConfigData getLoadedConfigData();
	
	/**
	 * Gets a {@link ConfigAccessor} which may be used to retrieve config values and automatically fallback
	 * to the default configuration when such values do not exist or are not of the correct type. <br>
	 * <br>
	 * Any config values which are collections should never be modified. To replace or modify config data when
	 * it is loaded, use a {@link ValueTransformer}.
	 * 
	 * @return an automatic configuration value accessor for this configuration
	 */
	ConfigAccessor getAccessor();
	
	/**
	 * Gets the {@code Executor} used to create completable futures
	 * 
	 * @return the {@code Executor} used for this config
	 */
	Executor getExecutor();
	
	/**
	 * Gets the configuration serialiser associated with this config
	 * 
	 * @return the config serialiser used by this config
	 */
	ConfigSerialiser getSerialiser();
	
	/**
	 * Reads the configuration from the specified path, applies any specifics this configuration's behaviour (updating
	 * old configs, transforming values), and sets the loaded configuration data of this {@code Configuration} to the
	 * values read. <br>
	 * If the reading was unsuccessful, the values in this {@code Configuration} will be unchanged. <br>
	 * <br>
	 * Uses this config's {@link #getSerialiser()} to perform the configuration reading itself.
	 * 
	 * @param source the file path from which to read
	 * @return a future which yields a nonnull {@link ConfigReadResult} indicating success or failure, and details of such
	 */
	CompletableFuture<ConfigReadResult> readConfig(Path source);
	
	/**
	 * Saves the default configuration to the specified path if such path does not yet exist. <br>
	 * <br>
	 * If the file does not exist, it will be created and the default configuration copied. Otherwise, this is a no-op.
	 * 
	 * @param destination the file path at which to save the default config
	 * @return a future which yields a nonnull {@link ConfigCopyResult} indicating success or failure, and details of such
	 */
	CompletableFuture<ConfigCopyResult> saveDefaultConfig(Path destination);
	
}
