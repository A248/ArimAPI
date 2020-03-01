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
package space.arim.api.platform;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Adds simple tab completions. Matches given command arguments against streams of <code>String</code>s, players, or command senders. <br>
 * The last argument is used for matching and all others are ignored. If the argument array is empty or <code>null</code>,
 * an empty string is used, meaning the result will be the elements of the source stream. <br>
 * <br>
 * There is no guarantee that returned lists are mutable. Thus, sorting any returned lists with {@link List#sort(Comparator)} may or may not work;
 * it is unsupported. Therefore it is recommended to explicitly use the methods designed for sorting.
 * Each method returning an unsorted list has a sorted variant with an optionally specified <code>Comparator</code>
 * 
 * @author A248
 *
 * @param <T> the type of the platform specific command sender object
 * @param <R> the type of the platform specific player object
 */
public interface PlatformTabCompletion<T, R> {

	/**
	 * Gets tab completions based on player names matching the command arguments. <br>
	 * <br>
	 * <b>The returned list is unsorted by default.</b> <br>
	 * For sorting, use: <br>
	 * * {@link #getPlayerNamesTabCompleteSorted(Stream, String[])} for natural ordering <br>
	 * * {@link #getPlayerNamesTabCompleteSorted(Stream, String[], Comparator)} to specify a <code>Comparator</code>
	 * 
	 * @param players a stream of players
	 * @param args the command arguments
	 * @return an unsorted list of matching names
	 */
	List<String> getPlayerNamesTabComplete(Stream<R> players, String[] args);
	
	/**
	 * Sorted variant of {@link #getPlayerNamesTabComplete(Stream, String[])}
	 * 
	 * @param players a stream of players
	 * @param args the command arguments
	 * @param comparator the comparator used for sorting
	 * @return a sorted list of matching names
	 */
	default List<String> getPlayerNamesTabCompleteSorted(Stream<R> players, String[] args, Comparator<String> comparator) {
		List<String> sorted = getPlayerNamesTabComplete(players, args);
		sorted.sort(comparator);
		return sorted;
	}
	
	/**
	 * Sorted variant of {@link #getPlayerNamesTabComplete(Stream, String[])} using natural ordering.
	 * 
	 * @param players a stream of players
	 * @param args the command arguments
	 * @return a sorted list of matching names
	 */
	default List<String> getPlayerNamesTabCompleteSorted(Stream<R> players, String[] args) {
		return getPlayerNamesTabCompleteSorted(players, args, Comparator.naturalOrder());
	}
	
	/**
	 * Gets tab completions based on command senders matching the command argument. <br>
	 * This is similar to {@link #getPlayerNamesTabComplete(Stream, String[])}, however, it includes the console
	 * as a possible choice. The representation of the console as a string is passed as a parameter. <br>
	 * When matching against the stream of command senders, each command sender is evaluated to a <code>String</code>;
	 * if the command sender is a player, the player name is used. Otherwise, the console representation parameter is used.
	 * 
	 * @param senders a stream of command senders
	 * @param consoleRepresentation the string based representation of nonplayer command senders
	 * @param args the command arguments
	 * @return an unsorted list of matching names
	 */
	List<String> getCommandSendersTabComplete(Stream<T> senders, String consoleRepresentation, String[] args);
	
	/**
	 * Sorted variant of {@link #getCommandSendersTabComplete(Stream, String, String[])}
	 * 
	 * @param senders a stream of command senders
	 * @param consoleRepresentation the string based representation of nonplayer command senders
	 * @param args the command arguments
	 * @param comparator the comparator used for sorting
	 * @return a sorted list of matching names
	 */
	default List<String> getCommandSendersTabCompleteSorted(Stream<T> senders, String consoleRepresentation, String[] args, Comparator<String> comparator) {
		List<String> sorted = getCommandSendersTabComplete(senders, consoleRepresentation, args);
		sorted.sort(comparator);
		return sorted;
	}
	
	/**
	 * Sorted variant of {@link #getCommandSendersTabComplete(Stream, String, String[])} using natural ordering
	 * 
	 * @param senders a stream of command senders
	 * @param consoleRepresentation the string based representation of nonplayer command senders
	 * @param args the command arguments
	 * @return a sorted list of matching names
	 */
	default List<String> getCommandSendersTabCompleteSorted(Stream<T> senders, String consoleRepresentation, String[] args) {
		return getCommandSendersTabCompleteSorted(senders, consoleRepresentation, args, Comparator.naturalOrder());
	}
	
	/**
	 * Gets tab completions matching the command arguments. <br>
	 * The returned list may be sorted with {@link List#sort(Comparator)}.
	 * 
	 * @param matching a stream of strings to match against
	 * @param args the command arguments
	 * @return an unsorted list of matching names
	 */
	default List<String> getMatchingTabComplete(Stream<String> matching, String[] args) {
		String startArg = (args.length > 0) ? args[args.length - 1] : "";
		return matching.filter((name) -> name.toLowerCase().startsWith(startArg)).sorted().collect(Collectors.toList());
	}
	
	/**
	 * Sorted variant of {@link #getMatchingTabComplete(Stream, String[])}
	 * 
	 * @param matching a stream of strings to match against
	 * @param args the command arguments
	 * @param comparator the comparator used for sorting
	 * @return a sorted list of matching names
	 */
	default List<String> getMatchingTabCompleteSorted(Stream<String> matching, String[] args, Comparator<String> comparator) {
		List<String> sorted = getMatchingTabComplete(matching, args);
		sorted.sort(comparator);
		return sorted;
	}
	
	/**
	 * Sorted variant of {@link #getMatchingTabComplete(Stream, String[])} using natural ordering
	 * 
	 * @param matching a stream of strings to match against
	 * @param args the command arguments
	 * @param comparator the comparator used for sorting
	 * @return a sorted list of matching names
	 */
	default List<String> getMatchingTabCompleteSorted(Stream<String> matching, String[] args) {
		return getMatchingTabCompleteSorted(matching, args, Comparator.naturalOrder());
	}
	
}
