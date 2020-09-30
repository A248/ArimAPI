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
 * Represents the results created from a single query with multiple statements.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.util.sql} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public interface CompositeQueryResult extends SqlClosable {

	/**
	 * Gets the next QueryResult and moves the cursor forward. Will return
	 * the first QueryResult if this or {@link #skip(int)} has never been called before.
	 * 
	 * @return the next query result
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	QueryResult next() throws SQLException;
	
	/**
	 * Skips the specified amount of query results. This is a shortcut
	 * instead of calling {@link #next()} merely to skip unneeded results.
	 * 
	 * @param skip the amount of results to skip
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	void skip(int skip) throws SQLException;
	
}
