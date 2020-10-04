/**
 * A common problem with some platform specific APIs is the odd way in which they provide
 * some sort of async execution service, but only run tasks at arbitrary time point. <br>
 * <br>
 * This happens, particularly, on the Bukkit and Sponge platforms, which both have a "main thread."
 * Each expose a scheduler whose asynchronous tasks are started every tick from the main thread. <br>
 * <br>
 * This is a huge potential for deadlock; any attempts to await completion of async tasks
 * from the main thread will fail unless the async tasks has already completed. <br>
 * <br>
 * However, executing work in parallel and returning to the main thread after doing so is a common
 * use case unfulfilled by this shortcoming. Therefore, this package provides the concept of
 * <i>real executors</i>, that is, an {@code Executor} which is not reliant on the main thread,
 * but rather runs tasks as would be expected: from the thread pool, as soon as a thread in the pool
 * is free. <br>
 * <br>
 * Using this package, one may take advantage of the common thread pools on all platforms without
 * having to write platform-specific code or creating an own thread pool.
 * 
 */
package space.arim.api.env.realexecutor;