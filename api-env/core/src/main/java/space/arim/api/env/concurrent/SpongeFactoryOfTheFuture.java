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
package space.arim.api.env.concurrent;

import space.arim.omnibus.util.concurrent.CentralisedFuture;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
import space.arim.omnibus.util.concurrent.impl.AbstractFactoryOfTheFuture;

import space.arim.api.env.SpongePlatformHandle;

import org.spongepowered.api.plugin.PluginContainer;

/**
 * An implementation of {@link FactoryOfTheFuture} for the Sponge platform, using a specified
 * plugin to execute synchronous tasks. <br>
 * <br>
 * When no longer needed, {@link #close()} should be called to cleanup.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link SpongePlatformHandle}
 */
@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
public class SpongeFactoryOfTheFuture extends AbstractFactoryOfTheFuture implements AutoCloseable {
	
	/**
	 * Creates from a {@code PluginContainer} to use for synchronous execution
	 * 
	 * @param plugin the plugin to use
	 * @throws UnsupportedOperationException always, see deprecation
	 */
	public SpongeFactoryOfTheFuture(PluginContainer plugin) {
		throw SpongePlatformHandle.uoe();
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public void executeSync(Runnable command) {
		throw SpongePlatformHandle.uoe();
	}

	@Override
	public <U> CentralisedFuture<U> newIncompleteFuture() {
		throw SpongePlatformHandle.uoe();
	}

}
