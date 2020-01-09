/* 
 * ArimAPI, a minecraft plugin library and framework.
 * Copyright © 2020 Anand Beh <https://www.arim.space>
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
package space.arim.api.util.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ParameterBuilder {
	
	private final HashMap<String, String> parameters = new HashMap<String, String>();
	
	ParameterBuilder add(String key, String object) {
		parameters.put(key, encode(object));
		return this;
	}
	
	ParameterBuilder add(String key, Object object) {
		return add(key, object.toString());
	}
	
	String[] getParams() {
		ArrayList<String> params = new ArrayList<String>();
		parameters.forEach((key, value) -> params.add(key + "=" + value));
		return params.toArray(new String[] {});
	}
	
	private static String encode(String object) {
		try {
			return URLEncoder.encode(object, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("UTF isn't supported!?!", ex);
		}
	}
	
}