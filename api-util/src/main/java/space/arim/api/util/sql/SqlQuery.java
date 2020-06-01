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

import java.util.Arrays;
import java.util.Objects;

/**
 * An executable query wrapping a statement string and arguments to the statement. <br>
 * Immutable and thread safe. <br>
 * <br>
 * Implementations of {@link SqlBackend} will create a PreparedStatement from the statement string,
 * then parameters in the statement will be replaced with the arguments provided. <br>
 * <br>
 * The statement string must be nonnull because it does not make any sense to execute a null query.
 * 
 * @author A248
 *
 */
public class SqlQuery {

	private final String statement;
	private final Object[] args;
	
	/**
	 * Creates from a statement string and arguments to it
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 */
	public SqlQuery(String statement, Object...args) {
		this.statement = Objects.requireNonNull(statement, "The statement of a SqlQuery must not be null");
		this.args = args;
	}
	
	/**
	 * Creates from a statement string without any arguments
	 * 
	 * @param statement the statement string; no parameters are possible since no arguments are provided
	 */
	public SqlQuery(String statement) {
		this(statement, (Object[]) null);
	}
	
	/**
	 * Exactly the same as using {@link #SqlQuery(String, Object...)}
	 * 
	 * @param statement the statement string, using question marks where parameters are to be inserted
	 * @param args the arguments to the prepared statement, may be null or empty
	 * @return the query wrapper, never <code>null</code>
	 */
	public static SqlQuery of(String statement, Object...args) {
		return new SqlQuery(statement, args);
	}
	
	/**
	 * Exactly the same as using {@link #SqlQuery(String)}
	 * 
	 * @param statement the statement string; no parameters are possible since no arguments are provided
	 * @return the query wrapper, never <code>null</code>
	 */
	public static SqlQuery of(String statement) {
		return new SqlQuery(statement);
	}
	
	/**
	 * Gets the statement string of this query
	 * 
	 * @return the statement string
	 */
	public String getStatement() {
		return statement;
	}
	
	/**
	 * Gets the arguments of this query
	 * 
	 * @return the arguments, may be null or empty
	 */
	public Object[] getArgs() {
		return args;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + statement.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SqlQuery)) {
			return false;
		}
		SqlQuery other = (SqlQuery) obj;
		return Arrays.equals(args, other.args) && statement.equals(other.statement);
	}
	
	@Override
	public String toString() {
		return "SqlQuery [statement=" + statement + ", args=" + Arrays.toString(args) + "]";
	}
	
}
