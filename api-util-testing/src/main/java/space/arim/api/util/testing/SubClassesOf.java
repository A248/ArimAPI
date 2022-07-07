/*
 * ArimAPI
 * Copyright © 2022 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */

package space.arim.api.util.testing;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Scans the classpath/module path for all classes which are a subclass or implementing class
 * of a given class or interface.
 */
public final class SubClassesOf {

	private final Class<?> superClass;

	/**
	 * Creates from a superclass to scan for
	 *
	 * @param superClass the superclass to scan for
	 */
	public SubClassesOf(Class<?> superClass) {
		this.superClass = Objects.requireNonNull(superClass);
	}

	/**
	 * Scans for all subclasses
	 *
	 * @return all subclasses
	 */
	public Set<Class<?>> scan() {
		return scan0(null, (cls) -> true);
	}

	/**
	 * Scans for all subclasses in a given module; if the module is unnamed, then all modules are used.
	 *
	 * @param module if a named module, only classes in this module will be scanned
	 * @return all subclasses in the given module
	 */
	public Set<Class<?>> scan(Module module) {
		return scan0(Objects.requireNonNull(module, "module"), (cls) -> true);
	}

	/**
	 * Scans for all subclasses in a given module; if the module is unnamed, then all modules are used. <br>
	 * Then filters the results.
	 *
	 * @param module if a named module, only classes in this module will be scanned
	 * @param filter a filter on the subclasses, if a class does not pass the filter, it is not included in the result
	 * @return the scanned subclasses
	 */
	public Set<Class<?>> scan(Module module, Predicate<? super Class<?>> filter) {
		return scan0(Objects.requireNonNull(module, "module"), Objects.requireNonNull(filter, "filter"));
	}

	private Set<Class<?>> scan0(Module module, Predicate<? super Class<?>> filter) {
		final ClassLoader ourClassLoader = getClass().getClassLoader();

		Set<Class<?>> scannedClasses;
		try (ScanResult scan = new ClassGraph()
				.enableClassInfo()
				.acceptModules((module == null || !module.isNamed()) ? "*" : module.getName())
				.scan()) {
			String superclassName = superClass.getName();
			ClassInfoList classInfoList = (superClass.isInterface()) ?
					scan.getClassesImplementing(superclassName) : scan.getSubclasses(superclassName);
			scannedClasses = classInfoList
					.getNames().stream()
					.map((name) -> {
						try {
							return Class.forName(name, false, ourClassLoader);
						} catch (ClassNotFoundException ex) {
							throw new IllegalStateException("Scanned class does not exist", ex);
						}
					})
					.filter(filter)
					.collect(Collectors.toUnmodifiableSet());
		}
		return scannedClasses;
	}
}
