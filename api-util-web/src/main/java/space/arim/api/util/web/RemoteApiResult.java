/* 
 * ArimAPI-util-web
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util-web. If not, see <https://www.gnu.org/licenses/>
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
public final class RemoteApiResult<T> {
	
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
		this.resultType = Objects.requireNonNull(resultType, "RemoteApiResult resultType must not be null");
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
	
	@Override
	public String toString() {
		return "RemoteApiResult [value=" + value + ", resultType=" + resultType + ", exception=" + exception + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exception == null) ? 0 : exception.hashCode());
		result = prime * result + resultType.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof RemoteApiResult<?>)) {
			return false;
		}
		RemoteApiResult<?> other = (RemoteApiResult<?>) object;
		return (resultType == other.resultType)
				&& ((exception == null) ? other.exception == null : exception.equals(other.exception))
				&& ((value == null) ? other.value == null : value.equals(other.value));
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
