/* 
 * ArimAPI-util-sql
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-util-sql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-util-sql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-util-sql. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.util.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.api.util.sql.QueryResult;
import space.arim.api.util.sql.UpdateResult;

/**
 * A {@link QueryResult} representing a ResultSet. In the case that programmers
 * call {@link #close()} on {@link #toResultSet()} rather than on this itself,
 * this class implements ResultSet and delegates instead.
 * 
 * @author A248
 *
 */
public class QueryResultAsResultSet extends ResultSetProxy implements QueryResult {

	/**
	 * Creates from a backing ResultSet
	 * 
	 * @param resultSet the result set
	 */
	public QueryResultAsResultSet(ResultSet resultSet) {
		super(resultSet);
	}

	@Override
	public boolean isResultSet() {
		return true;
	}

	@Override
	public ResultSet toResultSet() {
		return this;
	}

	@Override
	public boolean isUpdateResult() {
		return false;
	}

	@Override
	public UpdateResult toUpdateResult() {
		throw new IllegalStateException("QueryResult is neither a ResultSet nor update count");
	}
	
	@Override
	public void close() throws SQLException {
		super.close();
	}

	@Override
	public String toString() {
		return "QueryResultAsResultSet [toResultSet()=" + toResultSet() + "]";
	}

}
