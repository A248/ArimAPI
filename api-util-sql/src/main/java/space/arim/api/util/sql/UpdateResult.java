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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A result representing an update count, and possible generated keys. <br>
 * Counterpart to ResultSet.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.util.sql} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface UpdateResult extends AutoCloseable {

	/**
	 * Gets the update count
	 * 
	 * @return the update count
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	int getUpdateCount() throws SQLException;
	
	/**
	 * Gets the automatically generated keys, such as any auto increment
	 * columns in an <code>INSERT</code> statement.
	 * 
	 * @return the generated keys
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	ResultSet getGeneratedKeys() throws SQLException;
	
	/**
	 * Releases any underlying resources.
	 * 
	 */
	@Override
	void close() throws SQLException;
	
}
