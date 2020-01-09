/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.concurrent;

import java.util.concurrent.ExecutorService;
import space.arim.universal.registry.Registrable;

/**
 * A service designed for multithreading via asynchronous concurrent execution. <br>
 * <br>
 * * AsyncExecutor is a {@link Registrable}, which specifies {@link #getPriority()} <br>
 * * AsyncExecutor is an {@link BasicExecutor}, which specifies {@link #execute(Runnable)} <br>
 * <br>
 * AsyncExecutor differs from {@link ExecutorService} in a few important ways. First, it is a Registrable.
 * Second, AsyncExecutor is stripped to mere execution; it is not necessarily a thread pool.
 * AsyncExecutor does not specify any of ExecutorService's methods relating to
 * shutting down, termination, or invoking other callers' submissionse.
 * In fact, the only required method related to execution of tasks is {@link #execute(Runnable)}.
 * 
 * @author A248
 *
 */
public interface AsyncExecutor extends Registrable, BasicExecutor {

}
