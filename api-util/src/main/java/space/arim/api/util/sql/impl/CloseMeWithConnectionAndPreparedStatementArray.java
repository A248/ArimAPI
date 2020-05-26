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
import java.sql.SQLException;
import java.util.Arrays;

import space.arim.api.util.sql.SqlBackend;

/**
 * A closable which will release a connection and prepared statement array wrapped with it. <br>
 * Used to help implement {@link SqlBackend}.
 * 
 * @author A248
 *
 */
public class CloseMeWithConnectionAndPreparedStatementArray extends CloseMeWithConnection {

	private final PreparedStatement[] preparedStatementArray;
	
	public CloseMeWithConnectionAndPreparedStatementArray(Connection connection, PreparedStatement[] preparedStatementArray) {
		super(connection);
		this.preparedStatementArray = preparedStatementArray;
	}
	
	@Override
	public void close() throws SQLException {
		// Close in reverse order
		for (int n = preparedStatementArray.length - 1; n >= 0; n--) {
			preparedStatementArray[n].close();
		}
		super.close();
	}

	@Override
	public String toString() {
		return "CloseMeWithConnectionAndPreparedStatementArray [preparedStatementArray="
				+ Arrays.toString(preparedStatementArray) + ", toString()=" + super.toString() + "]";
	}

}
