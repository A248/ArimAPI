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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A ResultSet linked to a PreparedStatement and a Connection; closing the ResultSet closes all 3.
 * 
 * @author A248
 *
 */
public class ResultSetProxyWithPreparedStatementAndConnection extends ResultSetProxyWithPreparedStatement {

	private final Connection connection;
	
	/**
	 * Creates from a backing ResultSet and accompanying PreparedStatement and Connection
	 * 
	 * @param resultSet the backing result set
	 * @param preparedStatement the prepared statement to close when this object is closed
	 * @param connection the connection close when this object is closed
	 */
	public ResultSetProxyWithPreparedStatementAndConnection(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		super(resultSet, preparedStatement);
		this.connection = connection;
	}
	
	@Override
	public void close() throws SQLException {
		if (!connection.getAutoCommit()) {
			connection.commit();
		}
		super.close();
		connection.close();
	}

	@Override
	public String toString() {
		return "ResultSetProxyWithPreparedStatementAndConnection [connection=" + connection + ", toString()="
				+ super.toString() + "]";
	}
	
}
