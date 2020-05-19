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
import java.sql.SQLException;

/**
 * A closable which will release a connection and prepared statement wrapped with it. <br>
 * Used to help implement {@link SqlBackend}.
 * 
 * @author A248
 *
 */
public class CloseMeWithConnectionAndPreparedStatement extends CloseMeWithConnection {

	private final PreparedStatement preparedStatement;
	
	public CloseMeWithConnectionAndPreparedStatement(Connection connection, PreparedStatement preparedStatement) {
		super(connection);
		this.preparedStatement = preparedStatement;
	}
	
	@Override
	public void close() throws SQLException {
		preparedStatement.close();
		super.close();
	}

}
