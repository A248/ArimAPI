/**
 * Establishes platform-specific conventions for obtaining {@link Registry}. Once such a {@code Registry}
 * is obtained, platform-independent services may be queried. <br>
 * <br>
 * Moreover, the classes in this package should used by plugins responsible for adding this library to the classpath.
 * In practice, these plugins are: <br>
 * * The ArimAPI plugin itself <br>
 * * Plugins depending on ArimAPI which manage their dependencies by downloading them at runtime,
 * and which download ArimAPI
 * 
 */
package space.arim.api.env.convention;

import space.arim.universal.registry.Registry;