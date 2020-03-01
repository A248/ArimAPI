/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Denotes a field, method, or class should <b>ONLY</b> <i>ever</i> be accessed on a specific platform or environment. <br>
 * The caller should be certain they are operating within the exact context as specified by the value of the annotation.
 * 
 * @author A248
 *
 */
@Retention(CLASS)
@Target({ TYPE, FIELD, METHOD })
public @interface Platform {
	
	/**
	 * The platforms which the annotation target applies to.
	 * 
	 * @return the applicable platforms
	 */
	Type[] value();
	
	public enum Type {
		
		/**
		 * Applies to servers implementing the Bukkit and Spigot APIs.
		 * 
		 */
		SPIGOT,
		/**
		 * Applies to servers implementing the Bukkit, Spigot, and Paper APIs.
		 * 
		 */
		PAPER,
		/**
		 * Applies to servers implementing the BungeeCord API.
		 * 
		 */
		BUNGEE,
		/**
		 * Applies to servers implementing the Sponge API.
		 * 
		 */
		SPONGE;
		
	}
	
}
