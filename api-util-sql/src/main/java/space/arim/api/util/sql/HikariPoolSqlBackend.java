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

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Concrete implementation of {@link SqlBackend} using, as the name implies,
 * a HikariCP connection pool. <br>
 * Supports both modes of auto-commit. <br>
 * <br>
 * All methods are thread safe because HikariCP is itself thread safe. <br>
 * 
 * @author A248
 *
 */
public class HikariPoolSqlBackend extends BaseSqlBackend {

	private final HikariDataSource dataSource;
	
	/**
	 * Creates from a hikari configuration. A backing data source
	 * is immediately created and the pool is started from the config.
	 * 
	 * @param hikariConfig the hikari config
	 */
	public HikariPoolSqlBackend(HikariConfig hikariConfig) {
		this(new HikariDataSource(hikariConfig));
	}
	
	/**
	 * Creates from a hikari data source. The data source must be open
	 * and ready for operation.
	 * 
	 * @param dataSource the hikari data source
	 */
	public HikariPoolSqlBackend(HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Gets the hikari data source used by this sql backend
	 * 
	 * @return the hikari data source
	 */
	public HikariDataSource getDataSource() {
		return dataSource;
	}
	
	@Override
	Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	@Override
	public void close() throws SQLException {
		dataSource.close();
	}
	
	@Override
	public String toString() {
		return "HikariPoolSqlBackend [dataSource=" + dataSource + "]";
	}

}
