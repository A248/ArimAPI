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
import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.api.util.sql.MultiResultSet;

/**
 * An implementation of MultiResultSet which closes a connection when it is closed.
 * 
 * @author A248
 *
 */
public class MultiResultSetWithConnection implements MultiResultSet {

	private final ResultSet[] resultSetArray;
	private final Connection connection;
	
	/**
	 * Creates from a backing array and a connection
	 * 
	 * @param resultSetArray the result set array
	 * @param connection the connection to close when this is closed
	 */
	public MultiResultSetWithConnection(ResultSet[] resultSetArray, Connection connection) {
		this.resultSetArray = resultSetArray;
		this.connection = connection;
	}

	@Override
	public ResultSet get(int index) {
		return resultSetArray[index];
	}

	@Override
	public int length() {
		return resultSetArray.length;
	}

	@Override
	public void close() throws SQLException {
		// Close in reverse order
		for (int n = resultSetArray.length - 1; n >= 0; n--) {
			resultSetArray[n].close();
		}
		connection.close();
	}

}
