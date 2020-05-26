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
package space.arim.api.util.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.api.util.sql.QueryResult;

/**
 * A {@link QueryResult} which is neither a ResultSet nor an update count. <br>
 * <br>
 * Calling {@link #close()} close on this class alone does nothing. Rather,
 * it is intended that subclasses override the close method and close any of their attached objects.
 * 
 * @author A248
 *
 */
public class QueryResultAsNeither implements QueryResult {

	@Override
	public boolean isResultSet() {
		return false;
	}

	@Override
	public ResultSet toResultSet() {
		throw new IllegalStateException("QueryResult is neither a ResultSet nor update count");
	}

	@Override
	public boolean isUpdateCount() {
		return false;
	}

	@Override
	public int toUpdateCount() {
		throw new IllegalStateException("QueryResult is neither a ResultSet nor update count");
	}

	/**
	 * Does nothing. See class javadoc
	 * 
	 */
	@Override
	public void close() throws SQLException {
		
	}

	@Override
	public String toString() {
		return "QueryResultAsNeither []";
	}

}
