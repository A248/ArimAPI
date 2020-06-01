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
 * <b>Exceptions</b> <br>
 * All methods throw {@link SQLException}. Error handling is also intended to be done on the caller's side. <br>
 * Furthermore, the interface is designed to use PreparedStatement's in the implementation. <br>
 * <br>
 * <b>Auto-Commit</b> <br>
 * It is recommmended that both modes of auto-commit be supported by implementations – that, if auto-commit is off,
 * implementations commit the connection after all statements have been executed and before the connection is closed.
 * However, this is not a requirement. <br>
 * <br>
 * <b>Composite Queries</b> <br>
 * Using {@link #composite(String, Object...)}, it is possible to execute multiple statements using one statement string.
 * Note that the relevant jdbc url property, <code>allowMultiQueries</code>, must be enabled. <br>
 * <br>
 * Statement strings must be nonnull because it does not make any sense to execute a null query.
 * 
 * @author A248
 *
 */
public interface SqlBackend extends SqlClosable {
	
	/**
	 * Executes a simple statement without returning any information as to the result of the query.
	 *  
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return an object which must be closed by the caller
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	CloseMe execute(String statement, Object...args) throws SQLException;
	
	/**
	 * Using a single connection, executes multiple statements wrapped in {@link SqlQuery}s.
	 * Returns no information as to the result of the query.
	 * 
	 * @param queries the queries to execute
	 * @return  an object which must be closed by the caller
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	CloseMe execute(SqlQuery...queries) throws SQLException;
	
	/**
	 * Executes a statement, assuming it to be one which produces a ResultSet, and returns the ResultSet.
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return the result set which must be closed by the caller
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	ResultSet select(String statement, Object...args) throws SQLException;
	
	/**
	 * Using a single connection, executes multiple statements wrapped in {@link SqlQuery}s and returns results. <br>
	 * <br>
	 * The index of each query relates to a corresponding index in the returned {@link MultiResultSet};
	 * thus, {@link MultiResultSet#length()} will equal to the length of the input array. <br>
	 * <br>
	 * If a query does not produce a result set (for example, an <code>INSERT</code> statement),
	 * the corresponding <code>ResultSet</code> will be <code>null</code>. Therefore, some of the statements
	 * presumably produce result sets otherwise other methods may be used. <br>
	 * 
	 * @param queries the queries to execute
	 * @return an array of result sets enclosed in a single closable package
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	MultiResultSet select(SqlQuery...queries) throws SQLException;
	
	/**
	 * Executes a statement, returning the result of the query as a {@link QueryResult}
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return the query result which must be closed by the caller
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	QueryResult query(String statement, Object...args) throws SQLException;
	
	/**
	 * Using a single connection, executes multiple statements wrapped in {@link SqlQuery}s and returns results
	 * for each query, combined as a {@link MultiQueryResult}. <br>
	 * <br>
	 * The index of each query relates to a corresponding index in the returned {@link MultiResultSet};
	 * thus, {@link MultiResultSet#length()} will equal to the length of the input array.
	 * 
	 * @param queries the queries to execute
	 * @return an array of query results enclosed in a single closable package
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	MultiQueryResult query(SqlQuery...queries) throws SQLException; 
	
	/**
	 * Executes multiple statements contained within the statement string
	 * separated by semi{@literal -}colons, returning the result as a {@link CompositeQueryResult}. <br>
	 * <br>
	 * Note: This method has no equivalent <code>composite(SqlQuery...)</code> method
	 * because such a method defeats the purpose of executing a composite statement.
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return the full, composite results of all statements executed
	 * @throws SQLException generally, depending on the implementation, as relayed from JDBC
	 */
	CompositeQueryResult composite(String statement, Object...args) throws SQLException;
	
	/**
	 * Closes the backend, releasing any used resources
	 * 
	 */
	@Override
	void close() throws SQLException;

}
