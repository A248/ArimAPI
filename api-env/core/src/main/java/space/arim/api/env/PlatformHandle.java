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

import space.arim.omnibus.resourcer.ResourceHook;
import space.arim.omnibus.resourcer.Resourcer;

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
	 * Gets a resource hook for a specific resource based on this platform, using the specified {@link Resourcer}. <br>
	 * This will use the value of {@link Resourcer#hookUsage(Class, java.util.function.Supplier)} <br>
	 * <br>
	 * Exactly which {@code resourceClass} values may be specified is implementation dependent, it is up to implementers
	 * of {@code PlatformHandle} to decide which to support. Specifying a resource class other than one supported
	 * will result in a {@code IllegalArgumentException}. However, it is recommended that most implementations
	 * keep pace with the resource classes supported by select provided implementations as noted in the package javadoc.
	 * 
	 * @param <T> the resource type
	 * @param resourcer the {@link Resourcer} which is to manage the resource
	 * @param resourceClass the resource class, must be one of those supported
	 * @return the result of {@link Resourcer#hookUsage(Class, java.util.function.Supplier)}
	 * @throws IllegalArgumentException if the resource type is not supported
	 */
	<T> ResourceHook<T> hookPlatformResource(Resourcer resourcer, Class<T> resourceClass);
	
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
	 */
	RealExecutorFinder getRealExecutorFinder();
	
	/**
	 * Gets the platform type detected
	 * 
	 * @return the platform type detected
	 * @deprecated It is intended that {@code PlatformHandle} may be implemented for a variety of platforms,
	 * and not merely those defined in a fixed enum. This method may therefore return inaccurate results when
	 * the implementation cannot specify which platform from the enum it is for.
	 */
	@Deprecated
	PlatformType getPlatformType();
	
	/**
	 * Gets the plugin info which is used for platform-specific API methods
	 * requiring platform-specific plugin information
	 * 
	 * @return the platform plugin info used
	 */
	PlatformPluginInfo getImplementingPluginInfo();
	
}
