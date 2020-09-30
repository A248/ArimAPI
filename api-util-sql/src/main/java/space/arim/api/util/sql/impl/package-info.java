/**
 * Classes in this package are used to help implement the SQL framework {@link space.arim.api.util.sql}.
 * They mostly consist of simple, boilerplate implementations for closable interfaces. <br>
 * <br>
 * The general form of the name of classes in this package is one which begins with the implemented interface's name,
 * and ends with "WithPreparedStatement", "WithPreparedStatementAndConnection", or "WithConnection". In any such case,
 * the accompanying objects (of the types described after the "With", such as PreparedStatement) will be closed when
 * the overall interface is closed. These accompanying objects must be passed to the constructor of the implementing class. <br>
 * <br>
 * Since it must be guaranteed that the classes in this package close all of their underlying resources, {@link AutoCloseable#close()}
 * is explicitly implemented in each. <br>
 * Furthermore, for additional consistency and clarity, resources are always closed starting with ResultSets, then PreparedStatements,
 * then Connections. Arrays of any type of object are closed in reverse order. <br>
 * <br>
 * Also, closing objects bundled with a Connection will first commit the connection if auto-commit is disabled.
 * 
 * @deprecated See deprecation of {@link space.arim.api.util.sql} (this entire framework is deprecated)
 */
package space.arim.api.util.sql.impl;