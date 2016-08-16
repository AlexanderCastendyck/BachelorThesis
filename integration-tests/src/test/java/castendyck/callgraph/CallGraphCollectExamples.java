package castendyck.callgraph;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.inmemorycompiling.CompilationFinishedWithWarningsExceptions;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static castendyck.callgraph.CallGraphStrategyBuilder.aCallGraphStrategy;
import static castendyck.callgraph.CallGraphStrategyBuilder.aLevel;
import static castendyck.dependencygraphing.DependencyGraphTestBuilder.aDependencyGraph;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;
import static castendyck.inmemorycompiling.classes.InterfaceClassSourceCodeBuilder.sourceCodeForAnInterface;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.AbstractMethodSourceCodeBuilder.sourceCodeForAnAbstractMethod;
import static castendyck.inmemorycompiling.methods.InterfaceMethodSourceCodeBuilder.sourceCodeForAnInterfaceMethod;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;
import static castendyck.localrepository.LocalRepositoryBuilder.aLocalRepository;

public class CallGraphCollectExamples {

    public static CallGraphCollectExample exampleForAFunctionNotCalled() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForAFunctionNotCalled"))
                .with(byteCodes)
                .build();


        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledByPublicFunction() throws IOException, CompilationFinishedWithWarningsExceptions {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")
                                .withAccess("public")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByPublicFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }


    public static CallGraphCollectExample exampleForFunctionCalledByPrivateFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")
                                .withAccess("private")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByPrivateFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledByStaticFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")
                                .withAccess("public")
                                .witModifier("static")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByStaticFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledByImplementationOfAbstractFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB_abstract")
                        .withModifier("abstract")
                        .withAMethod(sourceCodeForAnAbstractMethod()
                                .named("function2")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB_impl")
                        .extending("ClassB_abstract")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")
                                .withAccess("public")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));

        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB_abstract", startingArtifact)
                        .withClassStoredInJarFor("ClassB_impl", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByImplementationOfAbstractFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB_impl.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledByInterfaceFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAnInterface()
                        .named("ClassB_interface")
                        .withAMethod(sourceCodeForAnInterfaceMethod()
                                .named("function2")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB_impl")
                        .implementing("ClassB_interface")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")
                                .withAccess("public")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB_interface", startingArtifact)
                        .withClassStoredInJarFor("ClassB_impl", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByInterfaceFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB_impl.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledAndCallingFunctionAlsoCalled() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassC")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function3")
                                .callingUsingTempObject("ClassB", "function2")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();


        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .withClassStoredInJarFor("ClassC", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledAndCallingFunctionAlsoCalled"))
                .with(byteCodes)
                .build();


        final FunctionIdentifier function3Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassC.class")
                .forFunction("function3")
                .build();
        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .containingFunctionIdentifier(
                                function3Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledTwice() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassC")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function3")
                                .callingUsingTempObject("ClassA", "function1")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(CallGraphStrategyBuilder.aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .withClassStoredInJarFor("ClassC", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledTwice"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final FunctionIdentifier function3Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassC.class")
                .forFunction("function3")
                .build();
        final List<CallPath> expectedCallPaths = Arrays.asList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build(),
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function3Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledFromAnotherArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependingArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependingArtifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(dependingArtifact)
                                .withChildren(aGraphNode()
                                        .forArtifact(startingArtifact))))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint))
                        .having(aLevel()
                                .withArtifactIdentifier(dependingArtifact)))
                .build();


        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", dependingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledFromAnotherArtifact"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(dependingArtifact)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledFromTwoDifferentArtifacts() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassC")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function3")
                                .callingUsingTempObject("ClassA", "function1")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependingArtifact_B = ArtifactIdentifierBuilder.buildShort("local", "dependingArtifact_B", "1.0");
        final ArtifactIdentifier dependingArtifact_C = ArtifactIdentifierBuilder.buildShort("local", "dependingArtifact_C", "1.0");
        final ArtifactIdentifier artificialRootNode = ArtifactIdentifierBuilder.buildShort("local", "artificialRootNode", "1.0");

        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(artificialRootNode)
                                .withChildren(aGraphNode()
                                                .forArtifact(dependingArtifact_B)
                                                .withChildren(aGraphNode()
                                                        .forArtifact(startingArtifact)),
                                        aGraphNode()
                                                .forArtifact(dependingArtifact_C)
                                                .withChildren(aGraphNode()
                                                        .forArtifact(startingArtifact)))))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint))
                        .having(aLevel()
                                .withArtifactIdentifier(dependingArtifact_B))
                        .having(aLevel()
                                .withArtifactIdentifier(dependingArtifact_C)))
                .build();


        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", dependingArtifact_B)
                        .withClassStoredInJarFor("ClassC", dependingArtifact_C)
                        .withEmptyJarFor(artificialRootNode)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledFromTwoDifferentArtifacts"))
                .with(byteCodes)
                .build();
        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(dependingArtifact_B)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final FunctionIdentifier function3Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(dependingArtifact_C)
                .forClass("ClassC.class")
                .forFunction("function3")
                .build();
        final List<CallPath> expectedCallPaths = Arrays.asList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .build(),
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function3Identifier)
                        .build()
        );


        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledFromDifferentArtifactWhoseFunctionIsThenCalledAgainByAThirdArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")
                                .callingUsingTempObject("ClassA", "function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassC")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function3")
                                .callingUsingTempObject("ClassB", "function2")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependingArtifact_B = ArtifactIdentifierBuilder.buildShort("local", "dependingArtifact_B", "1.0");
        final ArtifactIdentifier dependingArtifact_C = ArtifactIdentifierBuilder.buildShort("local", "dependingArtifact_C", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(dependingArtifact_C)
                                .withChildren(aGraphNode()
                                        .forArtifact(dependingArtifact_B)
                                        .withChildren(aGraphNode()
                                                .forArtifact(startingArtifact)))))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint))
                        .having(aLevel()
                                .withArtifactIdentifier(dependingArtifact_B))
                        .having(aLevel()
                                .withArtifactIdentifier(dependingArtifact_C)))
                .build();


        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", dependingArtifact_B)
                        .withClassStoredInJarFor("ClassC", dependingArtifact_C)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledFromDifferentArtifactWhoseFunctionIsThenCalledAgainByAThirdArtifact"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier function2Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(dependingArtifact_B)
                .forClass("ClassB.class")
                .forFunction("function2")
                .build();
        final FunctionIdentifier function3Identifier = aFunctionIdentifier()
                .withArtifactIdentifier(dependingArtifact_C)
                .forClass("ClassC.class")
                .forFunction("function3")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(
                                function2Identifier)
                        .containingFunctionIdentifier(
                                function3Identifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledRecursivelyAndByOneOtherFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("recursiveFunction")
                                .callingUsingTempObject("ClassA", "function1")
                                .callingWithinSameClass("recursiveFunction")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassC")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function3")
                                .callingUsingTempObject("ClassB", "recursiveFunction")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .withClassStoredInJarFor("ClassC", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledRecursivelyAndByOneOtherFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier recursiveFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("recursiveFunction")
                .build();
        final FunctionIdentifier function3FunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassC.class")
                .forFunction("function3")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(recursiveFunctionIdentifier)
                        .containingFunctionIdentifier(function3FunctionIdentifier)
                        .build()
        );
        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForOneFunctionCalledOnlyRecursively() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("recursiveFunction")
                                .callingUsingTempObject("ClassA", "function1")
                                .callingWithinSameClass("recursiveFunction")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();

        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForOneFunctionCalledOnlyRecursively"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier recursiveFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("recursiveFunction")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(recursiveFunctionIdentifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    public static CallGraphCollectExample exampleForFunctionCalledByOverloadedFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassB")
                        .withAMethod(sourceCodeForAMethod()
                                .named("overloadedFunction")
                                .withParameters("()"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("overloadedFunction")
                                .withParameters("(String s)")
                                .callingUsingTempObject("ClassA", "function1"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("overloadedFunction")
                                .withParameters("(int a)")))
                .build();

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final VulnerablePoint vulnerablePoint = aVulnerablePointFor(aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassA.class")
                .forFunction("function1"));
        final CallGraphCollectRequestDto requestDto = CallGraphCollectorRequestDtoBuilder.aRequest()
                .with(aDependencyGraph()
                        .withARootNode(aGraphNode()
                                .forArtifact(startingArtifact)))
                .with(aCallGraphStrategy()
                        .having(aLevel()
                                .withArtifactIdentifier(startingArtifact)
                                .withAVulnerablePoint(vulnerablePoint)))
                .build();


        final CallGraphCollectorConfiguration configuration = CallGraphCollectorConfigurationBuilder.aConfiguration()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", startingArtifact)
                        .withClassStoredInJarFor("ClassB", startingArtifact)
                        .locatedAt("target/controlFlowExampleRepos/exampleForFunctionCalledByOverloadedFunction"))
                .with(byteCodes)
                .build();

        final FunctionIdentifier overloadedFunctionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(startingArtifact)
                .forClass("ClassB.class")
                .forFunction("overloadedFunction")
                .withSignature("(Ljava/lang/String;)V")
                .build();
        final List<CallPath> expectedCallPaths = Collections.singletonList(
                CallPathBuilder.aCallPath()
                        .forVulnerablePoint(vulnerablePoint)
                        .containingFunctionIdentifier(vulnerablePoint.getFunctionIdentifier())
                        .containingFunctionIdentifier(overloadedFunctionIdentifier)
                        .build()
        );

        return new CallGraphCollectExample(configuration, requestDto, expectedCallPaths);
    }

    private static VulnerablePoint aVulnerablePointFor(FunctionIdentifierBuilder functionIdentifierBuilder) {
        final FunctionIdentifier functionIdentifier = functionIdentifierBuilder.build();
        final CVE cve = CveTestBuilder.aCve()
                .build();
        final VulnerablePoint vulnerablePoint = VulnerablePointBuilder.aVulnerablePoint()
                .withFunctionIdentifier(functionIdentifier)
                .forCve(cve)
                .build();
        return vulnerablePoint;
    }

    static class CallGraphCollectExample {
        public List<CallPath> expectedCallPaths;
        private CallGraphCollectorConfiguration configuration;
        private CallGraphCollectRequestDto callGraphCollectRequestDto;

        public CallGraphCollectExample(CallGraphCollectorConfiguration configuration, CallGraphCollectRequestDto requestDto, List<CallPath> expectedCallPaths) {
            this.configuration = configuration;
            this.callGraphCollectRequestDto = requestDto;
            this.expectedCallPaths = expectedCallPaths;
        }

        public CallGraphCollectorConfiguration getConfiguration() {
            return configuration;
        }

        public CallGraphCollectRequestDto getCallGraphCollectRequestDto() {
            return callGraphCollectRequestDto;
        }

        public List<CallPath> getExpectedCallPaths() {
            return expectedCallPaths;
        }
    }

}
