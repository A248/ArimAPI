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
package space.arim.api.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.shaded.com.zaxxer.hikari.HikariConfig;
import space.arim.shaded.com.zaxxer.hikari.HikariDataSource;

import space.arim.api.util.sql.impl.*;

/**
 * Concrete implementation of {@link SqlBackend} using, as the name implies,
 * a HikariCP connection pool. <br>
 * Supports both modes of auto-commit. <br>
 * <br>
 * All methods are thread safe because HikariCP is itself thread safe. <br>
 * 
 * @author A248
 *
 */
public class HikariPoolSqlBackend implements ConcurrentSqlBackend {

	private final HikariDataSource dataSource;
	
	/**
	 * Creates from a hikari configuration. A backing data source
	 * is immediately created and the pool is started from the config.
	 * 
	 * @param hikariConfig the hikari config
	 */
	public HikariPoolSqlBackend(HikariConfig hikariConfig) {
		dataSource = new HikariDataSource(hikariConfig);
	}
	
	@Override
	public CloseMe execute(String statement, Object... args) throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		SqlBackendImplUtils.applyArguments(preparedStatement, args);
		preparedStatement.execute();

		return new CloseMeWithConnectionAndPreparedStatement(connection, preparedStatement);
	}

	@Override
	public CloseMe execute(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		Connection connection = dataSource.getConnection();

		PreparedStatement[] preparedStatementArray = new PreparedStatement[queries.length];
		for (int n = 0; n < queries.length; n++) {
			SqlQuery query = queries[n];

			PreparedStatement preparedStatement = connection.prepareStatement(query.getStatement());
			SqlBackendImplUtils.applyArguments(preparedStatement, query.getArgs());
			preparedStatement.execute();

			preparedStatementArray[n] = preparedStatement;
		}
		return new CloseMeWithConnectionAndPreparedStatementArray(connection, preparedStatementArray);
	}
	
	@Override
	public ResultSet select(String statement, Object... args) throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		SqlBackendImplUtils.applyArguments(preparedStatement, args);
		ResultSet resultSet = preparedStatement.executeQuery();

		return new ResultSetProxyWithPreparedStatementAndConnection(resultSet, preparedStatement, connection);
	}

	@Override
	public MultiResultSet select(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		Connection connection = dataSource.getConnection();

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
	}

	@Override
	public QueryResult query(String statement, Object... args) throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		SqlBackendImplUtils.applyArguments(preparedStatement, args);
		preparedStatement.execute();

		int updateCount = preparedStatement.getUpdateCount();
		if (updateCount != -1) { // -1 means there is no update count
			return new QueryResultAsUpdateCountWithPreparedStatementAndConnection(updateCount, preparedStatement, connection);
		}
		ResultSet resultSet = preparedStatement.getResultSet();
		if (resultSet != null) { // null means there is no result set
			return new QueryResultAsResultSetWithPreparedStatementAndConnection(resultSet, preparedStatement, connection);
		}
		return new QueryResultAsNeitherWithPreparedStatementAndConnection(preparedStatement, connection);
	}

	@Override
	public MultiQueryResult query(SqlQuery... queries) throws SQLException {
		SqlBackendImplUtils.validatePositiveLength(queries);
		Connection connection = dataSource.getConnection();

		QueryResult[] queryResultArray = new QueryResult[queries.length];
		for (int n = 0; n < queries.length; n++) {
			SqlQuery query = queries[n];

			PreparedStatement preparedStatement = connection.prepareStatement(query.getStatement());
			SqlBackendImplUtils.applyArguments(preparedStatement, query.getArgs());
			preparedStatement.execute();
			
			int updateCount = preparedStatement.getUpdateCount();
			if (updateCount != -1) { // -1 means there is no update count
				queryResultArray[n] = new QueryResultAsUpdateCountWithPreparedStatement(updateCount, preparedStatement);
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
	}
	
	@Override
	public void close() throws SQLException {
		dataSource.close();
	}
	
	@Override
	public String toString() {
		return "HikariPoolSqlBackend [dataSource=" + dataSource + "]";
	}

}
