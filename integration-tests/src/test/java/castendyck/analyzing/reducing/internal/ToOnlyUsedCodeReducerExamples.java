package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.CallPathBuilder;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.reduction.Reduction;

import static castendyck.analyzing.reducing.internal.ReducerConfigurationTestBuilder.aSimpleConfigurationFor;
import static castendyck.analyzing.reducing.internal.ReductionTestBuilder.aReduction;
import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;
import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aSubModule;

public class ToOnlyUsedCodeReducerExamples {
    private static ArtifactIdentifier projectArtifact = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

    public static ReductionSetup noCallPaths() {
        final Reduction previousReduction = aReduction()
                .containingNoCallPaths()
                .build();

        final ReducerConfiguration configuration = aSimpleConfigurationFor(projectArtifact);
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup oneUsedAndOneNotUsedCallPath() {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "someClass", "function"),
                        aCallPathEndingInDifferentArtifact("junit"))
                .build();

        final ReducerConfiguration configuration = aSimpleConfigurationFor(projectArtifact);
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "someClass", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup threeNotUsedCallPaths() {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingInDifferentArtifact("junit"),
                        aCallPathEndingInDifferentArtifact("hamcrest"),
                        aCallPathEndingInDifferentArtifact("spring"))
                .build();

        final ReducerConfiguration configuration = aSimpleConfigurationFor(projectArtifact);
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup threeUsedCallPaths() {
        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "ClassA", "function"),
                        aCallPathEndingIn(projectArtifact, "ClassB", "function"),
                        aCallPathEndingIn(projectArtifact, "ClassC", "function"))
                .build();

        final ReducerConfiguration configuration = aSimpleConfigurationFor(projectArtifact);
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "ClassA", "function"),
                        aCallPathEndingIn(projectArtifact, "ClassB", "function"),
                        aCallPathEndingIn(projectArtifact, "ClassC", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup projectWithToSubModulesWithOneUsedCallPathsInEach() {
        final ArtifactIdentifier subModule1 = ArtifactIdentifierBuilder.buildShort("local", "subModule1", "1.0");
        final ArtifactIdentifier subModule2 = ArtifactIdentifierBuilder.buildShort("local", "subModule2", "1.0");


        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(subModule1, "ClassInSubModule1", "function"),
                        aCallPathEndingIn(subModule2, "ClassInSubModule2", "function"))
                .build();

        final ReducerConfiguration configuration = ReducerConfigurationTestBuilder.aConfiguration()
                .with(MockedPomFileBuilder.aPomFile()
                        .forArtifact(projectArtifact)
                        .withSubmodules(
                                aSubModule().forArtifact(subModule1),
                                aSubModule().forArtifact(subModule2)))
                .build();
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(subModule1, "ClassInSubModule1", "function"),
                        aCallPathEndingIn(subModule2, "ClassInSubModule2", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup projectWithOneModuleWithOneUsedCallPathsInRootAndOneUsedCallPathInTheSubModule() {
        final ArtifactIdentifier subModule = ArtifactIdentifierBuilder.buildShort("local", "subModule", "1.0");

        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "SomeClass", "function"),
                        aCallPathEndingIn(subModule, "ClassInSubModule", "function"))
                .build();

        final ReducerConfiguration configuration = ReducerConfigurationTestBuilder.aConfiguration()
                .with(MockedPomFileBuilder.aPomFile()
                        .forArtifact(projectArtifact)
                        .withSubmodules(
                                aSubModule().forArtifact(subModule)))
                .build();
        final Reducer reducer = new ToOnlyUsedCodeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(projectArtifact, "SomeClass", "function"),
                        aCallPathEndingIn(subModule, "ClassInSubModule", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    private static CallPath aCallPathEndingInDifferentArtifact(String name) {
        final int randomSuffix = (int) (Math.random() * 1000 + 1);
        final String functionName = "function_" + randomSuffix;
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("external", name, "1.0");
        return aCallPathEndingIn(artifactIdentifier, "someClass", functionName);
    }

    private static CallPath aCallPathEndingIn(ArtifactIdentifier artifactIdentifier, String className, String function) {
        final FunctionIdentifier functionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(className + ".class")
                .forFunction(function)
                .build();

        final CallPath callPath = CallPathBuilder.aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();
        return callPath;
    }
}
