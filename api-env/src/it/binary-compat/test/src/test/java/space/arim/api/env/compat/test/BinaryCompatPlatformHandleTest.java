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

package space.arim.api.env.compat.test;

import com.velocitypowered.api.plugin.PluginContainer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import space.arim.api.env.compat.compile.BinaryCompatPlatformHandle;

import java.nio.file.Path;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BinaryCompatPlatformHandleTest {

    @TempDir
    public Path dataFolder;

    private final BinaryCompatPlatformHandle tester = new BinaryCompatPlatformHandle();

    @Test
    public void bukkit() {
        Server server = mock(Server.class);
        when(server.getLogger()).thenReturn(Logger.getLogger(getClass().getName()));
        PluginLoader pluginLoader = mock(PluginLoader.class);
        JavaPlugin plugin = new JavaPlugin(
                pluginLoader, server, new PluginDescriptionFile("", "", ""), dataFolder.toFile(), null) {};
        tester.bukkit(plugin);
    }

    @Test
    public void velocity() {
        tester.velocity(mock(PluginContainer.class), mock(com.velocitypowered.api.proxy.ProxyServer.class));
    }

    @Test
    public void bungee() {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getProxy()).thenReturn(mock(net.md_5.bungee.api.ProxyServer.class));
        tester.bungee(plugin);
    }
}
