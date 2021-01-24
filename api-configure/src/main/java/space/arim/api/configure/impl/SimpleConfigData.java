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
package space.arim.api.configure.impl;

import java.util.List;
import java.util.Map;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;

/**
 * Common sense implementation of {@link ConfigData} using 2 backing maps, one of config values,
 * the other of comments.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public class SimpleConfigData extends BaseConfigData {

	/**
	 * Creates from a map of values and a map of comments
	 * 
	 * @param values the map of values, see {@link ConfigData#getValuesMap()} for requirements
	 * @param comments the map of comments, see {@link ConfigData#getCommentsMap()} for requirements
	 * @throws NullPointerException if either parameter is null
	 */
	public SimpleConfigData(Map<String, Object> values, Map<String, List<ConfigComment>> comments) {
		super(values, comments);
	}

}
