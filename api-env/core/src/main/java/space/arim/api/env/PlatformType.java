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

/**
 * Enumeration of all platforms supported by this library
 * 
 * @author A248
 *
 */
public enum PlatformType {

	/**
	 * The Bukkit API, really the Spigot API. <br>
	 * Includes Spigot, Paper, and extended forks, plus Glowstone. <br>
	 * Mere CraftBukkit is not and should never be supported.
	 * 
	 */
	BUKKIT,
	/**
	 * The BungeeCord API <br>
	 * Includes BungeeCord and Waterfall
	 * 
	 */
	BUNGEE,
	/**
	 * The Sponge API <br>
	 * Includes SpongeVanilla and SpongeForge
	 * 
	 */
	SPONGE,
	/**
	 * The Velocity API <br>
	 * Includes Velocity
	 * 
	 */
	VELOCITY
	
}
