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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import space.arim.api.util.sql.MultiQueryResult;
import space.arim.api.util.sql.QueryResult;

/**
 * An implementation of MultiQueryResult which closes a connection when it is closed.
 * 
 * @author A248
 *
 */
public class MultiQueryResultWithConnection implements MultiQueryResult {

	private final QueryResult[] queryResultArray;
	private final Connection connection;
	
	/**
	 * Creates from a backing array and a connection
	 * 
	 * @param queryResultArray the query result array
	 * @param connection the connection to close when this is closed
	 */
	public MultiQueryResultWithConnection(QueryResult[] queryResultArray, Connection connection) {
		this.queryResultArray = queryResultArray;
		this.connection = connection;
	}

	@Override
	public QueryResult get(int index) {
		return queryResultArray[index];
	}

	@Override
	public int length() {
		return queryResultArray.length;
	}

	@Override
	public void close() throws SQLException {
		if (!connection.getAutoCommit()) {
			connection.commit();
		}
		// Close in reverse order
		for (int n = queryResultArray.length - 1; n >= 0; n--) {
			queryResultArray[n].close();
		}
		connection.close();
	}

	@Override
	public String toString() {
		return "MultiQueryResultWithConnection [queryResultArray=" + Arrays.toString(queryResultArray) + ", connection="
				+ connection + "]";
	}
	
}
