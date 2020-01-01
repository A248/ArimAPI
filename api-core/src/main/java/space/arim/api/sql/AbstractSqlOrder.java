/*
 * ArimBans3, a punishment plugin for minecraft servers
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimBans3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimBans3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimBans3. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlOrder<T> {
	
	private List<T> queries = new ArrayList<T>();
	private ResultSet[] results;
	private int resultNum = 0;
	
	AbstractSqlOrder<T> addQuery(T query) {
		queries.add(query);
		resultNum++;
		return this;
	}
	
	AbstractSqlOrder<T> build() {
		results = new ResultSet[resultNum];
		resultNum = 0;
		return this;
	}
	
	protected abstract ExecutableQuery convert(T query);
	
	public boolean hasNext() {
		return resultNum < queries.size();
	}
	
	public ExecutableQuery next() {
		return convert(queries.get(resultNum));
	}
	
	public void populateResult(ResultSet data) {
		results[resultNum++] = data;
	}
	
	public ResultSet[] getResults() {
		return results;
	}
	
}
