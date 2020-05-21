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
 * A SQL database handler where each returned object is closable. <br>
 * Thus, a single query requires exactly 1 try/catch with resources on the caller's behalf. <br>
 * <br>
 * All methods throw {@link SQLException}. Error handling is also intended to be done on the caller's side. <br>
 * Furthermore, the interface is designed to use PreparedStatement's in the implementation.
 * 
 * @author A248
 *
 */
public interface SqlBackend extends AutoCloseable {
	
	/**
	 * Executes a simple statement without returning any results
	 *  
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return an object which must be closed by the caller
	 * @throws SQLException if something went wrong SQL wise
	 */
	CloseMe execute(String statement, Object...args) throws SQLException;
	
	/**
	 * Using a single connection, executes multiple statements wrapped in {@link SqlQuery}s
	 * 
	 * @param queries the queries to execute
	 * @return  an object which must be closed by the caller
	 * @throws SQLException if something went wrong SQL wise
	 */
	CloseMe execute(SqlQuery...queries) throws SQLException;
	
	/**
	 * Executes a select statement and returns a ResultSet.
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return the result set which must be closed by the caller
	 * @throws SQLException if something went wrong SQL wise
	 */
	ResultSet select(String statement, Object...args) throws SQLException;
	
	/**
	 * Using a single connection, executes multiple statements wrapped in {@link SqlQuery}s and returns results. <br>
	 * Some of the statements presumably produce result sets otherwise {@link #execute(SqlQuery...)} may be used. <br>
	 * <br>
	 * The index of each query relates to a corresponding index in the returned {@link MultiResultSet};
	 * thus, {@link MultiResultSet#length()} will equal to the length of the input array. <br>
	 * If a query does not produce a result set (for example, an <code>INSERT</code> statement),
	 * the corresponding <code>ResultSet</code> will be <code>null</code>
	 * 
	 * @param queries the queries to execute
	 * @return an array of result sets enclosed in a single closable package
	 * @throws SQLException if something went wrong SQL wise
	 */
	MultiResultSet select(SqlQuery...queries) throws SQLException;
	
	/**
	 * Closes the backend, releasing any accompanying resources
	 * 
	 */
	@Override
	void close() throws SQLException;

}
