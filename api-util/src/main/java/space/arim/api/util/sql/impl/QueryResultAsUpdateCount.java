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
 * A QueryResult which represents an update count
 * 
 * @author A248
 *
 */
public class QueryResultAsUpdateCount implements QueryResult {

	private final int updateCount;
	
	/**
	 * Creates from an update count
	 * 
	 * @param updateCount the update count
	 */
	public QueryResultAsUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	@Override
	public boolean isResultSet() {
		return false;
	}

	@Override
	public ResultSet toResultSet() {
		throw new RuntimeException("QueryResult is not a ResultSet");
	}

	@Override
	public boolean isUpdateCount() {
		return true;
	}

	@Override
	public int toUpdateCount() {
		return updateCount;
	}

	@Override
	public void close() throws SQLException {
		
	}

	@Override
	public String toString() {
		return "QueryResultAsUpdateCount [updateCount=" + updateCount + "]";
	}
	
}
