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
package space.arim.api.util.log;

import java.util.logging.Level;

import space.arim.shaded.org.slf4j.Logger;
import space.arim.shaded.org.slf4j.Marker;

/**
 * Converts a JUL logger to a slf4j Logger. <br>
 * A simple fix for developers who rely on APIs which provide JUL loggers but wish to use slf4j. <br>
 * <br>
 * Level mappings are identical to those specified by the SLF4JBridgeHandler docs: <br>
 * * FINEST  = TRACE <br>
 * * FINER   = DEBUG <br>
 * * FINE    = DEBUG <br>
 * * CONFIG  = INFO <br>
 * * INFO    = INFO <br>
 * * WARNING = WARN <br>
 * * SEVERE  = ERROR
 * 
 * @author A248
 *
 */
class JulAsSlf4j implements Logger {
	
	private final java.util.logging.Logger julLogger;
	
	/**
	 * Creates a logger implementing the slf4j API based on a JUL logger. <br>
	 * Calls using slf4j methods are rewritten to equivalent JUL calls.
	 * 
	 * @param julLogger the source logger
	 */
	JulAsSlf4j(java.util.logging.Logger julLogger) {
		this.julLogger = julLogger;
	}
	
	@Override
	public String getName() {
		return julLogger.getName();
	}
	
	@Override
	public boolean isTraceEnabled() {
		return julLogger.isLoggable(Level.FINEST);
	}
	
	@Override
	public void trace(String msg) {
		julLogger.log(Level.FINEST, msg);
	}
	
	@Override
	public void trace(String msg, Object obj) {
		julLogger.log(Level.FINEST, msg, obj);
	}
	
	@Override
	public void trace(String msg, Object obj1, Object obj2) {
		julLogger.log(Level.FINEST, msg, new Object[] {obj1, obj2});
	}
	
	@Override
	public void trace(String msg, Object...params) {
		julLogger.log(Level.FINEST, msg, params);
	}
	
	@Override
	public void trace(String msg, Throwable ex) {
		julLogger.log(Level.FINEST, msg, ex);
	}
	
	@Override
	public boolean isTraceEnabled(Marker marker) {
		return isTraceEnabled();
	}
	
	@Override
	public void trace(Marker marker, String msg) {
		trace(msg);
	}
	
	@Override
	public void trace(Marker marker, String msg, Object obj) {
		trace(msg, obj);
	}
	
	@Override
	public void trace(Marker marker, String msg, Object obj1, Object obj2) {
		trace(msg, obj1, obj2);
	}
	
	@Override
	public void trace(Marker marker, String msg, Object...params) {
		trace(msg, params);
	}
	
	@Override
	public void trace(Marker marker, String msg, Throwable ex) {
		trace(msg, ex);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return julLogger.isLoggable(Level.FINE);
	}
	
	@Override
	public void debug(String msg) {
		julLogger.log(Level.FINE, msg);
	}

	@Override
	public void debug(String msg, Object obj) {
		julLogger.log(Level.FINE, msg, obj);
	}

	@Override
	public void debug(String msg, Object obj1, Object obj2) {
		julLogger.log(Level.FINE, msg, new Object[] {obj1, obj2});
	}

	@Override
	public void debug(String msg, Object...params) {
		julLogger.log(Level.FINE, msg, params);
	}

	@Override
	public void debug(String msg, Throwable ex) {
		julLogger.log(Level.FINE, msg, ex);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return isDebugEnabled();
	}

	@Override
	public void debug(Marker marker, String msg) {
		debug(msg);
	}

	@Override
	public void debug(Marker marker, String msg, Object obj) {
		debug(msg, obj);
	}

	@Override
	public void debug(Marker marker, String msg, Object obj1, Object obj2) {
		debug(msg, obj1, obj2);
	}

	@Override
	public void debug(Marker marker, String msg, Object...params) {
		debug(msg, params);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable ex) {
		debug(msg, ex);
	}
	
	@Override
	public boolean isInfoEnabled() {
		return julLogger.isLoggable(Level.INFO);
	}
	
	@Override
	public void info(String msg) {
		julLogger.log(Level.INFO, msg);
	}

	@Override
	public void info(String msg, Object obj) {
		julLogger.log(Level.INFO, msg, obj);
	}

	@Override
	public void info(String msg, Object obj1, Object obj2) {
		julLogger.log(Level.INFO, msg, new Object[] {obj1, obj2});
	}

	@Override
	public void info(String msg, Object...params) {
		julLogger.log(Level.INFO, msg, params);
	}

	@Override
	public void info(String msg, Throwable ex) {
		julLogger.log(Level.INFO, msg, ex);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return isInfoEnabled();
	}

	@Override
	public void info(Marker marker, String msg) {
		info(msg);
	}

	@Override
	public void info(Marker marker, String msg, Object obj) {
		info(msg, obj);
	}

	@Override
	public void info(Marker marker, String msg, Object obj1, Object obj2) {
		info(msg, obj1, obj2);
	}

	@Override
	public void info(Marker marker, String msg, Object...params) {
		info(msg, params);
	}

	@Override
	public void info(Marker marker, String msg, Throwable ex) {
		info(msg, ex);
	}

	@Override
	public boolean isWarnEnabled() {
		return julLogger.isLoggable(Level.WARNING);
	}

	@Override
	public void warn(String msg) {
		julLogger.log(Level.WARNING, msg);
	}

	@Override
	public void warn(String msg, Object obj) {
		julLogger.log(Level.WARNING, msg, obj);
	}

	@Override
	public void warn(String msg, Object obj1, Object obj2) {
		julLogger.log(Level.WARNING, msg, new Object[] {obj1, obj2});
	}
	
	@Override
	public void warn(String msg, Object...params) {
		julLogger.log(Level.WARNING, msg, params);
	}
	
	@Override
	public void warn(String msg, Throwable ex) {
		julLogger.log(Level.WARNING, msg, ex);
	}
	
	@Override
	public boolean isWarnEnabled(Marker marker) {
		return isWarnEnabled();
	}
	
	@Override
	public void warn(Marker marker, String msg) {
		warn(msg);
	}
	
	@Override
	public void warn(Marker marker, String msg, Object obj) {
		warn(msg, obj);
	}
	
	@Override
	public void warn(Marker marker, String msg, Object obj1, Object obj2) {
		warn(msg, obj1, obj2);
	}
	
	@Override
	public void warn(Marker marker, String msg, Object...params) {
		warn(msg, params);
	}
	
	@Override
	public void warn(Marker marker, String msg, Throwable ex) {
		warn(msg, ex);
	}
	
	@Override
	public boolean isErrorEnabled() {
		return julLogger.isLoggable(Level.SEVERE);
	}
	
	@Override
	public void error(String msg) {
		julLogger.log(Level.SEVERE, msg);
	}
	
	@Override
	public void error(String msg, Object obj) {
		julLogger.log(Level.SEVERE, msg, obj);
	}
	
	@Override
	public void error(String msg, Object obj1, Object obj2) {
		julLogger.log(Level.SEVERE, msg, new Object[] {obj1, obj2});
	}
	
	@Override
	public void error(String msg, Object...params) {
		julLogger.log(Level.SEVERE, msg, params);
	}
	
	@Override
	public void error(String msg, Throwable ex) {
		julLogger.log(Level.SEVERE, msg, ex);
	}
	
	@Override
	public boolean isErrorEnabled(Marker marker) {
		return isErrorEnabled();
	}
	
	@Override
	public void error(Marker marker, String msg) {
		error(msg);
	}
	
	@Override
	public void error(Marker marker, String msg, Object obj) {
		error(msg, obj);
	}
	
	@Override
	public void error(Marker marker, String msg, Object obj1, Object obj2) {
		error(msg, obj1, obj2);
	}
	
	@Override
	public void error(Marker marker, String msg, Object...params) {
		error(msg, params);
	}
	
	@Override
	public void error(Marker marker, String msg, Throwable ex) {
		error(msg, ex);
	}
	
}
