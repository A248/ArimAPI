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

/**
 * Thrown to indicate a non-200 status code. This is a RuntimeException so that
 * it may be thrown from inside CompletableFutures.
 * 
 * @author A248
 *
 */
public class HttpNon200StatusCodeException extends RuntimeException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 2041055985968858366L;

	/**
	 * Creates an exception
	 * 
	 */
	public HttpNon200StatusCodeException() {
		
	}
	
	/**
	 * Creates the exception with the specified HTTP status code
	 * 
	 * @param code the status code
	 */
	public HttpNon200StatusCodeException(int code) {
		super("Status code " + code);
	}
	
	/**
	 * Creates the exception with the specified cause
	 * 
	 * @param cause the cause
	 */
	public HttpNon200StatusCodeException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Crates the exception with the specified HTTP status code and cause
	 * 
	 * @param code the status code
	 * @param cause the cause
	 */
	public HttpNon200StatusCodeException(int code, Throwable cause) {
		super("Status code " + code, cause);
	}
	
}
