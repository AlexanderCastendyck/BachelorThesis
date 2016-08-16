package castendyck.callgraph.functiondata;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.inmemorycompiling.classes.ClassSourceCodeBuilder;
import castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;


class FunctionStateBuilder {
    private final String functionName;
    private final ClassStateBuilder classStateBuilder;
    private final List<NormalMethodSourceCodeBuilder> methods;
    private final List<FunctionIdentifier> expectedCalls;

    public FunctionStateBuilder(String functionName, ClassStateBuilder classStateBuilder) {
        this.functionName = functionName;
        this.classStateBuilder = classStateBuilder;
        this.methods = new ArrayList<>();
        this.expectedCalls = new ArrayList<>();

        final NormalMethodSourceCodeBuilder builderForThisMethod = sourceCodeForAMethod()
                .named(functionName)
                .withAccess("public");
        methods.add(builderForThisMethod);
    }

    public SourceCodeStateBuilder notBeingCalledByAnyOne() {
        return classStateBuilder.getSourceCodeStateBuilder();
    }

    public String getFunctionName() {
        return functionName;
    }

    public SourceCodeStateBuilder beingCalledByFunctionWithinSameClass() {
        final String callingMethodName = "callingOnField";
        final NormalMethodSourceCodeBuilder builderForCallingMethod = sourceCodeForAMethod()
                .named(callingMethodName)
                .withAccess("public")
                .callingWithinSameClass(functionName);

        return registerFunctionCall(builderForCallingMethod);
    }

    public SourceCodeStateBuilder beingCalledByPrivateFunctionWithinSameClass() {
        final String callingMethodName = "callingOnField";
        final NormalMethodSourceCodeBuilder builderForCallingMethod = sourceCodeForAMethod()
                .named(callingMethodName)
                .withAccess("private")
                .callingWithinSameClass(functionName);

        return registerFunctionCall(builderForCallingMethod);
    }

    public SourceCodeStateBuilder beingCalledByTwoFunctionsWithinSameClass() {
        final NormalMethodSourceCodeBuilder method1 = sourceCodeForAMethod()
                .named("calling1")
                .withAccess("public")
                .callingWithinSameClass(functionName);
        registerFunctionCall(method1);

        final NormalMethodSourceCodeBuilder method2 = sourceCodeForAMethod()
                .named("calling2")
                .withAccess("private")
                .callingWithinSameClass(functionName);
        return registerFunctionCall(method2);
    }


    public SourceCodeStateBuilder beingCalledByTwoFunctionsFromTwoDifferentClasses() {
        final ArtifactIdentifier defaultArtifact = classStateBuilder.getArtifactIdentifier();
        final SourceCodeStateBuilder sourceCodeStateBuilder = classStateBuilder.getSourceCodeStateBuilder();

        final String classNameA = "ClassA";
        final String classNameB = "ClassB";

        //rearranged because resuls are ordered differently
        final ClassSourceCodeBuilder classSourceCodeBuilderB = createCallingWithName(classNameB, defaultArtifact);
        final ClassSourceCodeBuilder classSourceCodeBuilderA = createCallingWithName(classNameA, defaultArtifact);

        sourceCodeStateBuilder.addReferencedClass(classSourceCodeBuilderB);
        sourceCodeStateBuilder.addReferencedClass(classSourceCodeBuilderA);

        sourceCodeStateBuilder.registerClassDefaultArtifact(classNameA + ".class");
        sourceCodeStateBuilder.registerClassDefaultArtifact(classNameB + ".class");

        return sourceCodeStateBuilder;
    }

    public SourceCodeStateBuilder beingCalledByTwoFunctionsFromTwoDifferentClassesArtifacts() {
        final SourceCodeStateBuilder sourceCodeStateBuilder = classStateBuilder.getSourceCodeStateBuilder();

        final String callingClassA = "ClassA";
        ArtifactIdentifier artifactA = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("groupA")
                .withArtifactId("ArtifactA")
                .withVersion("1.0")
                .build();
        final ClassSourceCodeBuilder classSourceCodeBuilderA = createCallingWithName(callingClassA, artifactA);
        sourceCodeStateBuilder.addReferencedClass(classSourceCodeBuilderA);
        sourceCodeStateBuilder.registerClassInArtifact(callingClassA + ".class", artifactA);

        final String callingClassB = "ClassB";
        ArtifactIdentifier artifactB = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("groupB")
                .withArtifactId("ArtifactB")
                .withVersion("1.0")
                .build();
        final ClassSourceCodeBuilder classSourceCodeBuilderB = createCallingWithName(callingClassB, artifactB);
        sourceCodeStateBuilder.addReferencedClass(classSourceCodeBuilderB);
        sourceCodeStateBuilder.registerClassInArtifact(callingClassB + ".class", artifactB);

        return sourceCodeStateBuilder;
    }


    private ClassSourceCodeBuilder createCallingWithName(String callingClassName, ArtifactIdentifier artifactIdentifier) {
        final String className = classStateBuilder.getClassName();

        final String callingFunctionName = "calledBy" + callingClassName;
        final NormalMethodSourceCodeBuilder methodSourceCodeBuilder = sourceCodeForAMethod().named(callingFunctionName)
                .withAccess("public")
                .callingUsingTempObject(className, functionName);
        final FunctionIdentifier callingFunction = functionIdentifierFor(callingFunctionName, callingClassName, artifactIdentifier);
        expectedCalls.add(callingFunction);

        final ClassSourceCodeBuilder classSourceCodeBuilder = sourceCodeForAClass()
                .named(callingClassName)
                .withAMethod(methodSourceCodeBuilder);
        return classSourceCodeBuilder;
    }

    private SourceCodeStateBuilder registerFunctionCall(NormalMethodSourceCodeBuilder methodSourceCodeBuilder) {
        methods.add(methodSourceCodeBuilder);

        final String functionName = methodSourceCodeBuilder.getName();
        final FunctionIdentifier callingFunction = functionIdentifierFor(functionName);
        expectedCalls.add(callingFunction);

        return classStateBuilder.getSourceCodeStateBuilder();

    }

    private FunctionIdentifier functionIdentifierFor(String methodName) {
        final String className = classStateBuilder.getClassName();
        return functionIdentifierFor(methodName, className);
    }

    private FunctionIdentifier functionIdentifierFor(String methodName, String className) {
        final ArtifactIdentifier defaultArtifact = classStateBuilder.getArtifactIdentifier();
        return functionIdentifierFor(methodName, className, defaultArtifact);
    }

    private FunctionIdentifier functionIdentifierFor(String methodName, String className, ArtifactIdentifier artifactIdentifier) {
        final String fullClassName = className + ".class";
        return FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(fullClassName)
                .forFunction(methodName)
                .build();
    }

    public List<NormalMethodSourceCodeBuilder> build() {
        return methods;
    }

    public List<FunctionIdentifier> getExpectedCalls() {
        return expectedCalls;
    }
}
