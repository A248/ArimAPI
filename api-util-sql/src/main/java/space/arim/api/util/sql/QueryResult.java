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
 * The result of a SQL query which is either a ResultSet, an update result, or neither. <br>
 * Which type of result this represents depends on the query which produced this object.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.util.sql} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface QueryResult extends SqlClosable {

	/**
	 * Whether this constitutes a ResultSet.
	 * 
	 * @return true if a result set, false otherwise
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	boolean isResultSet() throws SQLException;
	
	/**
	 * Assuming {@link #isResultSet()} is true, gets the ResultSet represented.
	 * 
	 * @return the result set
	 * @throws RuntimeException or a subclass thereof, if there is no result set
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	ResultSet toResultSet() throws SQLException;
	
	/**
	 * Whether this constitutes an update result
	 * 
	 * @return true if an update result, false otherwise
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	boolean isUpdateResult() throws SQLException;
	
	/**
	 * Assuming {@link #isUpdateResult()} is true, gets the update result represented.
	 * 
	 * @return the update result
	 * @throws RuntimeException or a subclass thereof, if there is no update result
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	UpdateResult toUpdateResult() throws SQLException;
	
}
