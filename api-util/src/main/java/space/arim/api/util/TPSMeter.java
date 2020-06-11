/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util;

/**
 * Measures the exact current TPS.
 * 
 * @author A248
 *
 * @deprecated Not all platforms have the concept of a main thread, in which case,
 * the meaning of "TPS" is ill-defined, and cannot be abstracted into an interface which applies
 * to more than the specific platforms which measure TPS.
 */
@Deprecated(forRemoval = true)
public interface TPSMeter {

	/**
	 * Gets the value of the current server TPS. <br>
	 * <b>TPS <i>may</i> be higher than 20!</b>
	 * 
	 * @return the exact TPS
	 */
	double getTPS();
	
}
