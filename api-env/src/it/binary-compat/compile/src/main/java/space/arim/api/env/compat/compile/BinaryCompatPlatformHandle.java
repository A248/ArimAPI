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

package space.arim.api.env.compat.compile;

import com.velocitypowered.api.plugin.PluginContainer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.api.chat.SendableMessage;
import space.arim.api.env.BukkitPlatformHandle;
import space.arim.api.env.VelocityPlatformHandle;
import space.arim.api.env.BungeePlatformHandle;

public class BinaryCompatPlatformHandle {

    public void bukkit(JavaPlugin plugin) {
        BukkitPlatformHandle handle = new BukkitPlatformHandle(plugin);
        handle.createEnhancedExecutor(); // inherited method
        handle.disconnectUser(new BukkitPlayer(), SendableMessage.empty()); // own method
    }

    public void velocity(PluginContainer plugin, com.velocitypowered.api.proxy.ProxyServer server) {
        VelocityPlatformHandle handle = new VelocityPlatformHandle(plugin, server);
        handle.createEnhancedExecutor();
        handle.disconnectUser(new VelocityPlayer(), SendableMessage.empty());
    }

    public void bungee(Plugin plugin) {
        BungeePlatformHandle handle = new BungeePlatformHandle(plugin);
        handle.createEnhancedExecutor();
        handle.disconnectUser(new BungeePlayer(), SendableMessage.empty());
    }

}
