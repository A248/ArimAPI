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
package space.arim.api.configure.impl;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import space.arim.api.configure.ConfigCopyResult;
import space.arim.api.configure.Configuration;

public abstract class BaseAbstractConfiguration implements Configuration {

	private final Path defaultResource;
	
	protected BaseAbstractConfiguration(Path defaultResource) {
		this.defaultResource = defaultResource;
	}
	
	@Override
	public CompletableFuture<ConfigCopyResult> saveDefaultConfig(Path destination) {
		if (Files.exists(destination)) {
			return CompletableFuture.completedFuture(new SimpleConfigCopyResult(ConfigCopyResult.ResultType.ALREADY_EXISTS, null));
		}
		return CompletableFuture.supplyAsync(() -> {

			var writeOptions = Set.of(
					StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
			try (FileChannel fileChannel = FileChannel.open(destination, writeOptions);
					ReadableByteChannel sourceChannel = FileChannel.open(defaultResource, StandardOpenOption.READ)){

				fileChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
			} catch (IOException ex) {
				return new SimpleConfigCopyResult(ConfigCopyResult.ResultType.FAILURE_COPYING, ex);
			}
			ConfigCopyResult result = new SimpleConfigCopyResult(ConfigCopyResult.ResultType.SUCCESS, null);
			return result;

		}, getSerialiser().getExecutor()).exceptionally((ex) -> {
			return new SimpleConfigCopyResult(ConfigCopyResult.ResultType.UNKNOWN_ERROR, getOrWrapException(ex));
		});
	}
	
	private static Exception getOrWrapException(Throwable ex) {
		return AbstractConfigSerialiser.getOrWrapException(ex);
	}
	
}
