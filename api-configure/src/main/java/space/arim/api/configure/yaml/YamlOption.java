/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure.yaml;

/**
 * All options for the {@link YamlConfigSerialiser}'s specifics.
 * 
 * @author A248
 *
 */
public final class YamlOption {

	private static final YamlOption COMPACT_LISTS = new YamlOption();
	
	private YamlOption() {}
	
	/**
	 * Intructs the serialiser to write lists and arrays in a compact fashion. Namely,
	 * they will be written as "[e1, e2, e3]" instead of an expanded multiline list.
	 * 
	 * @return yaml option for compact lists
	 */
	public static YamlOption compactLists() {
		return COMPACT_LISTS;
	}
	
}
