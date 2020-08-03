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

import space.arim.api.configure.ConfigCopyResult;

/**
 * Standard implementation of {@link ConfigCopyResult}
 * 
 * @author A248
 *
 */
public class SimpleConfigCopyResult extends AbstractConfigResult implements ConfigCopyResult {

	public SimpleConfigCopyResult(ResultType resultType, Exception exception) {
		super(resultType, exception);
	}
	
	@Override
	public ResultType getResultDefinition() {
		return (ResultType) super.getResultDefinition();
	}

}