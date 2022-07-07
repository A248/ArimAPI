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

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Assistant for working with constructors annotated with {@link Inject} in a certain class
 *
 */
public class InjectableConstructor {

    private final Class<?> subject;

    /**
     * Creates from a class to analyze
     *
     * @param subject the class to analyze
     */
    public InjectableConstructor(Class<?> subject) {
        this.subject = Objects.requireNonNull(subject, "subject");
    }

    /**
     * Finds the injectable constructor
     *
     * @return the injectable constructor
     */
    public Constructor<?> findConstructor() {
        return Arrays.stream(subject.getConstructors())
                .filter((ctor) -> ctor.isAnnotationPresent(Inject.class))
                .findAny()
                .orElseThrow();
    }

    /**
     * An injectable constructor is said to contain a parameter if either that parameter is
     * declared directly or a {@code Provider} of that parameter is declared. <br>
     * <br>
     * Therefore, this method will never return {@code Provider.class} but rather the provided
     * type itself.
     *
     * @return the injectable parameters
     */
    public Set<Class<?>> injectableParameters() {
        Set<Class<?>> injectableParameters = new HashSet<>();
        for (Type parameter : findConstructor().getGenericParameterTypes()) {
            injectableParameters.add(getInjectedType(parameter));
        }
        return injectableParameters;
    }

    private static Class<?> getInjectedType(Type parameter) {
        if (parameter instanceof Class) {
            return (Class<?>) parameter;
        }
        if (parameter instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter;
            Type rawType = parameterizedType.getRawType();
            if (Provider.class.equals(rawType)) {
                return getRawInjectedType(parameterizedType.getActualTypeArguments()[0]);
            } else {
                return (Class<?>) rawType;
            }
        }
        return getRawInjectedType(parameter);
    }

    private static Class<?> getRawInjectedType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof WildcardType) {
            return getRawInjectedType(((WildcardType) type).getUpperBounds()[0]);
        }
        if (type instanceof TypeVariable) {
            return getRawInjectedType(((TypeVariable<?>) type).getBounds()[0]);
        }
        if (type instanceof GenericArrayType) {
            // Assume this is some odd injection system where array members are injected
            return getRawInjectedType(((GenericArrayType) type).getGenericComponentType());
        }
        throw new UnsupportedOperationException(Type.class.getName() + " sub-class not supported: " + type.getClass());
    }

    /**
     * Verifies that the injectable constructor contains at least the given parameters
     *
     * @param parameters the parameter types the constructor should at least have
     * @throws AssertionError if the verification failed
     */
    public void verifyParametersContain(Set<Class<?>> parameters) {
        Set<Class<?>> actualParameters = injectableParameters();
        Set<Class<?>> leftovers = new HashSet<>(parameters);
        leftovers.removeAll(actualParameters);
        assertTrue(leftovers.isEmpty(),
                () -> "Expected parameters of " + subject.getName() + " constructor to contain " + leftovers);
    }

    /**
     * Scans the classpath/module path for all classes which are a subclass or implementing
     * class of the given class or interface. Filters the discovered subclasses by the
     * predicate. Then verifies that the injectable constructor's parameters contain at least
     * the filtered classes. <br>
     * <br>
     * If the given superclass is in a module, this will only scan for classes that are in the
     * same module.
     *
     * @param superclass the superclass
     * @param filter a filter on the subclasses, if a class does not pass the filter, it is not included
     *               in the check
     * @throws AssertionError if the verification failed
     */
    public void verifyParametersContainSubclassesOf(Class<?> superclass, Predicate<? super Class<?>> filter) {
        Set<Class<?>> scannedClasses = new SubClassesOf(superclass)
                .scan(subject.getModule(), filter);
        verifyParametersContain(scannedClasses);
    }

    /**
     * Scans the classpath/module path for all classes which are a subclass or implementing
     * class of the given class or interface. Then verifies that the injectable constructor's
     * parameters contain at least the discovered classes. <br>
     * <br>
     * If the given superclass is in a module, this will only scan for classes that are in the
     * same module.
     *
     * @param superclass the superclass
     * @throws AssertionError if the verification failed
     */
    public void verifyParametersContainSubclassesOf(Class<?> superclass) {
        verifyParametersContainSubclassesOf(superclass, (cls) -> true);
    }

}
