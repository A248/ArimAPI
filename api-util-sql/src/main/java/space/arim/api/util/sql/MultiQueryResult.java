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
 * A package of a QueryResult array which is auto closable for convenience. <br>
 * Closing will close all of the underlying query results AND reliant connections.
 * 
 * @author A248
 *
 */
public interface MultiQueryResult extends SqlClosable {

	/**
	 * Gets one of the query results in the array. Never <code>null</code>
	 * 
	 * @param index the index
	 * @return the query result at the index
	 * @throws ArrayIndexOutOfBoundsException if <code>index {@literal <} 0 || index {@literal >}= length()</code>
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	QueryResult get(int index) throws SQLException;
	
	/**
	 * The size of the QueryResult array
	 * 
	 * @return the size or length of the array
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	int length() throws SQLException;
	
}