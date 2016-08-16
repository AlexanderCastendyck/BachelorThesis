package castendyck.inmemorycompiling;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

class MethodCallMatcher extends TypeSafeDiagnosingMatcher<ByteCode> {
    private final String expectedClassName;
    private String expectedMethodName;
    private String expectedCallTargetClass;
    private String expectedCallTargetMethod;

    private MethodCallMatcher(String className) {
        this.expectedClassName = className;
    }

    public static MethodCallMatcher containsAClass(String className) {
        return new MethodCallMatcher(className);
    }

    public MethodCallMatcher havingAMethodName(String methodName) {
        this.expectedMethodName = methodName;
        return this;
    }

    public MethodCallMatcher calling(String targetClass, String targetMethod) {
        this.expectedCallTargetClass = targetClass;
        this.expectedCallTargetMethod = targetMethod;
        return this;
    }

    @Override
    protected boolean matchesSafely(ByteCode byteCode, Description description) {
        if (!byteCode.getRelatedClassName().equals(expectedClassName)) {
            return false;
        }

        final InputStream inputStream = byteCode.getInputStream();
        final SimpleMethodVisitor methodVisitor = new SimpleMethodVisitor(expectedCallTargetClass, expectedCallTargetMethod);
        final SimpleClassVisitor classVisitor = new SimpleClassVisitor(expectedMethodName, methodVisitor);
        try {
            final ClassReader classReader = new ClassReader(inputStream);
            classReader.accept(classVisitor, 0);
            return methodVisitor.expectedCallFound;
        } catch (IOException e) {
            throw new AssertionError("error while parsing bytecode", e);
        }
    }

    @Override
    public void describeTo(Description description) {

    }

    private class SimpleClassVisitor extends ClassVisitor {
        private final String methodName;
        private final SimpleMethodVisitor methodVisitor;

        private SimpleClassVisitor(String methodName, SimpleMethodVisitor methodVisitor) {
            super(Opcodes.ASM4);
            this.methodName = methodName;
            this.methodVisitor = methodVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals(methodName)) {
                return methodVisitor;
            } else {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        }
    }

    private class SimpleMethodVisitor extends MethodVisitor {
        private final String expectedCalledClass;
        private final String expectedCalledFunction;
        private boolean expectedCallFound;

        private SimpleMethodVisitor(String expectedCalledClass, String expectedCalledFunction) {
            super(Opcodes.ASM4);
            this.expectedCalledClass = expectedCalledClass;
            this.expectedCalledFunction = expectedCalledFunction;
            this.expectedCallFound = false;
        }

        @Override
        public void visitMethodInsn(int i, String owner, String name, String desc, boolean isInterface) {
            owner  = owner.replaceAll("/", "\\.");
            if (owner.equals(expectedCalledClass) && name.equals(expectedCalledFunction)) {
                expectedCallFound = true;
            }
        }
    }
}
