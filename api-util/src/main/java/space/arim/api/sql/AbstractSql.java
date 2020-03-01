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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import space.arim.universal.util.function.ErringLazySingleton;

public abstract class AbstractSql implements SQLExecution {
	
	private final ErringLazySingleton<RowSetFactory, SQLException> factory = new ErringLazySingleton<RowSetFactory, SQLException>(() -> RowSetProvider.newFactory());
	
	CachedRowSet createCachedRowSet() throws SQLException {
		return factory.get().createCachedRowSet();
	}
	
	protected abstract Connection getConnection() throws SQLException;
	
	private void replaceParams(PreparedStatement statement, Object...parameters) throws SQLException {
		for (int n = 0; n < parameters.length; n++) {
			statement.setObject(n + 1, parameters[n]);
		}
	}
	
	@Override
	public void executionQueries(ExecutableQuery...queries) throws SQLException {
		if (queries.length == 0) {
			return;
		}
		try (Connection connection = getConnection()) {
			PreparedStatement[] statements = new PreparedStatement[queries.length];
			for (int n = 0; n < queries.length; n++) {
				if (queries[n] != null) {
					statements[n] = connection.prepareStatement(queries[n].statement());
					replaceParams(statements[n], queries[n].parameters());
					statements[n].execute();
					statements[n].close();
				}
			}
		}
	}
	
	@Override
	public void executionQuery(String query, Object...params) throws SQLException {
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
			replaceParams(statement, params);
			statement.execute();
		}
	}
	
	@Override
	public ResultSet[] selectionQueries(ExecutableQuery...queries) throws SQLException {
		if (queries.length == 0) {
			return new ResultSet[] {};
		}
		try (Connection connection = getConnection()) {
			PreparedStatement[] statements = new PreparedStatement[queries.length];
			CachedRowSet[] results = new CachedRowSet[queries.length];
			for (int n = 0; n < queries.length; n++) {
				if (queries[n] == null) {
					results[n] = null;
				} else {
					statements[n] = connection.prepareStatement(queries[n].statement());
					replaceParams(statements[n], queries[n].parameters());
					results[n] = createCachedRowSet();
					results[n].populate(statements[n].executeQuery());
					statements[n].close();
				}
			}
			return results;
		}
	}
	
	@Override
	public ResultSet selectionQuery(String query, Object...params) throws SQLException {
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
			replaceParams(statement, params);
			CachedRowSet results = createCachedRowSet();
			results.populate(statement.executeQuery());
			return results;
		}
	}
	
}
