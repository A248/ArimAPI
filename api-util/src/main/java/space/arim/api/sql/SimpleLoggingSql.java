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

import space.arim.universal.util.proxy.ProxiedConnection;

/**
 * Subclass of {@link LoggingSql} using HikariCP for the implementation.
 * 
 * @author A248
 *
 * @deprecated For multiple reasons. First, see deprecation of {@link SQLExecution}, {@link AbstractSql}, and {@link LoggingSql}.
 * This class, specifically, presupposes a long{@literal -}running <code>Connection</code> which is reused for SQL queries.
 * Such a practice should not be encouraged, for it leads itself to perpetual timeout issues; using properties such as
 * <i>autoReconnect</i> only complicates matters and causes more issues. Furthermore, concurrent use of this class requires
 * external synchronisation because a <code>Connection</code> is not always safe. On a minor note, this class also creates
 * unnecessary wrapper objects for <code>Connection</code> to avoid closing the long running connection due to the design
 * of {@link space.arim.api.sql}.
 */
@SuppressWarnings("deprecation")
@Deprecated
public abstract class SimpleLoggingSql extends LoggingSql {
	
	protected abstract Connection getUnderlyingConnection();
	
	@Override
	protected final Connection getConnection() {
		return new FakeclosableConnection(getUnderlyingConnection());
	}

}

class FakeclosableConnection extends ProxiedConnection  {
	
	FakeclosableConnection(Connection connection) {
		super(connection);
	}
	
	@Override
	public void close() throws SQLException {
		// don't close the underlying connection as it needs to be reused
	}
	
}
