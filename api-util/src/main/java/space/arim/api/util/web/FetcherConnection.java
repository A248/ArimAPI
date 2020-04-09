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
package space.arim.api.util.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import space.arim.universal.util.web.HttpStatus;
import space.arim.universal.util.web.HttpStatusException;

import space.arim.api.util.CommonInstancesUtil;

class FetcherConnection extends AbstractConnection {
	
	FetcherConnection(String url) {
		super(url);
	}
	
	FetcherConnection connect() throws IOException, HttpStatusException {
		open();
		connection().setRequestMethod("GET");
		setProperties();
		connection().connect();
		HttpStatus status = HttpStatus.fromCode(connection().getResponseCode());
		if (status != HttpStatus.OK) {
			throw new HttpStatusException(status);
		}
		return this;
	}
	
	<T> T getJson(Class<T> type) throws JsonSyntaxException, JsonIOException, IOException {
		return CommonInstancesUtil.gson().fromJson(new InputStreamReader(inputStream(), "UTF-8"), type);
	}
	
	String getSimpleRaw() throws IOException {
		try (Scanner scanner = new Scanner(inputStream())) {
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
	
}
