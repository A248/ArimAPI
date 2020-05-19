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

/**
 * Concrete implementation of {@link SqlBackend} using, as the name implies,
 * a HikariCP connection pool. <br>
 * <br>
 * All methods are thread safe because HikariCP is itself thread safe.
 * 
 * @author A248
 *
 */
public class HikariPoolSqlBackend implements SqlBackend {

	private final HikariDataSource dataSource;
	
	/**
	 * Creates from a hikari configuration
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

			resultSetArray[n] = new ResultSetProxyWithPreparedStatement(preparedStatement.getResultSet(), preparedStatement);
		}
		return new MultiResultSetWithPooledConnection(resultSetArray, connection);
	}

	@Override
	public void close() throws SQLException {
		dataSource.close();
	}

}
