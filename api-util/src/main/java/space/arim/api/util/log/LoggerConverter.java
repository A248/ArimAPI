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

import java.util.Objects;

import org.slf4j.Logger;

import space.arim.api.util.LazySingleton;

/**
 * Used for interacting with platforms which utilise different logging frameworks. <br>
 * To get the instance, use {@link #get()}
 * 
 * @author A248
 *
 * @deprecated This class is obsolete because its sole purpose, {@link #convert(java.util.logging.Logger)}, has been deprecated.
 */
@Deprecated
public class LoggerConverter {
	
	private static final LazySingleton<LoggerConverter> INST = new LazySingleton<LoggerConverter>(LoggerConverter::new);
	
	/**
	 * Gets the instance
	 * 
	 * @return the instance
	 */
	public static LoggerConverter get() {
		return INST.get();
	}
	
	/**
	 * Converts a JUL logger. <br>
	 * A simple fix for developers who rely on APIs which provide JUL loggers. <br>
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
	 * @return a slf4j logger
	 * 
	 * @deprecated This method encourages an incorrect use of loggers. Instead of having
	 * a global logger instance for a plugin which must be used at all times, thus necessitating
	 * a converter logger for different logging APIs which this method provides, programmers
	 * should simply derive loggers directly from the logging factory from the framework desired.
	 */
	@Deprecated
	public Logger convert(java.util.logging.Logger julLogger) {
		return new JulAsSlf4j(Objects.requireNonNull(julLogger));
	}
	
}
