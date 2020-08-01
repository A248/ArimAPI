/* 
 * ArimAPI-version
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-version is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-version is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-version. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.version;

/**
 * Version constant holder class
 * 
 * @author A248
 *
 */
public class ArimAPIVersion {

	/**
	 * The version of ArimAPI <br>
	 * (protected against constant folding)
	 * 
	 */
	public static final String VERSION = "${project.version}".toString(); // toString() prevents folding
	
	private ArimAPIVersion() {}
	
}
