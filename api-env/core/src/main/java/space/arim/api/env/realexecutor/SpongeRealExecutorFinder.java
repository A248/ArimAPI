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

import space.arim.api.env.SpongePlatformHandle;

import org.spongepowered.api.plugin.PluginContainer;

/**
 * The real executor finder for Sponge servers. Relies on implementations in SpongeCommon to retrieve the common
 * thread pool. <br>
 * <br>
 * Like Paper servers with the async scheduler patch, Sponge servers have the real executor declared in a field
 * named "executor", which is itself in an object in the field "asyncScheduler" of the server's scheduler.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link SpongePlatformHandle}
 */
@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
public class SpongeRealExecutorFinder implements RealExecutorFinder {

	/**
	 * Creates from a plugin to use
	 * 
	 * @param plugin the plugin
	 * @throws UnsupportedOperationException always, see deprecation
	 */
	public SpongeRealExecutorFinder(PluginContainer plugin) {
		throw SpongePlatformHandle.uoe();
	}

	@Override
	public Executor findExecutor(Consumer<Exception> exceptionHandler) {
		throw SpongePlatformHandle.uoe();
	}
	
}
