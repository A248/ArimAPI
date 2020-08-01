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
 * The result of copying default configuration values from a resource to a path on the filesystem. Such copying may
 * fail or succeed.
 * 
 * @author A248
 *
 */
public interface ConfigCopyResult extends ConfigResult {
	
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
		 * The default config was copied successfully to the filesystem, without error.
		 * 
		 */
		SUCCESS(true),
		/**
		 * The data does not need to be copied because the target file already exists. <br>
		 * <br>
		 * (This indicates a success even though it is not {@code SUCCESS}, {@link #isSuccess()}
		 * will return {@code true})
		 * 
		 */
		ALREADY_EXISTS(true),
		/**
		 * There was a failure copying the file to the filesystem, e.g. some kind of
		 * {@code IOException}
		 * 
		 */
		FAILURE_COPYING(false),
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
