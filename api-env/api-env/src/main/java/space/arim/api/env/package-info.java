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

/**
 * The base package defines {@link space.arim.api.env.PlatformHandle}, a useful interface for interacting with platform
 * specific operations. <br>
 * <br>
 * <b>Implementations</b> <br>
 * There are provided implementations of {@code PlatformHandle} for the Bukkit, Bungee, and Velocity platforms,
 * located within their respective packages. <br>
 * <br>
 * <b>Null handling</b> <br>
 * Unless stated otherwise, methods and constructors do not accept null values and may throw
 * {@code NullPointerException} if null values are passed. <br>
 * <br>
 * <b>Thread safety</b> <br>
 * Unless stated otherwise, all classes are immutable and thread safe.
 * 
 */
package space.arim.api.env;
