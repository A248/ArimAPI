/**
 * The base package defines {@link PlatformHandle}, a useful interface for interacting with platform
 * specific operations. <br>
 * <br>
 * <b>Implementations</b> <br>
 * There 4 provided implementations of {@code PlatformHandle} for the Bukkit, Bungee, Sponge, and Velocity platforms.
 * Most of the features they implement utilise more specific feature implementations. For example,
 * {@link space.arim.api.env.realexecutor} is used to implement {@link PlatformHandle#getRealExecutorFinder()}. <br>
 * <br>
 * Each implementation supports the following resource types in its method
 * {@link PlatformHandle#hookPlatformResource(space.arim.omnibus.resourcer.Resourcer, Class)}: <br>
 * * {@link FactoryOfTheFuture} <br>
 * * {@link EnhancedExecutor} <br>
 * <br>
 * It is recommended, but not required, for {@code PlatformHandle} implementations to similarly support such resource classes.
 * 
 */
package space.arim.api.env;

import space.arim.omnibus.util.concurrent.EnhancedExecutor;
import space.arim.omnibus.util.concurrent.FactoryOfTheFuture;
