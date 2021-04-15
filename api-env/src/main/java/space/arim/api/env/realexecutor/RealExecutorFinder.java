/* 
 * ArimAPI-env-core
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-env-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-env-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-env-core. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.env.realexecutor;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * A finder of a platform's true common thread pool, as a proper {@link Executor}. <br>
 * <br>
 * The only requirement regarding the {@code Executor} is that it execute tasks idiomatically:
 * from the thread pool, as soon as a thread in the pool is free. <br>
 * <br>
 * Note that, because on some platforms there is no API method to get a real executor,
 * implementations may resort to unstable reflection assuming specific internals. It is
 * advised to use one's own thread pool should the operation to find the platform's true
 * executor fail.
 * 
 * @author A248
 *
 * @deprecated See {@link space.arim.api.env.realexecutor}
 */
@Deprecated
public interface RealExecutorFinder {

	/**
	 * Attempts to retrieve this platform's true common thread pool. <br>
	 * <br>
	 * Any issues, which may be due to reflection or other checked exceptions, are passed to the
	 * exception handler. Use {@code null} to ignore all exceptions.
	 * 
	 * @param exceptionHandler an exception handler, which may be used to print exceptions, or {@code null} for none
	 * @return the executor if found, else {@code null} if the attempt was unsuccessful
	 */
	Executor findExecutor(Consumer<Exception> exceptionHandler);
	
	/**
	 * Attempts to retrieve this platform's true common thread pool. Exceptions will be ignored.
	 * 
	 * @return the executor if found, else {@code null} if the attempt was unsuccessful
	 */
	default Executor findExecutor() {
		return findExecutor(null);
	}
	
}
