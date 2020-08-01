/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

/**
 * The result of either reading configuration values <i>from</i> a path on the filesystem, or writing default values to such path.
 * 
 * @author A248
 *
 */
public interface ConfigResult {

	/**
	 * Gets the result definition of this result
	 * 
	 * @return the kind of the result
	 */
	ResultDefinition getResultDefinition();
	
	/**
	 * Gets the exception of this result if {@code !getResultType().isSuccess()},
	 * else {@code null}
	 * 
	 * @return gets the exception of this result, or {@code null} if this result is a success
	 */
	Exception getException();
	
	/**
	 * A common supertype for the ways in which loading or saving config values may succeed or fail
	 * 
	 * @author A248
	 *
	 */
	interface ResultDefinition {
		
		/**
		 * If this result indicates success.
		 * 
		 * @return true if this result is a success, false otherwise
		 */
		boolean isSuccess();
		
	}
	
}
