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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigAccessor;
import space.arim.api.configure.ConfigCopyResult;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.Configuration;

/**
 * A {@link Configuration} wrapping another tied to a specific file path. Useful when the configuration
 * need only be written to or read from a certain file (as opposed to several files dynamically).
 * 
 * @author A248
 *
 */
public class SingularConfig implements Configuration {

	private final Configuration delegate;
	private final Path configPath;
	
	/**
	 * Creates from a {@code Configuration} to wrap and a {@link Path} to use for this
	 * {@code SingularConfig}'s config path
	 * 
	 * @param delegate the wrapped {@code Configuration}
	 * @param configPath the config path
	 */
	public SingularConfig(Configuration delegate, Path configPath) {
		this.delegate = delegate;
		this.configPath = configPath;
	}
	
	/**
	 * Gets the {@code Configuration} this {@code SingularConfig} is based upon
	 * 
	 * @return the wrapped {@code Configuration} instance
	 */
	public Configuration getDelegate() {
		return delegate;
	}
	
	/**
	 * Gets the config path of this {@code SingularConfig}, used for {@link #readConfig()}
	 * and {@link #saveDefaultConfig()}
	 * 
	 * @return the config path
	 */
	public Path getConfigPath() {
		return configPath;
	}
	
	@Override
	public ConfigData getLoadedConfigData() {
		return delegate.getLoadedConfigData();
	}

	@Override
	public ConfigAccessor getAccessor() {
		return delegate.getAccessor();
	}
	
	@Override
	public Executor getExecutor() {
		return delegate.getExecutor();
	}

	@Override
	public ConfigSerialiser getSerialiser() {
		return delegate.getSerialiser();
	}

	@Override
	public CompletableFuture<ConfigReadResult> readConfig(Path source) {
		return delegate.readConfig(source);
	}
	
	/**
	 * Reads the configuration from the config path of this {@code SingularConfig}
	 * 
	 * @return a future which yields a nonnull {@link ConfigReadResult} indicating success or failure, and details of such
	 */
	public CompletableFuture<ConfigReadResult> readConfig() {
		return delegate.readConfig(configPath);
	}

	@Override
	public CompletableFuture<ConfigCopyResult> saveDefaultConfig(Path destination) {
		return delegate.saveDefaultConfig(destination);
	}
	
	/**
	 * Saves the default configuration to the config path of thise {@code SingularConfig}
	 * 
	 * @return a future which yields a nonnull {@link ConfigCopyResult} indicating success or failure, and details of such
	 */
	public CompletableFuture<ConfigCopyResult> saveDefaultConfig() {
		return delegate.saveDefaultConfig(configPath);
	}

}
