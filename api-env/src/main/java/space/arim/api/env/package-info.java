/**
 * The base package defines {@link space.arim.api.env.PlatformHandle}, a useful interface for interacting with platform
 * specific operations. <br>
 * <br>
 * <b>Implementations</b> <br>
 * There are provided implementations of {@code PlatformHandle} for the Bukkit, Bungee, and Velocity platforms.
 * Most of the features they provide utilise more specific feature implementations. For example,
 * {@link space.arim.api.env.realexecutor} is used to implement
 * {@link space.arim.api.env.PlatformHandle#getRealExecutorFinder()}
 * 
 */
package space.arim.api.env;
