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

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

/**
 * Helper class for configuration testing
 * 
 * @author A248
 *
 */
public class ConfigTestingHelper {

	private ConfigTestingHelper() {}
	
	/**
	 * Fails the test if the result definition of the specified result is not
	 * a success
	 * 
	 * @param result the config result to check
	 */
	public static void assertSuccess(ConfigResult result) {
		if (!result.getResultDefinition().isSuccess()) {
			fail(result.getException());
		}
	}
	
	/*private static void copyOrFail(Path source, Path dest) {
		var writeOptions = Set.of(
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		try (FileChannel fileChannel = FileChannel.open(dest, writeOptions);
				ReadableByteChannel sourceChannel = FileChannel.open(source, StandardOpenOption.READ)){

			fileChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
		} catch (IOException ex) {
			fail(ex);
		}
	}*/
	
	/**
	 * Copies a default resource or fails the test
	 * 
	 * @param defaultResource the default resource provider
	 * @param dest the destination path
	 */
	public static void copyOrFail(DefaultResourceProvider defaultResource, Path dest) {
		var writeOptions = Set.of(
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		try (FileChannel fileChannel = FileChannel.open(dest, writeOptions);
				InputStream inputStream = defaultResource.openStream();
				ReadableByteChannel sourceChannel = Channels.newChannel(inputStream)){

			fileChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
		} catch (IOException ex) {
			fail(ex);
		}
	}
	
}
