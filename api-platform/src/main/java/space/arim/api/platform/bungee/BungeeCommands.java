/* 
 * ArimAPI-platform
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-platform is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-platform is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-platform. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.platform.bungee;

import java.util.List;
import java.util.stream.Stream;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import space.arim.api.platform.AbstractPlatformCommands;
import space.arim.api.util.LazySingleton;

/**
 * BungeeCord commands utility. Use {@link #get()} to get the instance.
 * 
 * @author A248
 *
 */
public class BungeeCommands extends AbstractPlatformCommands<CommandSender, ProxiedPlayer> {

	private static final LazySingleton<BungeeCommands> INST = new LazySingleton<BungeeCommands>(BungeeCommands::new);
	
	protected BungeeCommands() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static BungeeCommands get() {
		return INST.get();
	}
	
	@Override
	public List<String> getPlayerNamesTabComplete(Stream<ProxiedPlayer> players, String[] args) {
		return getMatchingTabComplete(players.map((player) -> player.getName()), args);
	}
	
	@Override
	public List<String> getCommandSendersTabComplete(Stream<CommandSender> senders, String consoleRepresentation, String[] args) {
		return getMatchingTabComplete(senders.map((sender) -> (sender instanceof ProxiedPlayer) ? ((ProxiedPlayer) sender).getName() : consoleRepresentation), args);
	}
	
}
