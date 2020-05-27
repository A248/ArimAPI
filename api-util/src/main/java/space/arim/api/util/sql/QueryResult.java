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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The result of a SQL query which is either a ResultSet, an update count, or neither. <br>
 * Which type of result this represents depends on the query which produced this object.
 * 
 * @author A248
 *
 */
public interface QueryResult extends AutoCloseable {

	/**
	 * Whether this constitutes a ResultSet.
	 * 
	 * @return true if a result set, false otherwise
	 */
	boolean isResultSet();
	
	/**
	 * Assuming {@link #isResultSet()} is true, gets the ResultSet represented.
	 * 
	 * @return the result set
	 * @throws RuntimeException or a subclass thereof, if there is no result set
	 */
	ResultSet toResultSet();
	
	/**
	 * Whether this constitutes an update count
	 * 
	 * @return true if an update count, false otherwise
	 */
	boolean isUpdateCount();
	
	/**
	 * Assuming {@link #isUpdateCount()} is true, gets the update count represented.
	 * 
	 * @return the update count
	 * @throws RuntimeException or a subclass thereof, if there is no update count
	 */
	int toUpdateCount();
	
	/**
	 * Closes and releases all resources, including any result sets and update counts represented
	 * 
	 * @throws SQLException if something went wrong SQL wise
	 */
	@Override
	void close() throws SQLException;
	
}
