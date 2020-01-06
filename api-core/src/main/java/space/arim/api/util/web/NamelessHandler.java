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
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import space.arim.universal.util.exception.HttpStatusException;

import space.arim.api.util.StringsUtil;

public class NamelessHandler {
	
	private final String baseUrl;
	
	public NamelessHandler(String host, String key) {
		baseUrl = host + "/index.php?route=/api/v2/" + key + "/";
	}
	
	private Map<String, Object> postParams(String action, String...parameters) throws HttpStatusException, SenderException {
		try (SenderConnection conn = new SenderConnection(baseUrl + action)) {
			conn.setProperty("Content-Length", Integer.toString(parameters.length));
			conn.setProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			conn.post(parameters);
			return conn.response();
		} catch (IOException ex) {
			throw new SenderException("Could not connect to URL!", ex);
		}
	}
	
	/**
	 * Updates a player's username on the NamelessMC site.
	 * 
	 * @param uuid the player's unique id
	 * @param name the player's current name
	 * @return true if successful
	 * @throws HttpStatusException if an http response code other than 200 is returned
	 * @throws SenderException if a miscellaneous connection problem occured
	 */
	public boolean updateUsername(UUID uuid, String name) throws HttpStatusException, SenderException {
		Map<String, Object> response = postParams("updateUsername", (new ParameterBuilder()).add("uuid", Objects.requireNonNull(uuid, "UUID must not be null!").toString().replace("-", "")).add("username", StringsUtil.requireNonEmpty(name, "Name must not be null!")).getParams());
		return !response.containsKey("error") || !((Boolean) response.get("error"));
	}
	
	/**
	 * Updates a player's group on the NamelessMC site.
	 * 
	 * @param uuid
	 * @param groupId
	 * @return true if successful
	 * @throws HttpStatusException if an http response code other than 200 is returned
	 * @throws SenderException if a miscellaneous connection problem occured
	 */
	public boolean setGroup(UUID uuid, String groupId) throws HttpStatusException, SenderException {
		Map<String, Object> response = postParams("setGroup", (new ParameterBuilder()).add("uuid", Objects.requireNonNull(uuid, "UUID must not be null!")).add("group_id", StringsUtil.requireNonEmpty(groupId, "Group id must not be null!")).getParams());
		return !response.containsKey("error") || !((Boolean) response.get("error"));
	}
	
	
	
}
