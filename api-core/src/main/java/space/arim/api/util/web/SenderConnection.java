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

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import space.arim.universal.util.exception.HttpStatusException;
import space.arim.universal.util.web.HttpStatus;

import space.arim.api.util.CommonInstancesUtil;

public class SenderConnection extends AbstractConnection {
	
	private Map<String, Object> response;
	
	SenderConnection(String url) {
		super(url);
	}
	
	@SuppressWarnings("unchecked")
	SenderConnection post(String...parameters) throws IOException, HttpStatusException, SenderException {
		open();
		connection().setRequestMethod("POST");
		setProperties();
		connection().setDoOutput(true);
		try (DataOutputStream outputStream = new DataOutputStream(connection().getOutputStream()); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))) {
			writer.write(String.join("&", parameters));
			writer.flush();
		}
		HttpStatus status = HttpStatus.fromCode(connection().getResponseCode());
		if (status != HttpStatus.OK) {
			throw new HttpStatusException(status);
		}
		try (InputStreamReader reader = new InputStreamReader(inputStream(), "UTF-8")) {
			response = CommonInstancesUtil.GSON.fromJson(reader, Map.class);
		} catch (JsonSyntaxException | JsonIOException ex) {
			throw new SenderException("Could not parse JSON response!", ex);
		}
		return this;
	}
	
	Map<String, Object> response() {
		return response;
	}
	
}
