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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import space.arim.universal.util.web.HttpStatus;
import space.arim.universal.util.web.HttpStatusException;

import space.arim.api.annotation.Blocking;
import space.arim.api.uuid.UUIDUtil;

public final class FetcherUtil {
	
	private static final String ASHCON_API = "https://api.ashcon.app/mojang/v2/user/";
	private static final String MOJANG_API_FROM_NAME = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String MOJANG_API_FROM_UUID = "https://api.mojang.com/user/profiles/";
	
    private static final String SPIGOT_UPDATE_API = "https://api.spigotmc.org/legacy/update.php?resource=";
    
    private static final RateLimitedService IPSTACK = new RateLimitedService("IpStack", "http://api.ipstack.com/$ADDR?access_key=$KEY&fields=country_code,country_name,region_code,region_name,city,zip,latitude,longitude", 2592000L);
    private static final RateLimitedService FREEGEOIP = new RateLimitedService("FreeGeoIp", "https://freegeoip.app/json/$ADDR", 3600L);
    private static final RateLimitedService IPAPI = new RateLimitedService("IpApi", "https://ipapi.co/$ADDR/json/", 86400L);
    
	private FetcherUtil() {}
	
	private static <T> T getJsonFromUrl(final String url, Class<T> type) throws FetcherException, HttpStatusException {
		try (FetcherConnection conn = new FetcherConnection(url)) {
			return conn.connect().getJson(type);
		} catch (JsonSyntaxException | JsonIOException | IOException ex) {
			throw new FetcherException("Failed to connect to and parse JSON from " + url + " as " + type.getSimpleName(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getJsonMapFromUrl(String url) throws FetcherException, HttpStatusException {
		return getJsonFromUrl(url, Map.class);
	}
	
	private static Map<String, Object> getJsonMapFromAshcon(String player) throws FetcherException, HttpStatusException {
		return getJsonMapFromUrl(ASHCON_API + Objects.requireNonNull(player, "Name or UUID must not be null!"));
	}
	
	private static Map<String, Object> getJsonMapFromAshcon(UUID player) throws FetcherException, HttpStatusException {
		return getJsonMapFromAshcon(player.toString());
	}
	
	@Blocking
	public static UUID mojangApi(final String name) throws FetcherException, HttpStatusException {
		return UUID.fromString(UUIDUtil.expand(getJsonMapFromUrl(MOJANG_API_FROM_NAME + Objects.requireNonNull(name, "Name must not be null!")).get("id").toString()));
	}
	
	@Blocking
	public static String mojangApi(final UUID playeruuid) throws FetcherException, HttpStatusException {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] names = getJsonFromUrl(MOJANG_API_FROM_UUID + Objects.requireNonNull(playeruuid, "UUID must not be null!").toString().replace("-", "") + "/names", Map[].class);
		return names[names.length - 1].get("name").toString();
	}
	
	@Blocking
	public static UUID ashconApi(final String name) throws FetcherException, HttpStatusException {
		return UUID.fromString(getJsonMapFromAshcon(name).get("uuid").toString());
	}
	
	@Blocking
	public static String ashconApi(final UUID playeruuid) throws FetcherException, HttpStatusException {
		return getJsonMapFromAshcon(playeruuid).get("username").toString();
	}
	
	@Blocking
	public static SortedSet<Entry<String, String>> ashconNameHistory(final UUID playeruuid) throws FetcherException, HttpStatusException {
		TreeSet<Entry<String, String>> results = new TreeSet<Entry<String, String>>((entry1, entry2) -> {
			if (entry1.getValue().isEmpty()) {
				return 1;
			} else if (entry2.getValue().isEmpty()) {
				return -1;
			}
			String[] time1 = entry1.getValue().split("-");
			String[] time2 = entry2.getValue().split("-");
			int year1 = Integer.valueOf(time1[0]);
			int year2 = Integer.valueOf(time2[0]);
			if (year1 == year2) {
				int month1 = Integer.valueOf(time1[1]);
				int month2 = Integer.valueOf(time2[1]);
				return (month1 == month2) ? (Integer.valueOf(time1[2]) - Integer.valueOf(time2[2])) : (month1 - month2);
			}
			return year1 - year2;
		});
		Map<String, Object> map = getJsonMapFromAshcon(playeruuid);
		@SuppressWarnings("unchecked")
		List<Map<String, String>> history = (List<Map<String, String>>) map.get("username_history");
		for (Map<String, String> entry : history) {
			String changedAt = entry.get("changed_at");
			results.add(new DistinctEntry<String, String>(entry.get("username"), (changedAt == null) ? "" : changedAt.substring(0, 10)));
		}
		return results;
	}
	
	@Blocking
	public static String getLatestSpigotPluginVersion(final int resourceId) throws FetcherException, HttpStatusException {
		final String url = SPIGOT_UPDATE_API + resourceId;
		try (FetcherConnection conn = new FetcherConnection(url)) {
			return conn.connect().getSimpleRaw();
		} catch (IOException ex) {
			throw new FetcherException("Could not connect to url " + url, ex);
		}
	}
	
	@Blocking
	public static GeoIpInfo ipStack(final String address, final String key) throws FetcherException, RateLimitException, HttpStatusException {
		final String url = IPSTACK.getUrl(address).replace("$KEY", Objects.requireNonNull(key, "Key must not be null!"));
		try {
			Map<String, Object> json = getJsonMapFromUrl(url);
			return new GeoIpInfo(address, json.get("country_code").toString(), json.get("country_name").toString(), json.get("region_code").toString(), json.get("region_name").toString(), json.get("city").toString(), json.get("zip").toString(), Double.parseDouble(json.get("latitude").toString()), Double.parseDouble(json.get("longitude").toString()));
		} catch (HttpStatusException ex) {
			if (ex.status == HttpStatus.UNASSIGNED_104) {
				IPSTACK.expire();
			}
			throw ex;
		} catch (NumberFormatException ex) {
			throw new FetcherException("Could not parse JSON from " + url, ex);
		}
	}
	
	@Blocking
	public static GeoIpInfo freeGeoIp(final String address) throws FetcherException, RateLimitException, HttpStatusException {
		final String url = FREEGEOIP.getUrl(address);
		try {
			Map<String, Object> json = getJsonMapFromUrl(url);
			return new GeoIpInfo(address, json.get("country_code").toString(), json.get("country_name").toString(), json.get("region_code").toString(), json.get("region_name").toString(), json.get("city").toString(), json.get("zip_code").toString(), Double.parseDouble(json.get("latitude").toString()), Double.parseDouble(json.get("longitude").toString()));
		} catch (HttpStatusException ex) {
			if (ex.status == HttpStatus.FORBIDDEN) {
				FREEGEOIP.expire();
			}
			throw ex;
		} catch (NumberFormatException ex) {
			throw new FetcherException("Could not parse GeoIpInfo from url " + url, ex);
		}
	}
	
	@Blocking
	public static GeoIpInfo ipApi(final String address) throws FetcherException, RateLimitException, HttpStatusException {
		final String url = IPAPI.getUrl(address);
		try {
			Map<String, Object> json = getJsonMapFromUrl(url);
			return new GeoIpInfo(address, json.get("country").toString(), json.get("country_name").toString(), json.get("region_code").toString(), json.get("region").toString(), json.get("city").toString(), json.get("postal").toString(), Double.parseDouble(json.get("latitude").toString()), Double.parseDouble(json.get("longitude").toString()));
		} catch (HttpStatusException ex) {
			if (ex.status == HttpStatus.TOO_MANY_REQUESTS) {
				IPAPI.expire();
			}
			throw ex;
		} catch (NumberFormatException ex) {
			throw new FetcherException("Could not parse GeoIpInfo from url " + url, ex);
		}
	}
	
}

class DistinctEntry<K, V> implements Entry<K, V> {
	
	private final K key;
	private final V value;
	
	DistinctEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Entry<?, ?>) {
			Entry<?, ?> other = (Entry<?, ?>) object;
			return getKey().equals(other.getKey()) && getValue().equals(other.getValue());
		}
		return false;
	}
	
}
