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

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigComment;
import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.ConfigWriteResult;
import space.arim.api.configure.ValueTransformer;

/**
 * Base class easing implementation of {@link ConfigSerialiser}
 * 
 * @author A248
 *
 */
public abstract class AbstractConfigSerialiser implements ConfigSerialiser {
	
	protected AbstractConfigSerialiser() {

	}
	
	static Exception getOrWrapException(Throwable value) {
		if (value instanceof Exception) {
			return (Exception) value;
		}
		return new CompletionException(value);
	}
	
	protected abstract ConfigReadResult readConfig0(Path source, List<ValueTransformer> transformers);

	@Override
	public CompletableFuture<ConfigReadResult> readConfig(Path source, Executor executor, List<? extends ValueTransformer> transformers) {
		// Early null check of transformers
		@SuppressWarnings("unchecked")
		List<ValueTransformer> transfos = (List<ValueTransformer>) List.copyOf(transformers);

		// Early escape nonexistent file
		if (!Files.exists(source)) {
			return CompletableFuture.completedFuture(new SimpleConfigReadResult(ConfigReadResult.ResultType.FILE_NON_EXISTENT, new FileNotFoundException(), null));
		}

		return CompletableFuture.supplyAsync(() -> {
			return readConfig0(source, transfos);
		}, executor).exceptionally((ex) -> {
			return new SimpleConfigReadResult(ConfigReadResult.ResultType.UNKNOWN_ERROR, getOrWrapException(ex), null);
		});
	}

	protected abstract ConfigWriteResult writeConfig0(Path target, Map<String, Object> values, Map<String, List<ConfigComment>> comments);
	
	@Override
	public CompletableFuture<ConfigWriteResult> writeConfig(Path target, Executor executor, ConfigData data) {
		// Early null check of values and comments
		Map<String, Object> values = data.getValuesMap();
		Map<String, List<ConfigComment>> commentHeader = data.getCommentsMap();

		return CompletableFuture.supplyAsync(() -> {
			return writeConfig0(target, values, commentHeader);

		}, executor).exceptionally((ex) -> {
			return new SimpleConfigWriteResult(ConfigWriteResult.ResultType.UNKNOWN_ERROR, getOrWrapException(ex));
		});
	}

}
