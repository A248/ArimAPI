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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import space.arim.universal.util.lang.AutoClosable;

public abstract class AbstractConnection implements AutoClosable {

	private final String url;
	
	private HttpURLConnection connection;
	private Map<String, String> properties;
	
	AbstractConnection(String url) {
		this.url = url;
	}
	
	boolean secure() {
		return url.contains("https://");
	}
	
	void open() throws MalformedURLException, IOException {
		connection = (HttpURLConnection) (new URL(url)).openConnection();
	}
	
	void setProperties() {
		getProperties().forEach((key, value) -> connection().setRequestProperty(key, value));
	}
	
	void setRequestMethod(String method) throws ProtocolException {
		connection().setRequestMethod(method);
	}
	
	HttpURLConnection connection() {
		return connection;
	}
	
	InputStream inputStream() throws IOException {
		return connection().getInputStream();
	}
	
	Map<String, String> getProperties() {
		return properties != null ? properties : Collections.emptyMap();
	}
	
	AbstractConnection setProperty(String key, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(key, value);
		return this;
	}
	
	@Override
	public void close() {
		connection().disconnect();
	}
	
}
