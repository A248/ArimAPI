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

import net.md_5.bungee.api.plugin.Plugin;

/**
 * Previous location of {@code PlatformHandle} implementation for bungeecord.
 * Now moved to {@link space.arim.api.env.bungee.BungeePlatformHandle}
 *
 * @deprecated Use {@link space.arim.api.env.bungee.BungeePlatformHandle} instead
 */
@Deprecated
public class BungeePlatformHandle extends space.arim.api.env.bungee.BungeePlatformHandle {
    /**
     * Creates from a {@code Plugin} to use
     *
     * @param plugin the plugin
     */
    public BungeePlatformHandle(Plugin plugin) {
        super(plugin);
    }
}
