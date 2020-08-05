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
package space.arim.api.util.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An abstract implementation of {@link SqlBackend} relying on {@link #getConnection()} to open connections.
 * 
 * @author A248
 *
 */
public abstract class AbstractSqlBackend extends BaseSqlBackend {

	/**
	 * Gets an open connection. The connection will be closed when no longer needed.
	 * 
	 * @return an open connection
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	@Override
	protected abstract Connection getConnection() throws SQLException;
	
}
