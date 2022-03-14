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

package space.arim.api.util.testing.test;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
import space.arim.api.util.testing.InjectableConstructor;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectableConstructorTest {

    @Test
    public void findConstructor() throws NoSuchMethodException {
        assertEquals(
                SubjectOne.class.getConstructor(),
                new InjectableConstructor(SubjectOne.class).findConstructor());
    }

    public static class SubjectOne {

        @Inject
        public SubjectOne() {}
    }

    @Test
    public void simpleInjectableParameters() {
        assertEquals(
                Set.of(Integer.class, String.class),
                new InjectableConstructor(SubjectTwo.class).injectableParameters());
    }

    public static class SubjectTwo {

        @Inject
        public SubjectTwo(Integer a, String b) {}
    }

    @Test
    public void providedInjectableParameters() {
        assertEquals(
                Set.of(Integer.class, String.class),
                new InjectableConstructor(SubjectThree.class).injectableParameters());
    }

    public static class SubjectThree {

        @Inject
        public SubjectThree(Integer a, Provider<String> b) {}
    }
}
