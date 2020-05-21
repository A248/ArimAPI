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
package space.arim.api.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Previously a helper for logging SQL queries
 * 
 * @deprecated Relies on a singular {@link #log(String)} method which may unnecessarily
 * concatenate Strings. Consider wrapping a subclass of {@link AbstractSql}
 * and logging manually, or switch to the improved SQL framework in {@link space.arim.api.util.sql}
 */
@Deprecated
public abstract class LoggingSql extends AbstractSql {

	protected abstract void log(String message);
	
	@Override
	public void executionQueries(ExecutableQuery...queries) throws SQLException {
		log("Executing queries " + Arrays.toString(queries));
		super.executionQueries(queries);
	}
	
	@Override
	public void executionQuery(String query, Object...params) throws SQLException {
		log("Executing query " + query + " with parameters " + Arrays.toString(params));
		super.executionQuery(query, params);
	}
	
	@Override
	public ResultSet[] selectionQueries(ExecutableQuery...queries) throws SQLException {
		log("Executing selection queries " + Arrays.toString(queries));
		return super.selectionQueries(queries);
	}
	
	@Override
	public ResultSet selectionQuery(String query, Object...params) throws SQLException {
		log("Executing selection query " + query + " with parameters " + Arrays.toString(params));
		return super.selectionQuery(query, params);
	}
	
}
