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
 * The result of reading configuration values from a path on the filesystem. Such reading of config values may
 * fail or succeed.
 * 
 * @author A248
 *
 */
public interface ConfigReadResult extends ConfigResult {

	/**
	 * Gets the configuration data which was read, or {@code null} if unsuccessful
	 * 
	 * @return the config data or null if unsuccessful
	 */
	ConfigData getReadData();
	
	/**
	 * Gets the result type of this result
	 * 
	 * @return the result type, never {@code null}
	 */
	@Override
	ResultType getResultDefinition();
	
	/**
	 * The ways in which reading config values may succeed or fail
	 * 
	 * @author A248
	 *
	 */
	public enum ResultType implements ResultDefinition {
		
		/**
		 * The values were loaded successfully, without error
		 * 
		 */
		SUCCESS(true),
		/**
		 * The file could not be read from the filesystem and it is specifically known
		 * that this is because the file does not exist. <br>
		 * <br>
		 * {@link #FAILURE_READING} may also be the result type if the file did not exist,
		 * but it is not known that that was the specific cause.
		 * 
		 */
		FILE_NON_EXISTENT(false),
		/**
		 * There was a failure reading the file from the filesystem, e.g. some kind of
		 * {@code IOException}. It may be the case that the file did not exist or did
		 * not have the proper permissions.
		 * 
		 */
		FAILURE_READING(false),
		/**
		 * The config's raw data was read, but it could not be parsed to valid syntax.
		 * 
		 */
		FAILURE_PARSING(false),
		/**
		 * Something unexpected occurred
		 * 
		 */
		UNKNOWN_ERROR(false);
		
		private final boolean success;
		
		private ResultType(boolean success) {
			this.success = success;
		}
		
		@Override
		public boolean isSuccess() {
			return success;
		}
	}
	
}
