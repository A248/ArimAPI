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
package space.arim.api.util.web;

import java.util.Objects;

/**
 * The result of a request to a remote API server or backend.
 * 
 * @author A248
 *
 */
public class RemoteApiResult<T> {
	
	private final T value;
	private final ResultType resultType;
	private final Exception exception;
	
	/**
	 * Creates a result from a value, reason, and exception
	 * 
	 * @param value the value, or null for none
	 * @param resultType the reason, must not be null
	 * @param exception the exception, or null for none
	 */
	public RemoteApiResult(T value, ResultType resultType, Exception exception) {
		this.value = value;
		this.resultType = Objects.requireNonNull(resultType, "RemoteApiResult reason must not be null");
		this.exception = exception;
	}
	
	/**
	 * Get the value of the result
	 * 
	 * @return the value
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Get the result type
	 * 
	 * @return the reason, never <code>null</code>
	 */
	public ResultType getResultType() {
		return resultType;
	}
	
	/**
	 * Get the exception, if applicable, else <code>null</code>. <br>
	 * Exceptions are most commonly associated with {@link ResultType#ERROR},
	 * but may be attached to other results.
	 * 
	 * @return the exception or <code>null</code> if no exception is associated with this result
	 */
	public Exception getException() {
		return exception;
	}
	
	/**
	 * Indicates the kind of the result
	 * 
	 * @author A248
	 *
	 */
	public enum ResultType {
		
		/**
		 * Indicates the result was found
		 * 
		 */
		FOUND,
		/**
		 * Indicates the request was successful but no information was found
		 * 
		 */
		NOT_FOUND,
		/**
		 * Indicates the requestee has incurred rate limiting
		 * 
		 */
		RATE_LIMITED,
		/**
		 * Indicates an unexpected exception occurred
		 * 
		 */
		ERROR,
		/**
		 * Indicates an unknown problem
		 * 
		 */
		UNKNOWN;
		
	}
	
}
