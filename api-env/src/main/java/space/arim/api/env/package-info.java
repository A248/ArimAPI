/**
 * The base package defines {@link space.arim.api.env.PlatformHandle}, a useful interface for interacting with platform
 * specific operations. <br>
 * <br>
 * <b>Implementations</b> <br>
 * There are provided implementations of {@code PlatformHandle} for the Bukkit, Bungee, and Velocity platforms,
 * located within their respective packages. <br>
 * <br>
 * <b>Null handling</b> <br>
 * Unless stated otherwise, methods and constructors do not accept null values and may throw
 * {@code NullPointerException} if null values are passed. <br>
 * <br>
 * <b>Thread safety</b> <br>
 * Unless stated otherwise, all classes are immutable and thread safe.
 * 
 */
package space.arim.api.env;
