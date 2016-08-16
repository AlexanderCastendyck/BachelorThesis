package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPath;
import castendyck.directorybuilding.DirectoryBuilder;
import castendyck.filebuilding.FileBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.reduction.Reduction;
import castendyck.sourcecode.sourecodefilecollecting.TestClassesCollector;
import castendyck.sourcecode.sourecodefilecollecting.TestClassesCollectorFactory;

import java.io.IOException;
import java.nio.file.Path;

import static castendyck.analyzing.reducing.internal.ReductionTestBuilder.aReduction;
import static castendyck.callgraph.CallPathBuilder.aCallPath;
import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;

public class NoClassesInMavenTestDirectoryReducerExamples {
    private static final String PRODUCTION = "src/main/java/";
    private static final String TEST = "src/test/java/";

    private static final String TEST_ROOT_DIR = "target/NoClassesInMavenTestDirectoryReducerExamples/";


    public static ReductionSetup noCallPaths() throws IOException {
        final Reduction previousReduction = aReduction()
                .containingNoCallPaths()
                .build();

        final Path testRoot = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "noCallPaths")
                .with(DirectoryBuilder.aSubDirectory(TEST))
                .build();

        final Reducer reducer = createReducerConfiguredWithTestPath(testRoot);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aCallPathEndingInTest() throws IOException {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn("someClass"))
                .build();

        final Path testRoot = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "aCallPathEndingInTest")
                .with(FileBuilder.aFile(TEST + "someClass.java"))
                .build();

        final Reducer reducer = createReducerConfiguredWithTestPath(testRoot);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aCallPathEndingInProduction() throws IOException {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn("someClass"))
                .build();

        final Path testRoot = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "aCallPathEndingInProduction")
                .with(FileBuilder.aFile(PRODUCTION + "someClass.java"))
                .with(DirectoryBuilder.aSubDirectory(TEST))
                .build();

        final Reducer reducer = createReducerConfiguredWithTestPath(testRoot);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn("someClass"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aCallPathEndingInTestAndOneInProduction() throws IOException {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn("testClass"),
                        aCallPathEndingIn("productionClass"))
                .build();

        final Path testRoot = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "aCallPathEndingInTestAndOneInProduction")
                .with(FileBuilder.aFile(TEST + "testClass.java"))
                .with(FileBuilder.aFile(PRODUCTION + "productionClass.java"))
                .build();

        final Reducer reducer = createReducerConfiguredWithTestPath(testRoot);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn("productionClass"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aCallPathEndingInClassDeepInTestDirectory() throws IOException {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn("sub_Dir/sub2_Dir/sub3_Dir/testClass"))
                .build();

        final Path testRoot = DirectoryBuilder.aDirectory(TEST_ROOT_DIR + "aCallPathEndingInClassDeepInTestDirectory")
                .with(DirectoryBuilder.aSubDirectory(TEST)
                        .with(DirectoryBuilder.aSubDirectory("sub_Dir")
                                .with(DirectoryBuilder.aSubDirectory("sub2_Dir")
                                        .with(DirectoryBuilder.aSubDirectory("sub3_Dir")
                                                .with(FileBuilder.aFile("testClass.java"))))))
                .build();

        final Reducer reducer = createReducerConfiguredWithTestPath(testRoot);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    private static Reducer createReducerConfiguredWithTestPath(Path testRoot) {
        final Path testPath = testRoot.resolve(TEST);
        final ReducerConfiguration configuration = ReducerConfigurationTestBuilder.aConfiguration()
                .withProjectRootPath(testRoot)
                .withGeneralMavenTestPathPart(TEST)
                .build();
        return new NoClassesInMavenTestDirectoryReducerImpl(configuration);
    }

    private static CallPath aCallPathEndingIn(String className) {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final String fullyQualifiedClassName = className + ".class";
        final FunctionIdentifier functionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(fullyQualifiedClassName)
                .forFunction("function")
                .build();

        final CallPath callPath = aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();
        return callPath;
    }
}
