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
package space.arim.api.util.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResultAsUpdateResultUsingResultSet extends QueryResultAsUpdateResult {

	private final ResultSet generatedKeys;
	
	public QueryResultAsUpdateResultUsingResultSet(int updateCount, ResultSet generatedKeys) {
		super(updateCount);
		this.generatedKeys = generatedKeys;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return generatedKeys;
	}
	
	@Override
	public void close() throws SQLException {
		generatedKeys.close();
	}

}