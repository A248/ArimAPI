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
package space.arim.api.plugin.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.universal.registry.UniversalRegistry;

import space.arim.api.concurrent.AsyncExecutor;
import space.arim.api.concurrent.Synchroniser;

public class ArimApiPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		UniversalRegistry.get().register(AsyncExecutor.class, new DefaultAsyncExecutor(this));
		UniversalRegistry.get().register(Synchroniser.class, new DefaultSynchroniser(this));
	}
	
}
