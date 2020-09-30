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

import java.sql.SQLException;

/**
 * Subclass of {@link AutoCloseable} which specifically throws {@link SQLException} in {@link #close()}
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.util.sql} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface SqlClosable extends AutoCloseable {

	/**
	 * Releases any underlying resources.
	 * 
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	@Override
	void close() throws SQLException;
	
}
