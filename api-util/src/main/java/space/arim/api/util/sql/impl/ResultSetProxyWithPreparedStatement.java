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

/**
 * A ResultSet linked to a PreparedStatement; closing the ResultSet closes the PreparedStatement.
 * 
 * @author A248
 *
 */
public class ResultSetProxyWithPreparedStatement extends ResultSetProxy {

	private final PreparedStatement preparedStatement;
	
	/**
	 * Creates from a backing ResultSet and accompanying PreparedStatement
	 * 
	 * @param resultSet the backing result set
	 * @param preparedStatement the prepared statement to close when this object is closed
	 */
	public ResultSetProxyWithPreparedStatement(ResultSet resultSet, PreparedStatement preparedStatement) {
		super(resultSet);
		this.preparedStatement = preparedStatement;
	}
	
	@Override
	public void close() throws SQLException {
		super.close();
		preparedStatement.close();
	}

	@Override
	public String toString() {
		return "ResultSetProxyWithPreparedStatement [preparedStatement=" + preparedStatement + ", toString()="
				+ super.toString() + "]";
	}
	
}
