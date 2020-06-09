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

/**
 * A pure wrapper for geo-ip information
 * 
 * @author A248
 *
 */
public class GeoIpInfo {
	
	public final String address;
	public final String country_code;
	public final String country_name;
	public final String region_code;
	public final String region_name;
	public final String city;
	public final String zip;
	public final double latitude;
	public final double longitude;
	
	/**
	 * Creates with the following details. There are no requirements on these parameters.
	 * Any may be considered optional.
	 * 
	 * @param address the address
	 * @param country_code the country code
	 * @param country_name the country name
	 * @param region_code the region code
	 * @param region_name the region name
	 * @param city the city
	 * @param zip the zip code
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public GeoIpInfo(String address, String country_code, String country_name, String region_code, String region_name, String city, String zip, double latitude, double longitude) {
		this.address = address;
		this.country_code = country_code;
		this.country_name = country_name;
		this.region_code = region_code;
		this.region_name = region_name;
		this.city = city;
		this.zip = zip;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "GeoIpInfo [address=" + address + ", country_code=" + country_code + ", country_name=" + country_name
				+ ", region_code=" + region_code + ", region_name=" + region_name + ", city=" + city + ", zip=" + zip
				+ ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country_code == null) ? 0 : country_code.hashCode());
		result = prime * result + ((country_name == null) ? 0 : country_name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((region_code == null) ? 0 : region_code.hashCode());
		result = prime * result + ((region_name == null) ? 0 : region_name.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GeoIpInfo)) {
			return false;
		}
		GeoIpInfo other = (GeoIpInfo) obj;
		if (address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (country_code == null) {
			if (other.country_code != null) {
				return false;
			}
		} else if (!country_code.equals(other.country_code)) {
			return false;
		}
		if (country_name == null) {
			if (other.country_name != null) {
				return false;
			}
		} else if (!country_name.equals(other.country_name)) {
			return false;
		}
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) {
			return false;
		}
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) {
			return false;
		}
		if (region_code == null) {
			if (other.region_code != null) {
				return false;
			}
		} else if (!region_code.equals(other.region_code)) {
			return false;
		}
		if (region_name == null) {
			if (other.region_name != null) {
				return false;
			}
		} else if (!region_name.equals(other.region_name)) {
			return false;
		}
		if (zip == null) {
			if (other.zip != null) {
				return false;
			}
		} else if (!zip.equals(other.zip)) {
			return false;
		}
		return true;
	}
	
}
