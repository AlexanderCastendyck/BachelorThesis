package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistryBuilder;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.maven.pomfile.PomFile;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpreter;
import castendyck.vulnerablepoint.cveinterpreting.CveInterpretingResult;
import castendyck.vulnerablepoint.usedcpefinding.UsedCpeFinder;
import org.mockito.Mockito;

import java.util.List;

import static castendyck.cpe.CPE.createNew;
import static castendyck.dependencygraphing.GraphNodeTestBuilder.aGraphNode;
import static castendyck.inmemorycompiling.classes.NormalClassSourceCodeBuilder.sourceCodeForAClass;
import static castendyck.inmemorycompiling.methods.NormalMethodSourceCodeBuilder.sourceCodeForAMethod;
import static castendyck.localrepository.LocalRepositoryBuilder.aLocalRepository;
import static castendyck.vulnerablepoint.cveinterpreting.internal.ByteCodeHandlerWithoutLoadingClassesBuilder.aByteCodeHandler;

public class CveInterpreterFindAllCalledFunctionsExamples {
    public static CveInterpreterTestExample exampleForNoCallingFunction() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode().forArtifact(artifactIdentifier))
                .build();

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", artifactIdentifier)
                        .locatedAt("target/CveInterpreterExamples/exampleForNoCallingFunction/"))
                .with(dependencyGraph)
                .build();

        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = MockedUsedCpeFinderBuilder.aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final DependencyRegistry dependencyRegistry = DependencyRegistryBuilder.aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        final CveInterpreter interpreter = new CveInterpreterFindAllCalledFunctionsImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry, dependencyRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();
        final PomFile pomFile = Mockito.mock(PomFile.class);
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.NO_USABLE_RESULT)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }

    public static CveInterpreterTestExample exampleForOneCallingFunctionInDependingArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction")
                                .callingUsingTempObject("ClassA", "function")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");
        final ArtifactIdentifier dependingArtifact = ArtifactIdentifierBuilder.buildShort("group2", "dependingArtifact", "1.0");

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode().forArtifact(dependingArtifact)
                        .withChildren(aGraphNode().forArtifact(artifactIdentifier)))
                .build();

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", artifactIdentifier)
                        .withClassStoredInJarFor("CallingClass", dependingArtifact)
                        .locatedAt("target/CveInterpreterExamples/exampleForOneCallingFunctionInDependingArtifact/"))
                .with(dependencyGraph)
                .build();

        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = MockedUsedCpeFinderBuilder.aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final DependencyRegistry dependencyRegistry = DependencyRegistryBuilder.aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        final CveInterpreter interpreter = new CveInterpreterFindAllCalledFunctionsImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry, dependencyRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();
        final PomFile pomFile = Mockito.mock(PomFile.class);
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("function", "ClassA.class", artifactIdentifier, cve)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }

    public static CveInterpreterTestExample exampleForTwoCallingFunctionInTwoDependingArtifact() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ClassA")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("function2")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass_A")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction_A")
                                .callingUsingTempObject("ClassA", "function")))
                .forAClass(sourceCodeForAClass()
                        .named("CallingClass_B")
                        .withAMethod(sourceCodeForAMethod()
                                .named("callingFunction_B")
                                .callingUsingTempObject("ClassA", "function2")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");
        final ArtifactIdentifier dependingArtifact_A = ArtifactIdentifierBuilder.buildShort("groupA", "dependingArtifactA", "1.0");
        final ArtifactIdentifier dependingArtifact_B = ArtifactIdentifierBuilder.buildShort("groupB", "dependingArtifactB", "1.0");

        final DependencyGraph dependencyGraph = DependencyGraphTestBuilder.aDependencyGraph()
                .withARootNode(aGraphNode().forArtifact(dependingArtifact_A)
                        .withChildren(aGraphNode().forArtifact(artifactIdentifier),
                                aGraphNode().forArtifact(dependingArtifact_B)
                                        .withChildren(aGraphNode().forArtifact(artifactIdentifier))))
                .build();

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ClassA", artifactIdentifier)
                        .withClassStoredInJarFor("CallingClass_A", dependingArtifact_A)
                        .withClassStoredInJarFor("CallingClass_B", dependingArtifact_B)
                        .locatedAt("target/CveInterpreterExamples/exampleForOneCallingFunctionInDependingArtifact/"))
                .with(dependencyGraph)
                .build();

        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = MockedUsedCpeFinderBuilder.aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final DependencyRegistry dependencyRegistry = DependencyRegistryBuilder.aDependencyRegistry()
                .initializedWithGraph(dependencyGraph)
                .build();
        final CveInterpreter interpreter = new CveInterpreterFindAllCalledFunctionsImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry, dependencyRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();
        final PomFile pomFile = Mockito.mock(PomFile.class);
        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("function", "ClassA.class", artifactIdentifier, cve)
                .withAVulnerablePoint("function2", "ClassA.class", artifactIdentifier, cve)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }
}
