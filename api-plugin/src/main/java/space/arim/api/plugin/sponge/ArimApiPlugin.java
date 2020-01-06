/* 
 * ArimAPI-plugin
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-plugin. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.plugin.sponge;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.AsynchronousExecutor;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.SynchronousExecutor;

import com.google.inject.Inject;

import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.concurrent.AsyncExecutor;
import space.arim.api.concurrent.Synchroniser;
import space.arim.api.plugin.SimpleAsyncExecutor;

@Plugin(id = "arimapiplugin", name = "ArimAPIPlugin", version = "see_plugin_jar_resource=plugin.yml")
public class ArimApiPlugin {
	
	/*
	 * I am so glad there is very little implementation required here.
	 * The Sponge API is just absolutely horrible.
	 * 
	 * So much of its access is disgustingly static. There are no server instances for use.
	 * Worse, the extreme over-use of annotations for basic functionality is just appalling.
	 * As a result of the explosion of annotations, there are no plugin instances, either.
	 * Further, you cannot use simple maven resources filtering in a plugin.yml, because Sponge doesn't use a plugin.yml.
	 */
	
	@Inject
	public ArimApiPlugin(@AsynchronousExecutor SpongeExecutorService async, @SynchronousExecutor SpongeExecutorService sync) {
		UniversalRegistry.get().register(AsyncExecutor.class, new SimpleAsyncExecutor(async));
		UniversalRegistry.get().register(Synchroniser.class, new DefaultSynchroniser(sync));
	}
	
}
