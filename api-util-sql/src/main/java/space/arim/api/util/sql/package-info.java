/**
 * A framework for SQL usage aiming at simplifying queries and cleaner code. <br>
 * <br>
 * Within all classes in this package, few try/catch statements are ever made. It is intended that the caller
 * may execute SQL queries, using the primary interface {@link SqlBackend}, with a single try-with-resources block.
 * Every SQL-related method in this framework throws {@link java.sql.SQLException SQLException}, so that implementations have maximum flexibility in throwing such exceptions.
 * 
 * @deprecated This framework is deprecated for removal. The recommended alternative is either the
 * JOOQ or JDBI libraries.
 */
package space.arim.api.util.sql;
