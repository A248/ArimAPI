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

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Previous location of {@code PlatformHandle} implementation for bukkit.
 * Now moved to {@link space.arim.api.env.bukkit.BukkitPlatformHandle}
 *
 * @deprecated Use {@link space.arim.api.env.bukkit.BukkitPlatformHandle} instead
 */
@Deprecated
public class BukkitPlatformHandle extends space.arim.api.env.bukkit.BukkitPlatformHandle {

    /**
     * Creates from a {@code JavaPlugin} to use
     *
     * @param plugin the plugin
     */
    public BukkitPlatformHandle(JavaPlugin plugin) {
        super(plugin);
    }

}
