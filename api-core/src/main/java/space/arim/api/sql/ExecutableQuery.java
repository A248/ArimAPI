/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
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
package space.arim.api.sql;

import space.arim.universal.util.collections.CollectionsUtil;

import space.arim.api.util.StringsUtil;

/**
 * Immutable, thread-safe representation of an SQL query with unparameterised statement and parameters passed to the constructor <br>
 * <br>
 * {@link java.sql.PreparedStatement} objects should be based on {@link #statement()} with parameters {@link #parameters()}
 * attached to the PreparedStatement using {@link java.sql.PreparedStatement#setObject(int, Object) PreparedStatement#setObject(int, Object)}
 * 
 * @author A248
 *
 */
public class ExecutableQuery {

	private final String statement;
	private final Object[] parameters;
	
	public ExecutableQuery(String statement, Object...parameters) {
		this.statement = statement;
		this.parameters = parameters;
	}
	
	public String statement() {
		return statement;
	}
	
	public Object[] parameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		return "{[" + statement + "] with parameters [" + StringsUtil.concat(CollectionsUtil.convertAll(parameters, (param) -> param.toString()), ',') + "]}";
	}
	
}
