/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

import java.io.IOException;
import java.io.InputStream;

/**
 * The source of some default configuration, which may be accessed as an {@link InputStream}
 * 
 * @author A248
 *
 */
public interface DefaultResourceProvider {

	/**
	 * Opens an input stream providing the data of the default resource. <br>
	 * (The caller is responsible for closing the stream)
	 * 
	 * @return an open input stream providing the default resource
	 * @throws IOException if an IO error occurred
	 */
	InputStream openStream() throws IOException;
	
}
