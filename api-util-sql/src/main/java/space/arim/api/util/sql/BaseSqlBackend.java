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
package space.arim.api.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import space.arim.api.util.sql.impl.*;

abstract class BaseSqlBackend implements SqlBackend {

	abstract Connection getConnection() throws SQLException;
	
	@Override
	public CloseMe execute(String statement, Object... args) throws SQLException {
		Objects.requireNonNull(statement, "The statement to execute must not be null");
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			SqlBackendImplUtils.applyArguments(preparedStatement, args);
			preparedStatement.execute();

			return new CloseMeWithConnectionAndPreparedStatement(connection, preparedStatement);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}

	@Override
	public CloseMe execute(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		SqlBackendImplUtils.validateNoneAreNull(queries);
		Connection connection = getConnection();
		try {
			PreparedStatement[] preparedStatementArray = new PreparedStatement[queries.length];
			for (int n = 0; n < queries.length; n++) {
				SqlQuery query = queries[n];

				PreparedStatement preparedStatement = connection.prepareStatement(query.getStatement());
				SqlBackendImplUtils.applyArguments(preparedStatement, query.getArgs());
				preparedStatement.execute();

				preparedStatementArray[n] = preparedStatement;
			}
			return new CloseMeWithConnectionAndPreparedStatementArray(connection, preparedStatementArray);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}
	
	@Override
	public ResultSet select(String statement, Object... args) throws SQLException {
		Objects.requireNonNull(statement, "The statement to execute must not be null");
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			SqlBackendImplUtils.applyArguments(preparedStatement, args);
			ResultSet resultSet = preparedStatement.executeQuery();

			return new ResultSetProxyWithPreparedStatementAndConnection(resultSet, preparedStatement, connection);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}

	@Override
	public MultiResultSet select(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		SqlBackendImplUtils.validateNoneAreNull(queries);
		Connection connection = getConnection();
		try {
			ResultSet[] resultSetArray = new ResultSet[queries.length];
			for (int n = 0; n < queries.length; n++) {
				SqlQuery query = queries[n];

				PreparedStatement preparedStatement = connection.prepareStatement(query.getStatement());
				SqlBackendImplUtils.applyArguments(preparedStatement, query.getArgs());
				preparedStatement.execute();

				ResultSet resultSet = preparedStatement.getResultSet();
				if (resultSet == null) {
					preparedStatement.close();
					resultSetArray[n] = null;
					continue;
				}
				resultSetArray[n] = new ResultSetProxyWithPreparedStatement(resultSet, preparedStatement);
			}
			return new MultiResultSetWithConnection(resultSetArray, connection);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}

	@Override
	public QueryResult query(String statement, Object... args) throws SQLException {
		Objects.requireNonNull(statement, "The statement to execute must not be null");
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			SqlBackendImplUtils.applyArguments(preparedStatement, args);
			preparedStatement.execute();

			int updateCount = preparedStatement.getUpdateCount();
			if (updateCount != -1) { // -1 means there is no update count
				return new QueryResultAsUpdateResultUsingPreparedStatementWithConnection(updateCount, preparedStatement, connection);
			}
			ResultSet resultSet = preparedStatement.getResultSet();
			if (resultSet != null) { // null means there is no result set
				return new QueryResultAsResultSetWithPreparedStatementAndConnection(resultSet, preparedStatement, connection);
			}
			return new QueryResultAsNeitherWithPreparedStatementAndConnection(preparedStatement, connection);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}

	@Override
	public MultiQueryResult query(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		SqlBackendImplUtils.validateNoneAreNull(queries);
		Connection connection = getConnection();
		try {
			QueryResult[] queryResultArray = new QueryResult[queries.length];
			for (int n = 0; n < queries.length; n++) {
				SqlQuery query = queries[n];

				PreparedStatement preparedStatement = connection.prepareStatement(query.getStatement());
				SqlBackendImplUtils.applyArguments(preparedStatement, query.getArgs());
				preparedStatement.execute();
				
				int updateCount = preparedStatement.getUpdateCount();
				if (updateCount != -1) { // -1 means there is no update count
					queryResultArray[n] = new QueryResultAsUpdateResultUsingPreparedStatement(updateCount, preparedStatement);
					continue;
				}
				ResultSet resultSet = preparedStatement.getResultSet();
				if (resultSet != null) { // null means there is no result set
					queryResultArray[n] = new QueryResultAsResultSetWithPreparedStatement(resultSet, preparedStatement);
					continue;
				}
				queryResultArray[n] = new QueryResultAsNeitherWithPreparedStatement(preparedStatement);
			}
			return new MultiQueryResultWithConnection(queryResultArray, connection);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}
	
	@Override
	public CompositeQueryResult composite(String statement, Object... args) throws SQLException {
		Objects.requireNonNull(statement, "The statement to execute must not be null");
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			SqlBackendImplUtils.applyArguments(preparedStatement, args);
			preparedStatement.execute();

			return new CompositeQueryResultUsingPreparedStatementWithConnection(preparedStatement, connection);
		} catch (SQLException ex) {
			connection.close();
			throw ex;
		}
	}

}