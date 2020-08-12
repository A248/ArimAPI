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

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.DefaultResourceProvider;
import space.arim.api.configure.ValueTransformer;

public abstract class AbstractConfiguration extends BaseAbstractConfiguration {

	private final Executor executor;
	private final ConfigSerialiser serialiser;
	private final List<? extends ValueTransformer> transformers;
	
	protected AbstractConfiguration(DefaultResourceProvider defaultResource, Executor executor,
			ConfigSerialiser serialiser, List<? extends ValueTransformer> transformers) {
		super(defaultResource);
		this.executor = executor;
		this.serialiser = serialiser;
		this.transformers = List.copyOf(transformers);
	}
	
	@Override
	public Executor getExecutor() {
		return executor;
	}

	@Override
	public ConfigSerialiser getSerialiser() {
		return serialiser;
	}

	protected abstract void acceptRead(ConfigData successfulReadData);
	
	@Override
	public CompletableFuture<ConfigReadResult> readConfig(Path source) {
		return serialiser.readConfig(source, executor, transformers).thenApply((result) -> {
			if (result.getResultDefinition() == ConfigReadResult.ResultType.SUCCESS) {
				acceptRead(result.getReadData());
			}
			return result;
		});
	}

}
