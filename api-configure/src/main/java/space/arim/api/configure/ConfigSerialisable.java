/* 
 * ArimAPI-util
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

/**
 * An interface allowing {@link ConfigData#getValuesMap()} to use a different representation
 * of a type as a config string. <br>
 * <br>
 * For example, supposing an enum is stored in a config map, its {@code toString()} method may
 * be overridden. To ensure the enum is serialised as its type name, it may implement this interface.
 * 
 * @author A248
 *
 */
public interface ConfigSerialisable {

	/**
	 * Converts this object to its equivalent representation as a config string
	 * 
	 * @return the config string representation
	 */
	CharSequence toConfigString();
	
}
