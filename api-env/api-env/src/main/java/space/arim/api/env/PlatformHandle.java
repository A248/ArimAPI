/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
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
package space.arim.api.env;

import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

/**
 * An interface for working with platform-specific details, especially those not easily
 * abstracted otherwise.
 * 
 * @author A248
 *
 */
public interface PlatformHandle {
	
	/**
	 * Creates a {@link FactoryOfTheFuture} implementation for this platform. <br>
	 * <br>
	 * Note that the returned implementation may or may not be {@link AutoCloseable}. The penalty for failing to
	 * close such resources is small, but not negligible. The caller is strongly recommended to close the returned
	 * instance when disposing of it.
	 * 
	 * @return a futures factory implementation for this platform
	 */
	FactoryOfTheFuture createFuturesFactory();
	
	/**
	 * Creates a {@link EnhancedExecutor} implementation for this platform. The implementing
	 * enhanced executor will take advantage of the platform's common thread pool.
	 * 
	 * @return an enhanced executor implementation for this platform
	 */
	EnhancedExecutor createEnhancedExecutor();
	
	/**
	 * Gets the plugin info which is used for platform-specific API methods
	 * requiring platform-specific plugin information
	 * 
	 * @return the platform plugin info used
	 */
	PlatformPluginInfo getImplementingPluginInfo();
	
	/**
	 * Gets an informative string describing the version of the platform this handle is operating on. <br>
	 * <br>
	 * This is intended for debugging and/or monitoring purposes.
	 * 
	 * @return the platform version
	 */
	default String getPlatformVersion() {
		return "";
	}
	
}
