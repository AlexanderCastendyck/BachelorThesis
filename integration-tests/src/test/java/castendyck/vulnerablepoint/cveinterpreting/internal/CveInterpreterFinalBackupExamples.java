package castendyck.vulnerablepoint.cveinterpreting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.cve.CVE;
import castendyck.cve.CveTestBuilder;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.inmemorycompiling.ByteCode;
import castendyck.inmemorycompiling.ByteCodeBuilder;
import castendyck.maven.pomfile.PomFile;
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
import static castendyck.vulnerablepoint.cveinterpreting.internal.MockedUsedCpeFinderBuilder.aUsedCpeFinder;
import static org.mockito.Matchers.any;

public class CveInterpreterFinalBackupExamples {
    public static CveInterpreterTestExample exampleWithOnlyOneApiCallInOneClass() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ArbitraryClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ApiClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod")
                                .callingUsingTempObject("ArbitraryClass", "function1")
                                .callingWithinSameClass("notExposedMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("notExposedMethod")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ArbitraryClass", artifactIdentifier)
                        .withClassStoredInJarFor("ApiClass", artifactIdentifier)
                        .locatedAt("target/CveInterpreterExamples/exampleWithOnlyOneApiCallInOneClass/"))
                .with(DependencyGraphTestBuilder.aDependencyGraph()
                        .withARootNode(aGraphNode().forArtifact(artifactIdentifier)))
                .build();
        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final CveInterpreterFinalBackupImpl interpreter = new CveInterpreterFinalBackupImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();

        final PomFile pomFile = Mockito.mock(PomFile.class);

        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("exposedMethod", "ApiClass.class", artifactIdentifier, cve)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }



    public static CveInterpreterTestExample exampleWithThreeExposedApiCallsInOneClass() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ApiClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod1")
                                .callingWithinSameClass("notExposedMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod2")
                                .callingWithinSameClass("notExposedMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod3")
                                .callingUsingTempObject("Object", "toString"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("notExposedMethod")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ApiClass", artifactIdentifier)
                        .locatedAt("target/CveInterpreterExamples/exampleWithThreeExposedApiCallsInOneClass/"))
                .with(DependencyGraphTestBuilder.aDependencyGraph()
                        .withARootNode(aGraphNode().forArtifact(artifactIdentifier)))
                .build();
        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final CveInterpreterFinalBackupImpl interpreter = new CveInterpreterFinalBackupImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();

        final PomFile pomFile = Mockito.mock(PomFile.class);

        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("exposedMethod1", "ApiClass.class", artifactIdentifier, cve)
                .withAVulnerablePoint("exposedMethod2", "ApiClass.class", artifactIdentifier, cve)
                .withAVulnerablePoint("exposedMethod3", "ApiClass.class", artifactIdentifier, cve)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }

    public static CveInterpreterTestExample exampleWithTwoExposedApiCallsInTwoClasses() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ArbitraryClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("function1")))
                .forAClass(sourceCodeForAClass()
                        .named("ApiClass1")
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod1")
                                .callingUsingTempObject("ArbitraryClass", "function1")
                                .callingWithinSameClass("notExposedMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("notExposedMethod")))
                .forAClass(sourceCodeForAClass()
                        .named("ApiClass2")
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod2")
                                .callingWithinSameClass("notExposedMethod"))
                        .withAMethod(sourceCodeForAMethod()
                                .named("notExposedMethod")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ArbitraryClass", artifactIdentifier)
                        .withClassStoredInJarFor("ApiClass1", artifactIdentifier)
                        .withClassStoredInJarFor("ApiClass2", artifactIdentifier)
                        .locatedAt("target/CveInterpreterExamples/exampleWithTwoExposedApiCallsInTwoClasses/"))
                .with(DependencyGraphTestBuilder.aDependencyGraph()
                        .withARootNode(aGraphNode().forArtifact(artifactIdentifier)))
                .build();
        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final CveInterpreterFinalBackupImpl interpreter = new CveInterpreterFinalBackupImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();

        final PomFile pomFile = Mockito.mock(PomFile.class);

        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withAVulnerablePoint("exposedMethod1", "ApiClass1.class", artifactIdentifier, cve)
                .withAVulnerablePoint("exposedMethod2", "ApiClass2.class", artifactIdentifier, cve)
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }

    public static CveInterpreterTestExample exampleWithAnExposedCallThatIsNotCallingAnything() throws Exception {
        final List<ByteCode> byteCodes = ByteCodeBuilder.aByteCode()
                .forAClass(sourceCodeForAClass()
                        .named("ApiClass")
                        .withAMethod(sourceCodeForAMethod()
                                .named("exposedMethod")))
                .build();
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("group", "artifact", "1.0");

        final SourceCodeRegistry sourcecodeRegistry = SourceCodeRegistryBuilder.aSourceCodeRegistry()
                .with(aLocalRepository()
                        .containing(byteCodes)
                        .withClassStoredInJarFor("ApiClass", artifactIdentifier)
                        .locatedAt("target/CveInterpreterExamples/exampleWithAnExposedCallThatIsNotCallingAnything/"))
                .with(DependencyGraphTestBuilder.aDependencyGraph()
                        .withARootNode(aGraphNode().forArtifact(artifactIdentifier)))
                .build();
        final ByteCodeHandler byteCodeHandler = aByteCodeHandler()
                .with(byteCodes)
                .with(sourcecodeRegistry)
                .build();

        final UsedCpeFinder usedCpeFinder = aUsedCpeFinder()
                .thatReturnsAllCpesAsUsed();
        final CveInterpreterFinalBackupImpl interpreter = new CveInterpreterFinalBackupImpl(usedCpeFinder, byteCodeHandler, sourcecodeRegistry);

        final CVE cve = CveTestBuilder.aCve()
                .witCpe(createNew("cpe:/a:group:artifact:1.0"))
                .build();

        final PomFile pomFile = Mockito.mock(PomFile.class);

        final CveInterpretingResult expectedCveInterpretingResult = CveInterpretingResultBuilder.anInterpretingResult()
                .withResultStatus(CveInterpretingResult.RESULT.HAS_RESULT)
                .withNoVulnerablePoints()
                .build();
        return new CveInterpreterTestExample(interpreter, cve, pomFile, expectedCveInterpretingResult);
    }
}
