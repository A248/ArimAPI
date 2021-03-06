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
package space.arim.api.env.annote;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Platform-specific plugin object. <br>
 * <br>
 * Bukkit: JavaPlugin which must be enabled (org.bukkit.plugin.java.JavaPlugin) <br>
 * Bungee: Plugin which must be enabled (net.md_5.bungee.api.plugin.Plugin) <br>
 * Velocity: PluginContainer with loaded instance (com.velocitypowered.api.plugin.PluginContainer)
 * 
 * 
 * @author A248
 *
 */
@Retention(CLASS)
@Target({ TYPE_PARAMETER, TYPE_USE })
public @interface PlatformPlugin {

}
