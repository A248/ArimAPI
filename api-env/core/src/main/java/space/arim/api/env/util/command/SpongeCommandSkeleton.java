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
import java.util.Optional;

import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * A decoupled {@code CommandCallable} skeleton for Sponge. It is intended that only {@link #execute(CommandSource, String)}
 * and {@link #suggest(CommandSource, String)} are implemented, but other methods may be overridden if desired.
 * 
 * @author A248
 *
 */
public interface SpongeCommandSkeleton extends CommandCallable {

	/**
	 * Executes this command. {@code source} and {@code args} correspond to those in {@code #process(CommandSource, String)}
	 * 
	 * @param source the command source
	 * @param args the raw arguments
	 */
	void execute(CommandSource source, String args);
	
	/**
	 * Gets tab complete suggestions. {@code source} and {@code args} correspond to those in
	 * {@code #getSuggestions(CommandSource, String, Location)}.
	 * 
	 * @param source the command source
	 * @param args the raw arguments
	 * @return tab completion suggestions
	 */
	List<String> suggest(CommandSource source, String args);
	
	@Override
	default CommandResult process(CommandSource source, String arguments) throws CommandException {
		execute(source, arguments);
		return CommandResult.success();
	}

	@Override
	default List<String> getSuggestions(CommandSource source, String arguments, Location<World> targetPosition)
			throws CommandException {
		return suggest(source, arguments);
	}

	@Override
	default boolean testPermission(CommandSource source) {
		return true;
	}

	@Override
	default Optional<Text> getShortDescription(CommandSource source) {
		return Optional.empty();
	}

	@Override
	default Optional<Text> getHelp(CommandSource source) {
		return Optional.empty();
	}

	@Override
	default Text getUsage(CommandSource source) {
		return Text.EMPTY;
	}

}
