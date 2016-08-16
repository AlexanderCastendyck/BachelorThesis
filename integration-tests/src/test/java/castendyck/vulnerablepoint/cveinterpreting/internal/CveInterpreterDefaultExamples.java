package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.bytecode.dependentartifactcalling.CallsOfDependentArtifactsFinder;
import castendyck.bytecode.dependentartifactcalling.internal.CallsOfDependentArtifactsFinderImpl;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.bytecode.reversecalllist.ReverseCallListCreatorFactory;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.callgraph.functiondata.FunctionDataProviderFactory;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.cve.CVE;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.repository.LocalRepository;
import castendyck.streamfetching.MockedStreamFetcher;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.mockito.Mockito;

import java.util.List;

import static castendyck.cpe.CPE.createNew;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static castendyck.dependencygraphing.dependencyregistry.DependencyRegistryBuilder.aDependencyRegistry;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;
import static castendyck.localrepository.LocalRepositoryBuilder.aLocalRepository;
import static castendyck.vulnerablepoint.cveinterpreting.internal.ByteCodeHandlerWithoutLoadingClassesBuilder.aByteCodeHandler;
import static castendyck.cve.CveTestBuilder.aCve;
import static castendyck.vulnerablepoint.cveinterpreting.internal.MockedUsedCpeFinderBuilder.aUsedCpeFinder;
import static castendyck.vulnerablepoint.cveinterpreting.internal.SourceCodeRegistryBuilder.aSourceCodeRegistry;

public class CveInterpreterDefaultExamples {
    public static CveInterpreterTestExample exampleWithNoCallFromDependentArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(artifactIdentifier))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithNoCallFromDependentArtifact/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withNoVulnerablePoints()
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult);
    }

    public static CveInterpreterTestExample exampleWithOneCallFromDependentArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifactIdentifier)))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass", dependentArtifact)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithOneCallFromDependentArtifact/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("function", "ClassInArtifact.class", artifactIdentifier, cve)
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    public static CveInterpreterTestExample exampleWithTwoCallsFromDependentArtifacts() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass1")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction1")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass2")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction2")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact1 = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact1", "1.0");
        final ArtifactIdentifier dependentArtifact2 = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact2", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact1)
                        .withChildren(aGraphNode()
                                        .forArtifact(artifactIdentifier),
                                aGraphNode()
                                        .forArtifact(dependentArtifact2)
                                        .withChildren(aGraphNode()
                                                .forArtifact(artifactIdentifier))
                        ))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass1", dependentArtifact1)
                .withClassStoredInJarFor("CallingArtifactClass2", dependentArtifact2)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithTwoCallsFromDependentArtifacts/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("function", "ClassInArtifact.class", artifactIdentifier, cve)
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    public static CveInterpreterTestExample exampleWithTwoCallsFromDependentArtifactToTwoDifferentFunctions() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassInArtifact", "function1")
                                .callingUsingTempObject("ClassInArtifact", "function2")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifactIdentifier)))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass", dependentArtifact)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithTwoCallsFromDependentArtifactToTwoDifferentFunctions/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("function1", "ClassInArtifact.class", artifactIdentifier, cve)
                .withAVulnerablePoint("function2", "ClassInArtifact.class", artifactIdentifier, cve)
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    public static CveInterpreterTestExample exampleWithOneCallFromDependentArtifactCallingFurtherInArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")
                                .callingWithinSameClass("privateMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("privateMethod")
                                .callingUsingTempObject("InnerClassInArtifact", "functionWherePathsEnds")))
                .forAClass(sourceCodeForAClass()
                        .named("InnerClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("functionWherePathsEnds")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifactIdentifier)))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("InnerClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass", dependentArtifact)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithOneCallFromDependentArtifactCallingFurtherInArtifact/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("functionWherePathsEnds", "InnerClassInArtifact.class", artifactIdentifier, cve)
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    public static CveInterpreterTestExample exampleWithOneCallFromDependentArtifactResultingInTwoCallPathsInArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")
                                .callingWithinSameClass("functionWherePathsEnds1")
                                .callingWithinSameClass("functionWherePathsEnds2"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("functionWherePathsEnds1"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("functionWherePathsEnds2")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifactIdentifier)))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass", dependentArtifact)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithOneCallFromDependentArtifactResultingInTwoCallPathsInArtifact/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("functionWherePathsEnds1", "ClassInArtifact.class", artifactIdentifier, cve)
                .withAVulnerablePoint("functionWherePathsEnds2", "ClassInArtifact.class", artifactIdentifier, cve)
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    public static CveInterpreterTestExample exampleWithOneCallFromDependentArtifactThatDoesNotEndInTargetArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassInArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")
                                .callingUsingTempObject("ClassInThirdArtifact", "functionWherePathsEnds")))
                .forAClass(sourceCodeForAClass()
                        .named("ClassInThirdArtifact")
                        .withAMethod(sourceCodeForAMethod()
                                .named("functionWherePathsEnds")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingArtifactClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassInArtifact", "function")))
                .build();

        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final ArtifactIdentifier dependentArtifact = ArtifactIdentifierBuilder.buildShort("local", "dependentArtifact", "1.0");
        final ArtifactIdentifier finalArtifact = ArtifactIdentifierBuilder.buildShort("local", "finalArtifact", "1.0");
        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode()
                        .forArtifact(dependentArtifact)
                        .withChildren(aGraphNode()
                                .forArtifact(artifactIdentifier)
                                .withChildren(aGraphNode()
                                        .forArtifact(finalArtifact))))
                .build();

        final LocalRepository localRepository = aLocalRepository()
                .containing(byteCodes)
                .withClassStoredInJarFor("ClassInArtifact", artifactIdentifier)
                .withClassStoredInJarFor("CallingArtifactClass", dependentArtifact)
                .withClassStoredInJarFor("ClassInThirdArtifact", finalArtifact)
                .locatedAt("target/CveInterpreterDefaultExamples/exampleWithOneCallFromDependentArtifactThatDoesNotEndInTargetArtifact/")
                .build();
        final CveInterpreter cveInterpreter = createFrom(dependencyGraph, localRepository, byteCodes);

        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withNoVulnerablePoints()
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    private static CveInterpreter createFrom(DependencyGraph dependencyGraph, LocalRepository localRepository, List<ByteCode> byteCodes) throws Exception {
        final SourceCodeRegistry sourcecodeRegistry = aSourceCodeRegistry()
                .with(localRepository)
                .with(dependencyGraph)
                .build();
        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final DependencyRegistry dependencyRegistry = aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        final ReverseCallListCreator reverseCallListCreator = ReverseCallListCreatorFactory.newInstance(dependencyRegistry, sourcecodeRegistry, byteCodeHandler);
        final CallsOfDependentArtifactsFinder dependentArtifactsFinder = new CallsOfDependentArtifactsFinderImpl(reverseCallListCreator);

        final MockedStreamFetcher streamFetcher = MockedStreamFetcher.forByteCodes(byteCodes);
        final FunctionDataProvider functionDataProvider = FunctionDataProviderFactory.newInstance(sourcecodeRegistry, dependencyRegistry, streamFetcher);

        final UsedCpeFinder usedCpeFinder = aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final CveInterpreter cveInterpreter = new CveInterpreterDefaultImpl(usedCpeFinder, dependentArtifactsFinder, functionDataProvider);
        return cveInterpreter;
    }

    private static CveInterpreterTestExample createCveInterpreterExampleFor(CveInterpreter cveInterpreter, CveInterpretingResult expectedCveInterpretingResult) {
        final CVE cve = aCve()
                .witCpe(createNew("cpe:/a:local:artifact:1.0"))
                .build();
        return createCveInterpreterExampleFor(cveInterpreter, expectedCveInterpretingResult, cve);
    }

    private static CveInterpreterTestExample createCveInterpreterExampleFor(CveInterpreter cveInterpreter, CveInterpretingResult expectedCveInterpretingResult, CVE cve) {
        final PomFile pomFile = Mockito.mock(PomFile.class);
        return new CveInterpreterTestExample(cveInterpreter, cve, pomFile, expectedCveInterpretingResult);
    }
}
