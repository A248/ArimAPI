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
package space.arim.api.platform.spigot;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import space.arim.api.platform.AbstractPlatformCommands;
import space.arim.api.util.LazySingleton;

/**
 * Spigot commands utility. Use {@link #get()} to get the instance.
 * 
 * @author A248
 *
 */
public class SpigotCommands extends AbstractPlatformCommands<CommandSender, Player> {

	private static final LazySingleton<SpigotCommands> INST = new LazySingleton<SpigotCommands>(SpigotCommands::new);
	
	protected SpigotCommands() {}
	
	/**
	 * Gets the main instance
	 * 
	 * @return the instance
	 */
	public static SpigotCommands get() {
		return INST.get();
	}
	
	@Override
	public List<String> getPlayerNamesTabComplete(Stream<? extends Player> players, String[] args) {
		return getMatchingTabComplete(players.map((player) -> player.getName()), args);
	}
	
	@Override
	public List<String> getCommandSendersTabComplete(Stream<CommandSender> senders, String consoleRepresentation, String[] args) {
		return getMatchingTabComplete(senders.map((sender) -> (sender instanceof Player) ? ((Player) sender).getName() : consoleRepresentation), args);
	}
	
}
