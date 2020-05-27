/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2019 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * A kind of plugin or application which is able to execute queries. <br>
 * <br>
 * All methods throw {@link SQLException}
 * 
 * @author A248
 * 
 * @deprecated For multiple reasons. First, the void return types of {@link #executionQuery(String, Object...)}
 * and {@link #executionQueries(ExecutableQuery...)} make it unclear whether implementations may execute queries
 * in a separate thread. Additionally, they do not allow for retrieving update counts. Second, the array-based
 * return type of {@link #selectionQueries(ExecutableQuery...)} is cumbersome for working with try-with-resources
 * and requires that each result set be closed. See the package javadoc for an alternative.
 */
@Deprecated
public interface SQLExecution {
	
	/**
	 * Executes a given set of queries. This is a blocking operation.
	 * 
	 * @param queries the {@link ExecutableQuery} objects to run
	 * @throws SQLException if something went wrong
	 */
	void executionQueries(ExecutableQuery...queries) throws SQLException;
	
	/**
	 * Same as {@link #executionQueries(ExecutableQuery...)} but accepts a <code>Collection</code> instead.
	 * 
	 * @param queries the queries to run
	 * @throws SQLException if something went wrong
	 */
	default void executionQueries(Collection<ExecutableQuery> queries) throws SQLException {
		executionQueries(queries.toArray(new ExecutableQuery[] {}));
	}
	
	/**
	 * Executes a single query. This is a blocking operation.
	 * 
	 * @param query the SQL statement before parameterisation
	 * @param parameters the parameters to add to the statement
	 * @throws SQLException if something went wrong
	 */
	void executionQuery(String query, Object...parameters) throws SQLException;
	
	/**
	 * Executes a given set of queries and returns the results. This is a blocking operation.
	 * 
	 * @param queries the {@link ExecutableQuery} objects to run
	 * @return result array ordered according to the order of the parameter array
	 * @throws SQLException if something went wrong
	 */
	ResultSet[] selectionQueries(ExecutableQuery...queries) throws SQLException;
	
	/**
	 * Same as {@link #selectionQueries(ExecutableQuery...)} but accepts a <code>List</code> instead.
	 * 
	 * @param queries the queries to run
	 * @return result array ordering according to the order of the parameter list
	 * @throws SQLException if something went wrong
	 */
	default ResultSet[] selectionQueries(List<ExecutableQuery> queries) throws SQLException {
		return selectionQueries(queries.toArray(new ExecutableQuery[] {}));
	}
	
	/**
	 * Executes a single query and returns the result. This is a blocking operation.
	 * 
	 * @param query the SQL statement before parameterisation
	 * @param parameters the parameters to add to the statement
	 * @return the result set
	 * @throws SQLException if something went wrong
	 */
	ResultSet selectionQuery(String query, Object...parameters) throws SQLException;
	
}
