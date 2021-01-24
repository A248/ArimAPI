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

import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;

/**
 * Standard implementation of {@link ConfigReadResult}
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public class SimpleConfigReadResult extends AbstractConfigResult implements ConfigReadResult {

	private final ConfigData data;
	
	public SimpleConfigReadResult(ResultType resultType, Exception exception, ConfigData data) {
		super(resultType, exception);
		this.data = data;
	}
	
	@Override
	public ResultType getResultDefinition() {
		return (ResultType) super.getResultDefinition();
	}

	@Override
	public ConfigData getReadData() {
		return data;
	}

}
