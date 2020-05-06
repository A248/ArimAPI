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

import java.lang.reflect.Proxy;
import java.util.Objects;

import space.arim.shaded.org.slf4j.Logger;

import space.arim.api.util.LazySingleton;

/**
 * Used for interacting with platforms which utilise different logging frameworks. <br>
 * To get the instance, use {@link #get()}
 * 
 * @author A248
 *
 */
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
	 */
	public Logger convert(java.util.logging.Logger julLogger) {
		return new JulAsSlf4j(Objects.requireNonNull(julLogger));
	}
	
	/**
	 * Converts a slf4j logger. <br>
	 * That this method exists is a disappointment for the Minecraft community.
	 * 
	 * @param slf4jLogger the slf4j logger, presumably provided by the Sponge API
	 * @return a slf4j logger
	 */
	public Logger convert(org.slf4j.Logger slf4jLogger) {
		return (Logger) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class<?>[] {Logger.class}, new Slf4jProxyHandler(Objects.requireNonNull(slf4jLogger)));
	}
	
}
