/* 
 * ArimAPI-configure
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * ArimAPI-configure is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ArimAPI-configure is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI-configure. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */
package space.arim.api.configure;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Utility class for locating JAR resources. <br>
 * <br>
 * Each method returns a {@link Path} created from the located resource. If the resource in the specified location
 * was not found or could not be converted to valid syntax, {@code IllegalArgumentException} is thrown. <br>
 * <br>
 * Note that slashes are automatically prepended to all resource names, to ensure they are considered absolute
 * per {@link Class#getResource(String)}.
 * 
 * @author A248
 *
 */
public final class JarResources {

	private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	
	private JarResources() {}
	
	private static Path forURL(URL url) {
		URI uri;
		try {
			uri = url.toURI();
		} catch (URISyntaxException ex) {
			throw new IllegalArgumentException(ex);
		}
		return Path.of(uri);
	}
	
	/**
	 * Gets a resource in the caller class, using {@link Class#getResource(String)}
	 * 
	 * @param resourceName the resource name
	 * @return a path pointing to the resource
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static Path forCallerClass(String resourceName) {
		Class<?> caller = WALKER.getCallerClass();
		return forClass(caller, resourceName);
	}
	
	/**
	 * Gets a resource in the specified class, using {@link Class#getResource(String)}
	 * 
	 * @param resourceClass the resource class
	 * @param resourceName the resource name
	 * @return a path pointing to the resource
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static Path forClass(Class<?> resourceClass, String resourceName) {
		return forURL(resourceClass.getResource('/' + resourceName));
	}
	
	/**
	 * Gets a resource in the specified class loader, using {@link ClassLoader#getResource(String)}
	 * 
	 * @param classLoader the class loader
	 * @param resourceName the resource name
	 * @return a path pointing to the resource
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static Path forClassLoader(ClassLoader classLoader, String resourceName) {
		return forURL(classLoader.getResource('/' + resourceName));
	}
	
	/**
	 * Gets a resource in the specified module, using {@link ClassLoader#getResource(String)} and the
	 * class loader of the module
	 * 
	 * @param module the module
	 * @param resourceName the resource name
	 * @return a path pointing to the resource
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static Path forModule(Module module, String resourceName) {
		return forClassLoader(module.getClassLoader(), resourceName);
	}
	
}
