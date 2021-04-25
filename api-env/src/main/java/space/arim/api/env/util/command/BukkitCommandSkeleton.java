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

package space.arim.api.env.util.command;

/**
 * Former location of {@link space.arim.api.env.bukkit.BukkitCommandSkeleton}
 *
 * @deprecated Use {@link space.arim.api.env.bukkit.BukkitCommandSkeleton} instead
 */
@Deprecated
public abstract class BukkitCommandSkeleton extends space.arim.api.env.bukkit.BukkitCommandSkeleton {
    /**
     * Creates from a plugin, command name, and aliases
     *
     * @param command the command name
     * @param aliases all aliases, may be empty
     */
    protected BukkitCommandSkeleton(String command, String... aliases) {
        super(command, aliases);
    }
}
