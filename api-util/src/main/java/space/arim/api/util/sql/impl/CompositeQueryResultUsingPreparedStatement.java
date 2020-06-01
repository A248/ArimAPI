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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import space.arim.api.util.sql.CompositeQueryResult;
import space.arim.api.util.sql.QueryResult;

/**
 * An implementation of {@link CompositeQueryResult} using a prepared statement from which to draw
 * both the initial and all future results.
 * 
 * @author A248
 *
 */
public class CompositeQueryResultUsingPreparedStatement implements CompositeQueryResult {

	private final PreparedStatement preparedStatement;
	private QueryResult startQueryResult;
	private List<QueryResult> queryResults = new ArrayList<>();

	/**
	 * Creates from a backing prepared statement. The first query result is immediately
	 * derived from the statement.
	 * 
	 * @param preparedStatement the prepared statement
	 * @throws SQLException if something went wrong getting the initial query result
	 */
	public CompositeQueryResultUsingPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		this.preparedStatement = preparedStatement;
		startQueryResult = getInitialQueryResult();
	}
	
	private QueryResult getInitialQueryResult() throws SQLException {
		int updateCount = preparedStatement.getUpdateCount();
		if (updateCount != -1) { // -1 means there is no update count
			return new QueryResultAsUpdateCount(updateCount);
		}
		ResultSet resultSet = preparedStatement.getResultSet();
		if (resultSet != null) { // null means there is no result set
			return new QueryResultAsResultSet(resultSet);
		}
		return new QueryResultAsNeither();
	}
	
	private QueryResult getNext() throws SQLException {
		if (startQueryResult != null) {
			QueryResult startResult = startQueryResult;
			startQueryResult = null;
			return startResult;
		}
		if (preparedStatement.getMoreResults()) {
			return new QueryResultAsResultSet(preparedStatement.getResultSet());
		}
		int updateCount = preparedStatement.getUpdateCount();
		if (updateCount != -1) {
			return new QueryResultAsUpdateCount(updateCount);
		}
		return new QueryResultAsNeither();
	}
	
	@Override
	public QueryResult next() throws SQLException {
		QueryResult next = getNext();
		queryResults.add(next);
		return next;
	}
	
	@Override
	public void skip(int skip) throws SQLException {
		while (skip > 0) {
			preparedStatement.getMoreResults();
			skip--;
		}
	}

	@Override
	public void close() throws SQLException {
		for (QueryResult qr : queryResults) {
			qr.close();
		}
		preparedStatement.close();
	}
	
}
