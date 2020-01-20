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
import java.sql.SQLException;

import space.arim.universal.util.sql.WrappedConnection;

public abstract class SimpleLoggingSql extends LoggingSql {

	private final Connection connection;
	
	protected SimpleLoggingSql(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	protected final Connection getConnection() {
		return new FakeclosableConnection(connection);
	}

}

class FakeclosableConnection extends WrappedConnection  {
	
	FakeclosableConnection(Connection connection) {
		super(connection);
	}
	
	@Override
	public void close() throws SQLException {
		// don't close the underlying connection as it needs to be reused
	}
	
}
