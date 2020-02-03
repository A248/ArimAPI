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
package space.arim.api.util;

import space.arim.universal.registry.RegistryPriority;

/**
 * Implementation of {@link CallerFinder}
 * 
 * @author A248
 *
 */
public class CallerFinderProvider extends SecurityManager implements CallerFinder {

	@Override
	public Class<?> getCallerClass() {
		return getClassContext()[2];
	}
	
	@Override
	public Class<?> getCallerClass(int level) {
		return getClassContext()[level];
	}
	
	@Override
	public byte getPriority() {
		return priority();
	}
	
	/**
	 * The priority of this implementation. Static for convenience.
	 * 
	 * @return the same priority as {@link #getPriority()}
	 */
	public static byte priority() {
		return RegistryPriority.LOWER;
	}
	
}
