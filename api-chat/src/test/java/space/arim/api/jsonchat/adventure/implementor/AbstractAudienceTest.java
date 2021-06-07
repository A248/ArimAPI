/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
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

package space.arim.api.jsonchat.adventure.implementor;

import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractAudienceTest {

    /*
     * Checks that all empty methods in Audience are overridden by AbstractAudience
     * with either a re-abstraction or non-empty re-implementation
     */
    @TestFactory
    public Stream<DynamicNode> noEmptyMethods() {
        return Arrays.stream(AbstractAudience.class.getMethods()).filter((method) -> {
            if (Modifier.isAbstract(method.getModifiers())) {
                return false;
            }
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.equals(Audience.class)) {
                try {
                    AbstractAudience.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    return false; // Skip this method - it is overridden in AbstractAudience
                } catch (NoSuchMethodException ex) {
                    return true; // Analyze
                }
            }
            return AbstractAudience.class.isAssignableFrom(declaringClass);
        }).map((method) -> {
            return DynamicTest.dynamicTest("For method " + method, () -> verifyNoEmptyMethods(method));
        });
    }

    private void verifyNoEmptyMethods(Method method) {
        String classResourceName = method.getDeclaringClass().getName().replace('.', '/') + ".class";
        byte[] classBytes;
        try (InputStream classStream = ClassLoader.getSystemResourceAsStream(classResourceName)) {
            assert classStream != null : "Missing class";
            classBytes = classStream.readAllBytes();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ClassContentVisitor visitor = new ClassContentVisitor(method);
        new ClassReader(classBytes).accept(visitor, 0);
        assertTrue(visitor.hasFoundMethodContent(), "Method is empty: " + method);
    }

    private static final class ClassContentVisitor extends ClassVisitor {

        private final Method method;
        private boolean foundMethodContent;

        ClassContentVisitor(Method method) {
            super(Opcodes.ASM9);
            this.method = method;
        }

        void foundMethodContent() {
            foundMethodContent = true;
        }

        boolean hasFoundMethodContent() {
            return foundMethodContent;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.equals(method.getName()) && Arrays.equals(Type.getArgumentTypes(descriptor), Type.getArgumentTypes(method))) {
                return new MethodContentVisitor(this);
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

    private static final class MethodContentVisitor extends MethodVisitor {

        private final ClassContentVisitor classContentVisitor;

        MethodContentVisitor(ClassContentVisitor classContentVisitor) {
            super(Opcodes.ASM9);
            this.classContentVisitor = classContentVisitor;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            classContentVisitor.foundMethodContent();
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

        @Override
        public void visitInsn(final int opcode) {
            if (opcode == Opcodes.ATHROW) {
                classContentVisitor.foundMethodContent();
            }
            super.visitInsn(opcode);
        }
    }
}
