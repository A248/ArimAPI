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

/**
 * A simple approach to getting the class which called a particular method. <br>
 * <b>Use {@link CallerFinderProvider} for an instance.</b>
 * 
 * @author A248
 *
 */
public interface CallerFinder {
	
	/**
	 * Gets the class calling a method. <br>
	 * Equivalent to {@link #getCallerClass(int)} with parameter <code>3</code>.<br>
	 * <br>
	 * <b>Example</b>: <br>
	 * A third party class (the caller) calls your API method.
	 * In your API method, use this method to get the caller.
	 * 
	 * @return the caller of the method you're using this method in
	 */
	Class<?> getCallerClass();
	
	/**
	 * Gets the caller class with a specified level of depth. <br>
	 * 0: returns this class <br>
	 * 1: returns your class <br>
	 * 2: returns the class which called your class <br>
	 * ... and so on
	 * 
	 * @param level the depth to check
	 * @return the caller class at a certain depth
	 */
	Class<?> getCallerClass(int level);
	
}
