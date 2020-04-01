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
package space.arim.api.platform.bungee;

import net.md_5.bungee.api.plugin.Plugin;

import space.arim.api.concurrent.SyncExecution;

/**
 * A default implementation of {@link SyncExecution} on the BungeeCord platform. Uses the server's inbuilt scheduling.
 * 
 * @author A248
 *
 */
public class DefaultSyncExecution extends DefaultExecution implements SyncExecution {

	/**
	 * Creates the instance.
	 * 
	 * @param plugin the plugin to use
	 */
	public DefaultSyncExecution(Plugin plugin) {
		super(plugin);
	}

}
