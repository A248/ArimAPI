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
package space.arim.api.util.log;

import org.slf4j.Logger;

/**
 * Used for interacting with platforms which utilise different logging frameworks.
 * 
 * @author A248
 *
 */
public class LoggerConverter {
	
	/**
	 * Converts a JUL logger to a slf4j Logger. <br>
	 * A simple fix for developers who rely on APIs which provide JUL loggers but wish to use slf4j. <br>
	 * <br>
	 * Level mappings are identical to those specified by the SLF4JBridgeHandler docs: <br>
	 * FINEST  = TRACE <br>
	 * FINER   = DEBUG <br>
	 * FINE    = DEBUG <br>
	 * CONFIG  = INFO <br>
	 * INFO    = INFO <br>
	 * WARNING = WARN <br>
	 * SEVERE  = ERROR
	 * 
	 * @param julLogger the JUL logger
	 * @return a slf4j Logger
	 */
	public static Logger convert(java.util.logging.Logger julLogger) {
		return new JulAsSlf4j(julLogger);
	}
	
}
