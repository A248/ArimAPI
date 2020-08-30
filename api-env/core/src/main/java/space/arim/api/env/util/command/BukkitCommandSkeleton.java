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
package space.arim.api.env.util.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Command skeleton for Bukkit to be registered directly in the server's command map. <br>
 * <br>
 * The methods intended to be implemented by subclasses are {@link #execute(CommandSender, String[])} for execution
 * and {@link #suggest(CommandSender, String[])} for tab completion. <br>
 * <br>
 * <b>Mutability</b> <br>
 * Bukkit's {@code Command} is primarily designed to be mutable. However, certain steps can be taken to work around this. <br>
 * <br>
 * Firstly, in this subclass, the description, permission, permission message, and usage message are prevented from being changed
 * from the default, which is a blank description, no permission, no permission message, and the name of the command. It is
 * intended that all such handling is conducted by the {@link #execute(CommandSender, String[])} method. <br>
 * <br>
 * Secondly, Bukkit it does not allow changing many details of the command once it has been registered. To reduce mutability
 * to the fullest extent while complying with method contracts, the user may register this command in a command map via
 * {@code CommandMap#register(String, String, Command)} or another appropriate {@code CommandMap} method to have the server
 * recognise the command. Ideally this is done following complete construction.
 * 
 * @author A248
 *
 */
public abstract class BukkitCommandSkeleton extends Command {
	
	/**
	 * Creates from a plugin, command name, and aliases
	 * 
	 * @param command the command name
	 * @param aliases all aliases, may be empty
	 */
	protected BukkitCommandSkeleton(String command, String...aliases) {
		super(command, "", '/' + command, List.of(aliases));
	}

	/**
	 * Executes the command. {@code sender} and {@code args} correspond to those in {@code #execute(CommandSender, String, String[])}
	 * 
	 * @param sender the command sender
	 * @param args the arguments
	 */
	protected abstract void execute(CommandSender sender, String[] args);
	
	/**
	 * Gets tab complete suggestions. {@code sender} and {@code args} correspond to those in
	 * {@code #tabComplete(CommandSender, String, String[])}
	 * 
	 * @param sender the command sender
	 * @param args the arguments
	 * @return tab completion suggestions
	 */
	protected abstract List<String> suggest(CommandSender sender, String[] args);
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		execute(sender, args);
		return true;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return suggest(sender, args);
	}
	
	@Override
	public String getPermission() {
		return null;
	}
	
	@Override
	public void setPermission(String permission) {
		
	}
	
	@Override
	public boolean testPermissionSilent(CommandSender target) {
		return true;
	}
	
	@Override
	public String getPermissionMessage() {
		return null;
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public String getUsage() {
		return '/' + getName();
	}
	
	@Override
	public Command setDescription(String description) {
		return this;
	}
	
	@Override
	public Command setPermissionMessage(String permissionMessage) {
		return this;
	}

	@Override
	public Command setUsage(String usage) {
		return this;
	}
	
}
