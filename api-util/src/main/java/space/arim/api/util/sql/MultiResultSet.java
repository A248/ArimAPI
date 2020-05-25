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
 * A package of a ResultSet array which is auto closable for convenience. <br>
 * Closing will close all of the underlying ResultSets AND reliant connections.
 * 
 * @author A248
 *
 */
public interface MultiResultSet extends AutoCloseable {

	/**
	 * Gets one of the result sets in the array. <br>
	 * If the statement from which the result set in question did not produce a ResultSet,
	 * this will return <code>null</code>
	 * 
	 * @param index the index
	 * @return the result set at the index
	 * @throws ArrayIndexOutOfBoundsException if <code>index {@literal <} 0 || index {@literal >}= length()</codE>
	 */
	ResultSet get(int index);
	
	/**
	 * The size of the ResultSet array
	 * 
	 * @return the size or length of the array
	 */
	int length();
	
	/**
	 * Closes and releases all resources, including but not limited to each ResultSet in the underlying array
	 * 
	 */
	@Override
	void close() throws SQLException;

}
