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
package space.arim.api.configure.configs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import space.arim.api.configure.ConfigData;
import space.arim.api.configure.ConfigReadResult;
import space.arim.api.configure.ConfigSerialiser;
import space.arim.api.configure.Configuration;
import space.arim.api.configure.JarResources;
import space.arim.api.configure.ValueTransformer;

/**
 * Builder for the {@link Configuration} implementations in this package, {@link PairedConfig}
 * and {@link MergingConfig}. <br>
 * <br>
 * The required details of this builder which must be set before building a {@code Configuration}
 * are the default resource path, an executor, and a config serialiser. Adding value transformers is optional.
 * If these 3 requirements are not set when calling build methods, {@link IllegalArgumentException} is thrown. <br>
 * <br>
 * Having met such requirements, a {@code CompletableFuture} yielding a {@code Configuration} instance is returned.
 * The future will be completed exceptionally with {@code IllegalArgumentException} if the default config data
 * could not be copied from the jar resource identified by the default resource path, or the serialiser
 * itself was unable to parse or read values. <br>
 * <br>
 * If the method with an {@code Executor} is used, that executor will be used for specifically copying the default values.
 * However, the executor of the builder, which is used for creating the resulting configuration, is still required.
 * 
 * @author A248
 *
 */
public class ConfigurationBuilder {

	private Path defaultResource;
	private Executor executor;
	private ConfigSerialiser serialiser;
	private final List<ValueTransformer> transformers = new ArrayList<>();
	
	/**
	 * Sets the default resource path of this builder to the specified one. <br>
	 * <br>
	 * To obtain such paths, {@link JarResources} may be used.
	 * 
	 * @param defaultResource the default resource path
	 * @return this builder
	 */
	public ConfigurationBuilder defaultResource(Path defaultResource) {
		this.defaultResource = defaultResource;
		return this;
	}
	
	/**
	 * Sets the executor of this builder to the specified one
	 * 
	 * @param executor the executor
	 * @return this builder
	 */
	public ConfigurationBuilder executor(Executor executor) {
		this.executor = executor;
		return this;
	}
	
	/**
	 * Sets the config serialiser of this builder to the specified one
	 * 
	 * @param serialiser the config serialiser
	 * @return this builder
	 */
	public ConfigurationBuilder serialiser(ConfigSerialiser serialiser) {
		this.serialiser = serialiser;
		return this;
	}
	
	/**
	 * Adds the specified value transformer to this builder
	 * 
	 * @param transformer the value transformer
	 * @return this builder
	 * @throws NullPointerException if the specified transformer is null
	 */
	public ConfigurationBuilder addTransformer(ValueTransformer transformer) {
		transformers.add(Objects.requireNonNull(transformer, "Transformer"));
		return this;
	}
	
	/**
	 * Adds the specified value transformers to this builder
	 * 
	 * @param transformers the value transformers
	 * @return this builder
	 * @throws NullPointerException if any transformer is null
	 */
	public ConfigurationBuilder addTransformers(ValueTransformer...transformers) {
		this.transformers.addAll(List.of(transformers)); // implicit null check
		return this;
	}
	
	/**
	 * Adds the specified value transformers to this builder
	 * 
	 * @param transformers the value transformers
	 * @return this builder
	 * @throws NullPointerException if any transformer is null
	 */
	public ConfigurationBuilder addTransformers(Collection<? extends ValueTransformer> transformers) {
		this.transformers.addAll(List.copyOf(transformers)); // implicit null check
		return this;
	}
	
	private static void checkNonNull(Object value, String name) {
		if (value == null) {
			throw new IllegalArgumentException(name + " must be specified");
		}
	}
	
	private static CompletableFuture<ConfigData> buildConfig(Path defaultResource, Executor executor, ConfigSerialiser serialiser,
			List<ValueTransformer> transformers) {
		checkNonNull(defaultResource, "Default resource path");
		checkNonNull(executor, "Executor");
		checkNonNull(serialiser, "Config serialiser");
		return serialiser.readConfig(defaultResource, executor, transformers).thenApply((readResult) -> {
			if (readResult.getResultDefinition() != ConfigReadResult.ResultType.SUCCESS) {
				throw new IllegalArgumentException(readResult.getException());
			}
			return readResult.getReadData();
		});
	}
	
	/**
	 * Creates a {@link PairedConfig} configuration, by first loading the default config data
	 * from the resource class and name specified in this builder. <br>
	 * <br>
	 * If for some reason the default config data could not be extracted from the jar resource,
	 * the returned future will be completed with {@code IllegalArgumentException}
	 * 
	 * @return a completable future which yields a {@link PairedConfig}
	 * @throws IllegalArgumentException if the resource class, resource name, or config serialiser is not set
	 */
	public CompletableFuture<PairedConfig> buildPairedConfig() {
		Path defaultResource = this.defaultResource;
		Executor executor = this.executor;
		ConfigSerialiser serialiser = this.serialiser;
		List<ValueTransformer> transformers = List.copyOf(this.transformers);
		return buildConfig(defaultResource, executor, serialiser, transformers).thenApply((configData) -> {
			return new PairedConfig(defaultResource, executor, serialiser, transformers, configData);
		});
	}
	
	/**
	 * Creates a {@link MergingConfig} configuration, by first loading the default config data
	 * from the resource class and name specified in this builder. <br>
	 * <br>
	 * If for some reason the default config data could not be extracted from the jar resource,
	 * the returned future will be completed with {@code IllegalArgumentException}
	 * 
	 * @return a completable future which yields a {@link MergingConfig}
	 * @throws IllegalArgumentException if the resource class, resource name, or config serialiser is not set
	 */
	public CompletableFuture<MergingConfig> buildMergingConfig() {
		Path defaultResource = this.defaultResource;
		Executor executor = this.executor;
		ConfigSerialiser serialiser = this.serialiser;
		List<ValueTransformer> transformers = List.copyOf(this.transformers);
		return buildConfig(defaultResource, executor, serialiser, transformers).thenApply((configData) -> {
			return new MergingConfig(defaultResource, executor, serialiser, transformers, configData);
		});
	}
	
}
