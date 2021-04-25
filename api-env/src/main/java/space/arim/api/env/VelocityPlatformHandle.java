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

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;

/**
 * Previous location of {@code PlatformHandle} implementation for velocity.
 * Now moved to {@link space.arim.api.env.velocity.VelocityPlatformHandle}
 *
 * @deprecated Use {@link space.arim.api.env.velocity.VelocityPlatformHandle} instead
 */
@Deprecated
public class VelocityPlatformHandle extends space.arim.api.env.velocity.VelocityPlatformHandle {
    /**
     * Creates from a {@code PluginContainer} and {@code ProxyServer} to use
     *
     * @param plugin the plugin
     * @param server the server
     */
    public VelocityPlatformHandle(PluginContainer plugin, ProxyServer server) {
        super(plugin, server);
    }
}
