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
package space.arim.api.util.config;

/**
 * Thrown when configured values could not be loaded from the filesystem,
 * specifically because the file could not be read.
 * 
 * @author A248
 *
 * @deprecated See {@link space.arim.api.util.config} (this entire framework is deprecated for removal)
 */
@Deprecated(forRemoval = true)
public class ConfigReadValuesFromFileException extends ConfigLoadValuesFromFileException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 6136704160708545879L;

	/**
	 * Creates an exception with the given cause
	 * 
	 * @param cause the cause
	 */
	public ConfigReadValuesFromFileException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Creates an exception with the given message and cause
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public ConfigReadValuesFromFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Creates an exception with the given message
	 * 
	 * @param message the message
	 */
	public ConfigReadValuesFromFileException(String message) {
		super(message);
	}
	
}
