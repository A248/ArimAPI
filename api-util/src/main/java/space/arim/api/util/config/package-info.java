/**
 * A multi-platform configuration utility whose primary interface is {@link Config}. <br>
 * This config framework will only throw exceptions where documented, and most of such are subclasses of
 * {@link ConfigException}. <br>
 * <br>
 * <b>Users are generally encouraged to migrate to <i>arimapi-configure</i>, which is far more comprehensive</b>.
 * The classes in this package are in maintenance mode. See {@code space.arim.api.configure} for more details
 * on the recommended alternative. <br>
 * <br>
 * Note that <i>this</i> package requires SnakeYAML to be present on the classpath.
 * 
 * @deprecated This framework is deprecated for removal. <b>The recommended alternative is
 * <a href="https://github.com/A248/DazzleConf">DazzleConf</a></b>, a typesafe configuration library
 * by the same author of this package, which improves on many of the design mistakes in this framework.
 */
package space.arim.api.util.config;