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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import space.arim.api.util.sql.SqlBackend;
import space.arim.api.util.sql.SqlQuery;

/**
 * A utility class used to help implement {@link SqlBackend}
 * 
 * @author A248
 *
 */
public class SqlBackendImplUtils {

	private SqlBackendImplUtils() {}
	
	/**
	 * Replaces parameters inside a PreparedStatement with their arguments. <br>
	 * If there is a mismatch between the number of parameters and the argument array,
	 * a {@link SQLException} is naturally thrown. <br>
	 * <br>
	 * If the arguments are null or empty, this is a no{@literal -}op.
	 * 
	 * @param preparedStatement the prepared statement to parameterise
	 * @param args the argument array, may be null or empty
	 * @throws SQLException if the parameterisation encountered an error
	 */
	public static void applyArguments(PreparedStatement preparedStatement, Object[] args) throws SQLException {
		if (args == null) {
			return;
		}
		int length = args.length;
		if (length == 0) {
			return;
		}
		for (int n = 0; n < length; n++) {
			preparedStatement.setObject(n + 1, args[n]);
		}
	}
	
	/**
	 * Validates that array is nonnull and its length is positive,
	 * otherwise throws an <code>IllegalArgumentException</code>
	 * 
	 * @param queries the input array whose length to validate
	 * @throws IllegalArgumentException if the length is zero or the array is null
	 */
	public static void validatePositiveLength(SqlQuery[] queries) {
		if (queries == null || queries.length == 0) {
			throw new IllegalArgumentException("Cannot execute zero queries");
		}
	}
	
	/**
	 * Validates that all of the contents of the array are nonnull.
	 * 
	 * @param queries the input array whose contents to validate
	 * @throws NullPointerException if any query in the array is null
	 */
	public static void validateNoneAreNull(SqlQuery[] queries) {
		for (SqlQuery query : queries) {
			Objects.requireNonNull(query, "No SqlQuery may be null");
		}
	}
	
}
