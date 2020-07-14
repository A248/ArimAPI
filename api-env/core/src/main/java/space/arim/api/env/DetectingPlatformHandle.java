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
import space.arim.universal.registry.RequireServices;

import space.arim.api.chat.SendableMessage;

/**
 * An implementation of {@link PlatformHandle} which automatically detects the platform in use.
 * 
 * @author A248
 *
 */
public class DetectingPlatformHandle implements PlatformHandle {

	private final PlatformHandle delegate;
	
	private static final PlatformType PLATFORM;
	
	static {
		PlatformType platform = null;

		try {
			Class.forName("org.bukkit.Server");
			platform = PlatformType.BUKKIT;
		} catch (ClassNotFoundException ignored) {	}

		if (platform == null) {
			try {
				Class.forName("net.md_5.bungee.api.ProxyServer");
				platform = PlatformType.BUNGEE;
			} catch (ClassNotFoundException ignored) {	}
		}

		if (platform == null) {
			try {
				Class.forName("org.spongepowered.api.Game");
				platform = PlatformType.SPONGE;
			} catch (ClassNotFoundException ignored) {	}
		}

		if (platform == null) {
			try {
				Class.forName("com.velocitypowered.api.proxy.ProxyServer");
				platform = PlatformType.VELOCITY;
			} catch (ClassNotFoundException ignored) {	}
		}
		PLATFORM = platform;
	}
	
	/**
	 * Creates from a {@link Registry} to use. The {@code Registry} must have a registration
	 * for {@link PlatformPluginInfo} so that such object may be used for platform-specific
	 * operations.
	 * 
	 * @param registry the registry to use
	 */
	public DetectingPlatformHandle(@RequireServices(PlatformPluginInfo.class) Registry registry) {
		switch (PLATFORM) {
		case BUKKIT:
			delegate = new BukkitPlatformHandle(registry);
			break;
		case BUNGEE:
			delegate = new BungeePlatformHandle(registry);
			break;
		case SPONGE:
			delegate = new SpongePlatformHandle(registry);
			break;
		case VELOCITY:
			delegate = new VelocityPlatformHandle(registry);
			break;
		default:
			throw new UnsupportedOperationException("Platform could not be detected");
		}
		assert PLATFORM == delegate.getPlatformType();
		
	}

	@Override
	public <T> T registerDefaultServiceIfAbsent(Class<T> service) {
		return delegate.registerDefaultServiceIfAbsent(service);
	}

	@Override
	public void sendMessage(Object player, SendableMessage message) {
		delegate.sendMessage(player, message);
	}

	@Override
	public PlatformType getPlatformType() {
		return PLATFORM;
	}

	@Override
	public PlatformPluginInfo getImplementingPluginInfo() {
		return delegate.getImplementingPluginInfo();
	}
	
}
