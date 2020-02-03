/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.server.sponge;

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import space.arim.api.concurrent.AsyncExecution;
import space.arim.api.concurrent.Shutdownable;

/**
 * A default implementation of {@link AsyncExecution} on the Sponge platform. Uses the server's inbuilt scheduling.
 * 
 * @author A248
 *
 */
public class DefaultAsyncExecution extends DefaultExecution implements AsyncExecution, Shutdownable {

	/**
	 * Creates the instance. See {@link SpongeRegistrable#SpongeRegistrable(PluginContainer)} for more information.
	 * 
	 * @param plugin the plugin to use for Registrable information
	 * @param async the <code>SpongeExecutorService</code> to use for execution. <b>MUST be an ASYNC executor!</b>
	 */
	public DefaultAsyncExecution(PluginContainer plugin, SpongeExecutorService async) {
		super(plugin,async);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
	}
	
	@Override
	public void shutdownAndWait() {
		super.shutdownAndWait();
	}
	
}
