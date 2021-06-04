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
 * Platform-specific server object. <br>
 * <br>
 * Bukkit: Server (org.bukkit.Server) <br>
 * Bungee: ProxyServer (net.md_5.bungee.api.ProxyServer) <br>
 * Velocity: ProxyServer (com.velocitypowered.api.proxy.ProxyServer)
 * 
 * 
 * @author A248
 *
 */
@Retention(CLASS)
@Target({ TYPE_PARAMETER, TYPE_USE })
public @interface PlatformServer {

}
