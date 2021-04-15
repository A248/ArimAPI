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

import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;

import space.arim.api.chat.SendableMessage;
import space.arim.api.env.annote.PlatformCommandSender;
import space.arim.api.env.annote.PlatformPlayer;
import space.arim.api.env.chat.PlatformMessageForwardAdapter;
import space.arim.api.env.realexecutor.RealExecutorFinder;

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
	 * Sends a {@link SendableMessage} to a command sender based on this platform. <br>
	 * <br>
	 * <b>This method is not thread safe on all platforms.</b> <br>
	 * <br>
	 * Implementations may be more efficient than using the platform's {@link PlatformMessageForwardAdapter} to convert
	 * to a platform specific type.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 */
	void sendMessage(@PlatformCommandSender Object recipient, SendableMessage message);
	
	/**
	 * Disconnects (kicks) a player based on this platform with the reason provided {@link SendableMessage}. <br>
	 * <br>
	 * <b>This method is not thread safe on all platforms.</b> <br>
	 * <br>
	 * Implementations may be more efficient than using the platform's {@link PlatformMessageForwardAdapter} to convert
	 * to a platform specific type.
	 * 
	 * @param user the user to kick
	 * @param reason the kick message
	 */
	void disconnectUser(@PlatformPlayer Object user, SendableMessage reason);
	
	/**
	 * Gets a {@link RealExecutorFinder} for this platform
	 * 
	 * @return a {@code RealExecutorFinder}
	 * @deprecated See {@link space.arim.api.env.realexecutor}
	 */
	@Deprecated
	RealExecutorFinder getRealExecutorFinder();
	
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
