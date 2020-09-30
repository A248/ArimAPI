/**
 * A framework for SQL usage aiming at simplifying queries and cleaner code. <br>
 * <br>
 * Within all classes in this package, few try/catch statements are ever made. It is intended that the caller
 * may execute SQL queries, using the primary interface {@link SqlBackend}, with a single try-with-resources block.
 * Every SQL-related method in this framework throws {@link java.sql.SQLException SQLException}, so that implementations have maximum flexibility in throwing such exceptions.
 * 
 * @deprecated This framework is deprecated for removal. The recommended alternative is the
 * <a href="https://github.com/A248/JdbCaesar">JdbCaesar</a> library by the same author of this framework,
 * which has significant design improvements over this one. JdbCaesar is more powerful, more resilient,
 * and better structured.
 */
package space.arim.api.util.sql;
