/**
 * <i><b>This framework is in maintenance mode and will soon be deprecated. </b>The recommended alternative is
 * <a href="https://github.com/A248/DazzleConf">DazzleConf</a>, a typesafe configuration library
 * by the same author of this package.</i> <br>
 * <br>
 * A configuration framework designed to load values, copy defaults, update configurations,
 * and write values, with full thread safety. <br>
 * <br>
 * Configuration values are read from the filesystem and written back to it using a {@link ConfigSerialiser}.
 * Such config serialisers are intended to be implemented for specific config formats. <br>
 * <br>
 * Operations involving IO are returned as a {@link CompletableFuture} of some sort of {@link ConfigResult}.
 * Since such operations may succeed or fail, the config result indicates success or failure as well
 * as any other resulting objects. <br>
 * <br>
 * Configuration data is only ever supposed to be modified when it is read or written. To this effect,
 * {@link ValueTransformer}s may be used to modify values on creation. {@link ConfigSerialisable}
 * should be used to change values on reserialisation. <br>
 * <br>
 * Most programmers will want to use the {@link ConfigurationBuilder} to build a {@link Configuration}
 * implementation from the {@code space.arim.api.configure.configs} package.
 * 
 */
package space.arim.api.configure;

import java.util.concurrent.CompletableFuture;

import space.arim.api.configure.configs.ConfigurationBuilder;