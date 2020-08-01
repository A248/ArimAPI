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

import space.arim.api.configure.ConfigResult;

abstract class AbstractConfigResult implements ConfigResult {

	private final ResultDefinition resultDefinition;
	private final Exception exception;
	
	AbstractConfigResult(ResultDefinition resultDefinition, Exception exception) {
		this.resultDefinition = resultDefinition;
		this.exception = exception;
	}
	
	@Override
	public ResultDefinition getResultDefinition() {
		return resultDefinition;
	}

	@Override
	public Exception getException() {
		return exception;
	}

}
