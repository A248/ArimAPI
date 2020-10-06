/**
 * The base package defines {@link PlatformHandle}, a useful interface for interacting with platform
 * specific operations. <br>
 * <br>
 * <b>Implementations</b> <br>
 * There 4 provided implementations of {@code PlatformHandle} for the Bukkit, Bungee, Sponge, and Velocity platforms.
 * Most of the features they implement utilise more specific feature implementations. For example,
 * {@link space.arim.api.env.realexecutor} is used to implement {@link PlatformHandle#getRealExecutorFinder()}
 * 
 */
package space.arim.api.env;
