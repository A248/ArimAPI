/**
 * A framework for SQL usage aiming at simplifying queries and cleaner code. <br>
 * <br>
 * Within all classes in this package, no try/catch statements are ever made. All exceptions are relayed to the caller. <br>
 * It is intended that the caller may execute SQL queries, using the primary interface {@link SqlBackend}, with
 * a single try-with-resources block. <br>
 * <br>
 * Since 0.13.0, every SQL-related method in this framework throws {@link java.sql.SQLException SQLException}, so that
 * implementations have maximum flexibility in throwing such exceptions.
 * 
 */
package space.arim.api.util.sql;
