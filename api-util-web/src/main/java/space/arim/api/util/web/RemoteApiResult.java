/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
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

package space.arim.api.util.web;

import java.util.Objects;

/**
 * The result of a request to a remote API server or backend. Can succeed or fail in various ways. <br>
 * <br>
 * See {@link ResultType} for possible outcomes
 * 
 * @author A248
 *
 */
public final class RemoteApiResult<T> {

	private final T value;
	private final ResultType resultType;
	private final Exception exception;

	private RemoteApiResult(T value, ResultType resultType, Exception exception) {
		this.value = value;
		this.resultType = Objects.requireNonNull(resultType, "RemoteApiResult resultType must not be null");
		this.exception = exception;
	}

	/**
	 * Creates a successfully found result
	 *
	 * @param value the result value, cannot be null
	 * @param <T> the type of the result object
	 * @return the found result
	 */
	public static <T> RemoteApiResult<T> found(T value) {
		return new RemoteApiResult<>(Objects.requireNonNull(value, "value"), ResultType.FOUND, null);
	}

	/**
	 * Creates a successfully found result, where the type of the result is {@code Void}. <br>
	 * <br>
	 * This method is required to be separate from {@link #found(Object)} since it is not
	 * possible to obtain a non-null {@code Void} value.
	 *
	 * @return the found result
	 */
	public static RemoteApiResult<Void> foundVoid() {
		return new RemoteApiResult<>(null, ResultType.FOUND, null);
	}

	/**
	 * Creates a not found result
	 *
	 * @param <T> the type of the result object
	 * @return the not found result
	 */
	public static <T> RemoteApiResult<T> notFound() {
		return new RemoteApiResult<>(null, ResultType.NOT_FOUND, null);
	}

	/**
	 * Creates an error result
	 *
	 * @param exception the exception which caused the error
	 * @param <T> the type of the result object
	 * @return the result
	 */
	public static <T> RemoteApiResult<T> error(Exception exception) {
		return new RemoteApiResult<>(null, ResultType.ERROR, exception);
	}

	/**
	 * Creates a rate limited result
	 *
	 * @param <T> the type of the result object
	 * @return the result
	 */
	public static <T> RemoteApiResult<T> rateLimited() {
		return new RemoteApiResult<>(null, ResultType.RATE_LIMITED, null);
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
	 * @return the reason, never null
	 */
	public ResultType getResultType() {
		return resultType;
	}
	
	/**
	 * Get the exception, if applicable, else <code>null</code>. <br>
	 * Exceptions are most commonly associated with {@link ResultType#ERROR},
	 * but may be attached to other results.
	 * 
	 * @return the exception or null if no exception is associated with this result
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
		result = prime * result + resultType.hashCode();
		result = prime * result + Objects.hashCode(exception);
		result = prime * result + Objects.hashCode(value);
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
				&& Objects.equals(exception, other.exception)
				&& Objects.equals(value, other.value);
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
		ERROR

	}
	
}
