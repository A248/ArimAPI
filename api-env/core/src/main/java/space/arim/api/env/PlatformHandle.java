/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env;

import space.arim.universal.registry.Registry;
import space.arim.universal.util.concurrent.EnhancedExecutor;
import space.arim.universal.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformPlayer;

/**
 * An interface for working with platform-specific details, especially those not easily
 * abstracted otherwise.
 * 
 * @author A248
 *
 */
public interface PlatformHandle {

	/**
	 * Registers a known service using {@link Registry#registerIfAbsent(Class, java.util.function.Supplier)}. <br>
	 * <br>
	 * The supported services: <br>
	 * * {@link FactoryOfTheFuture} <br>
	 * * {@link EnhancedExecutor} <br>
	 * * {@link PlatformScheduler}
	 * 
	 * @param <T> the service type
	 * @param service the service class
	 * @return the result of {@link Registry#registerIfAbsent(Class, java.util.function.Supplier)}
	 * @throws IllegalArgumentException if the service is not supported
	 */
	<T> T registerDefaultServiceIfAbsent(Class<T> service);
	
	/**
	 * Sends a JSON message to a player using the API associated with {@link Message}
	 * 
	 * @param player the recipient
	 * @param message the message
	 */
	void sendMessage(@PlatformPlayer Object player, SendableMessage message);
	
	/**
	 * Gets the platform type detected
	 * 
	 * @return the platform type detected
	 */
	PlatformType getPlatformType();
	
	/**
	 * Gets the plugin info which is used for platform-specific API methods
	 * requiring platform-specific plugin information
	 * 
	 * @return the platform plugin info used
	 */
	PlatformPluginInfo getImplementingPluginInfo();
	
}
