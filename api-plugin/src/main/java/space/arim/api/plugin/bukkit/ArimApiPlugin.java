/* 
 * ArimAPI-plugin
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.plugin.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.concurrent.AsyncExecution;
import space.arim.api.concurrent.SyncExecution;
import space.arim.api.server.TPSMeter;
import space.arim.api.util.CallerFinder;
import space.arim.api.util.CallerFinderProvider;
import space.arim.api.uuid.UUIDResolver;

public class ArimApiPlugin extends JavaPlugin {
	
	@Override
	public void onLoad() {
		UniversalRegistry.get().register(CallerFinder.class, new CallerFinderProvider());
		UniversalRegistry.get().register(AsyncExecution.class, new DefaultAsyncExecution(this));
		UniversalRegistry.get().register(SyncExecution.class, new DefaultSyncExecution(this));
		UniversalRegistry.get().register(UUIDResolver.class, new DefaultUUIDResolver(this));
		UniversalRegistry.get().register(TPSMeter.class, new DefaultTPSMeter(this));
	}
	
}
