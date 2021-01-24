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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * A reader and writer of configuration data, implemented for a single format.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface ConfigSerialiser {
	
	/**
	 * Reads config data from a default resource provider. <br>
	 * <br>
	 * Returns a future which yields a nonnull {@link ConfigReadResult} allowing the caller to determine the results of the read,
	 * including whether it succeeded or failed.
	 * 
	 * @param defaultResource the default resource provider from which to read the configuration
	 * @param executor the executor used to create the resulting future
	 * @param transformers value transformer used to process all config values
	 * @return a future which yields a nonnull {@link ConfigWriteResult} indicating success or failure, and details of such
	 */
	CompletableFuture<ConfigReadResult> readConfig(DefaultResourceProvider defaultResource, Executor executor,
			List<? extends ValueTransformer> transformers);
	
	/**
	 * Reads config data from the desired target path. <br>
	 * <br>
	 * Returns a future which yields a nonnull {@link ConfigReadResult} allowing the caller to determine the results of the read,
	 * including whether it succeeded or failed.
	 * 
	 * @param source the path from which to read the configuration
	 * @param executor the executor used to create the resulting future
	 * @param transformers value transformer used to process all config values
	 * @return a future which yields a nonnull {@link ConfigWriteResult} indicating success or failure, and details of such
	 */
	CompletableFuture<ConfigReadResult> readConfig(Path source, Executor executor,
			List<? extends ValueTransformer> transformers);
	
	/**
	 * Saves the specified config data to the desired target path on the filesystem. If the file already exists,
	 * it will be overwritten. <br>
	 * <br>
	 * Returns a future which yields a nonnull {@link ConfigWriteResult} allowing the caller to determine the results of the write,
	 * including whether it succeeded or failed. <br>
	 * <br>
	 * <b>Serialisation</b> <br>
	 * If the object is a typical serialisable object, such as a string, integer, boolean, or list, it will be serialised thus. <br>
	 * <br>
	 * Otherwise, if the config value is of another type, a different procedure will be used. The object will be written to the map
	 * as a string, per {@link ConfigSerialisable#toConfigString()} if it implements such interface, or {@link Object#toString()}
	 * if it does not.
	 * 
	 * @param target the path to which to save the configuration
	 * @param executor the executor used to create the resulting future
	 * @param data the configuration data to save
	 * @return a future which yields a nonnull {@link ConfigWriteResult} indicating success or failure, and details of such
	 */
	CompletableFuture<ConfigWriteResult> writeConfig(Path target, Executor executor, ConfigData data);
	
}
