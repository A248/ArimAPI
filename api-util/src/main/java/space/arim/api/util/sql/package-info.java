/**
 * A framework for SQL usage aiming at simplifying queries and cleaner code. <br>
 * <br>
 * Within all classes in this package, no try/catch statements are ever made. All exceptions are relayed to the caller. <br>
 * It is intended that the caller may execute SQL queries, using the primary interface {@link SqlBackend}, with
 * a single try-with-resources block.
 * 
 */
package space.arim.api.util.sql;