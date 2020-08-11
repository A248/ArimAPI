/**
 * A framework for SQL usage aiming at simplifying queries and cleaner code. <br>
 * <br>
 * <b>This framework is now in maintenance mode. The preferred alternative is
 * <a href="https://github.com/A248/JdbCaesar">JdbCaesar</a></b> <br>
 * <br>
 * Within all classes in this package, few try/catch statements are ever made. It is intended that the caller
 * may execute SQL queries, using the primary interface {@link SqlBackend}, with a single try-with-resources block.
 * Every SQL-related method in this framework throws {@link java.sql.SQLException SQLException}, so that implementations have maximum flexibility in throwing such exceptions.
 * 
 */
package space.arim.api.util.sql;
