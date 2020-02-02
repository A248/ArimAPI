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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class DecoupledCommand implements CommandCallable {
	
	/**
	 * Handles the command.
	 * 
	 * @param sender the command sender (player, console, etc.)
	 * @param args the arguments
	 */
	protected abstract boolean execute(CommandSource sender, String[] args);
	
	/**
	 * Gets tab completion possibilities
	 * 
	 * @param sender the command sender (player, console, etc.)
	 * @param args the arguments
	 * @return a list of tab completions
	 */
	protected List<String> getTabComplete(CommandSource sender, String[] args) {
		return Collections.emptyList();
	}
	
	/**
	 * Use {@link #execute(CommandSource, String[])} instead.
	 * 
	 */
	@Override
	public final CommandResult process(CommandSource sender, String arguments) {
		return execute(sender, arguments.split(" ")) ? CommandResult.success() : CommandResult.empty();
	}
	
	/**
	 * Use {@link #getTabComplete(CommandSource, String[])} instead.
	 * 
	 */
	@Override
	public final List<String> getSuggestions(CommandSource sender, String arguments, Location<World> targetPosition) throws CommandException {
		return getTabComplete(sender, arguments.split(" "));
	}
	
	@Override
	public boolean testPermission(CommandSource sender) {
		return true;
	}
	
	@Override
	public Optional<Text> getShortDescription(CommandSource sender) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Text> getHelp(CommandSource sender) {
		return Optional.empty();
	}
	
	@Override
	public Text getUsage(CommandSource sender) {
		return Text.of();
	}

}
