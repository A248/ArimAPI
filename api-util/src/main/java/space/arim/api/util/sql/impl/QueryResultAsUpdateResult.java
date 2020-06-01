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
import space.arim.api.util.sql.UpdateResult;

public abstract class QueryResultAsUpdateResult implements QueryResult, UpdateResult {

	private final int updateCount;
	
	public QueryResultAsUpdateResult(int updateCount) {
		this.updateCount = updateCount;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return updateCount;
	}

	@Override
	public abstract ResultSet getGeneratedKeys() throws SQLException;

	@Override
	public boolean isResultSet() throws SQLException {
		return false;
	}

	@Override
	public ResultSet toResultSet() throws SQLException {
		throw new IllegalStateException("QueryResult is not a ResultSet");
	}

	@Override
	public boolean isUpdateResult() throws SQLException {
		return true;
	}

	@Override
	public UpdateResult toUpdateResult() throws SQLException {
		return this;
	}
	
	@Override
	public abstract void close() throws SQLException;

}
