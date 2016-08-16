package castendyck.inmemorycompiling;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static castendyck.inmemorycompiling.ByteCodeBuilder.aByteCode;
import static castendyck.inmemorycompiling.ClassMatcher.isAClassNamed;
import static castendyck.inmemorycompiling.FieldSourceCodeBuilder.sourceCodeForField;
import static castendyck.inmemorycompiling.MethodCallMatcher.containsAClass;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;

public class InMemoryCompilingTestIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testByteCodeForSingleClass_containsClassFile() throws Exception {
        final String className = "someClass";
        final ByteCodeBuilder byteCodeBuilder = aByteCode().forAClass(sourceCodeForAClass()
                .named(className));

        final List<ByteCode> byteCodes = act(byteCodeBuilder);

        assertThat(byteCodes, hasSize(1));
        final ByteCode byteCode = byteCodes.get(0);
        final Class<?> someClass = getClassFromByteCode(byteCode);
        assertThat(someClass.getName(), equalTo(className));
    }

    @Test
    public void testByteCodeForSingleClass_containsClassFileWithCorrectMethod() throws Exception {
        final String className = "someClass";
        final String methodName = "someMethod";
        final String methodAccess = "public";
        final ByteCodeBuilder byteCodeBuilder = aByteCode().forAClass(sourceCodeForAClass()
                .named(className)
                .withAMethod(sourceCodeForAMethod()
                        .named(methodName)
                        .withAccess(methodAccess)));


        final List<ByteCode> byteCodes = act(byteCodeBuilder);


        assertThat(byteCodes, hasSize(1));
        final ByteCode byteCode = byteCodes.get(0);
        final Class<?> someClass = getClassFromByteCode(byteCode);
        assertThat(someClass, isAClassNamed(className).containingMethod(methodName));

        final Method method = someClass.getMethod(methodName);
        assertTrue(Modifier.isPublic(method.getModifiers()));
    }

    @Test
    public void testByteCodeForSingleClass_containsMethodWithCorrectCall() throws Exception {
        final String className = "someClass";
        final String methodName = "someMethod";
        final String methodAccess = "public";
        final String targetName = "javax.tools.ToolProvider";
        final String targetMethod = "getSystemJavaCompiler";

        final ByteCodeBuilder byteCodeBuilder = aByteCode().forAClass(sourceCodeForAClass()
                .named(className)
                .withAMethod(sourceCodeForAMethod()
                        .named(methodName)
                        .withAccess(methodAccess)
                        .callingOnField(targetName, targetMethod)));

        final List<ByteCode> byteCodes = act(byteCodeBuilder);


        assertThat(byteCodes, hasSize(1));
        final ByteCode byteCode = byteCodes.get(0);
        assertThat(byteCode, containsAClass(className)
                .havingAMethodName(methodName)
                .calling(targetName, targetMethod));
    }

    @Test
    public void testByteCodeForSingleClass_containsTwoClasses() throws Exception {
        final String firstClassName = "firstClass";
        final String secondClassName = "secondClass";
        final ByteCodeBuilder byteCodeBuilder = aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named(firstClassName))
                .forAClass(sourceCodeForAClass()
                        .named(secondClassName));

        final List<ByteCode> byteCodes = act(byteCodeBuilder);


        assertThat(byteCodes, hasSize(2));
        final ByteCode byteCode = byteCodes.get(0);
        final Class<?> firstClass = getClassFromByteCode(byteCode);
        assertThat(firstClass.getName(), equalTo(firstClassName));

        final ByteCode byteCode2 = byteCodes.get(1);
        final Class<?> secondClass = getClassFromByteCode(byteCode2);
        assertThat(secondClass.getName(), equalTo(secondClassName));
    }

    @Test
    public void testByteCodeForSingleClass_compilesForAClassReferencingAnother() throws Exception {
        final String referencingClass = "ReferencingClass";
        final String referencedClass = "ReferencedClass";

        String callingMethodName = "callingMethod";
        String calledMethodName = "calledMethod";
        final ByteCodeBuilder byteCodeBuilder = aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named(referencingClass)
                        .withAMethod(sourceCodeForAMethod()
                                .named(callingMethodName)
                                .withAccess("public")
                                .callingUsingTempObject(referencedClass, calledMethodName)))
                .forAClass(sourceCodeForAClass()
                        .named(referencedClass)
                        .withAMethod(sourceCodeForAMethod()
                                .named(calledMethodName)
                                .withAccess("public")));

        final List<ByteCode> byteCodes = act(byteCodeBuilder);


        assertThat(byteCodes, hasSize(2));
        final ByteCode byteCode = byteCodes.get(0);
        final Class<?> firstClass = getClassFromByteCode(byteCode);
        assertThat(firstClass, isAClassNamed(referencingClass).containingMethod(callingMethodName));

        final ByteCode byteCode2 = byteCodes.get(1);
        final Class<?> secondClass = getClassFromByteCode(byteCode2);
        assertThat(secondClass, isAClassNamed(referencedClass).containingMethod(calledMethodName));
    }

    @Test
    public void testByteCodeForSingleClass_failsForAClassReferencingNonExistingClass() throws Exception {
        final String referencingClass = "ReferencingClass";
        final String referencedClass = "ReferencedClass";

        String callingMethodName = "callingMethod";
        String calledMethodName = "calledMethod";

        expectedException.expect(CompilationFinishedWithWarningsExceptions.class);
        final ByteCodeBuilder byteCodeBuilder = aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named(referencingClass)
                        .withAMethod(sourceCodeForAMethod()
                                .named(callingMethodName)
                                .withAccess("public")
                                .callingUsingTempObject(referencedClass, calledMethodName)));

        act(byteCodeBuilder);
    }

    private List<ByteCode> act(ByteCodeBuilder byteCodeBuilder) throws CompilationFinishedWithWarningsExceptions, IOException {
        return byteCodeBuilder
                .build();
    }

    private Class<?> getClassFromByteCode(ByteCode byteCode) throws ClassNotFoundException {
        final String className = byteCode.getRelatedClassName();
        return byteCode.getAssociatedClassLoader().loadClass(className);
    }
}
