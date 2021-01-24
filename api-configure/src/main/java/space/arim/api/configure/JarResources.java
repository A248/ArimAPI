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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for locating JAR resources. <br>
 * <br>
 * Each method returns a {@link DefaultResourceProvider} created from the located resource. If the resource in
 * the specified location was not found or could not be converted to valid syntax, {@code IllegalArgumentException} is thrown.
 * 
 * @author A248
 *
 * @deprecated See deprecation of {@link space.arim.api.configure} (this entire framework is deprecated)
 */
@Deprecated(forRemoval = true)
public final class JarResources {

	private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
	
	private JarResources() {}
	
	/**
	 * Gets a resource in the caller class, using {@link Class#getResource(String)}. <br>
	 * <br>
	 * Note that slashes are automatically prepended to all resource names, to ensure they are considered absolute
	 * per {@code Class#getResource(String)}.
	 * 
	 * @param resourceName the resource name
	 * @return a default resource provider
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static DefaultResourceProvider forCallerClass(String resourceName) {
		Class<?> caller = WALKER.getCallerClass();
		return forClass(caller, resourceName);
	}
	
	/**
	 * Gets a resource in the specified class, using {@link Class#getResource(String)}. <br>
	 * <br>
	 * Note that slashes are automatically prepended to all resource names, to ensure they are considered absolute
	 * per {@code Class#getResource(String)}.
	 * 
	 * @param resourceClass the resource class
	 * @param resourceName the resource name
	 * @return a default resource provider
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static DefaultResourceProvider forClass(Class<?> resourceClass, String resourceName) {
		return forURL(resourceClass.getResource('/' + resourceName));
	}
	
	/**
	 * Gets a resource in the specified class loader, using {@link ClassLoader#getResource(String)}
	 * 
	 * @param classLoader the class loader
	 * @param resourceName the resource name
	 * @return a default resource provider
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static DefaultResourceProvider forClassLoader(ClassLoader classLoader, String resourceName) {
		return forURL(classLoader.getResource(resourceName));
	}
	
	/**
	 * Gets a resource in the specified module, using {@link ClassLoader#getResource(String)} and the
	 * class loader of the module
	 * 
	 * @param module the module
	 * @param resourceName the resource name
	 * @return a default resource provider
	 * @throws IllegalArgumentException if the resource could not be found or accessed
	 */
	public static DefaultResourceProvider forModule(Module module, String resourceName) {
		return forClassLoader(module.getClassLoader(), resourceName);
	}
	
	private static DefaultResourceProvider forURL(URL url) {
		return new ClassLoaderResourceProvider(url);
	}
	
	private static class ClassLoaderResourceProvider implements DefaultResourceProvider {

		private final URL url;
		
		ClassLoaderResourceProvider(URL url) {
			this.url = url;
		}
		
		@Override
		public InputStream openStream() throws IOException {
			return url.openStream();
		}
		
	}
	
}
