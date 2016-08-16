package castendyck.analyzing.reducing.internal;

import castendyck.analyzing.reducing.Reducer;
import castendyck.analyzing.reducing.ReducerConfiguration;
import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.CallPath;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierAbbreviatedBuilder;
import castendyck.reduction.Reduction;

import static castendyck.analyzing.reducing.internal.ReducerConfigurationTestBuilder.aConfiguration;
import static castendyck.analyzing.reducing.internal.ReductionTestBuilder.aReduction;
import static castendyck.callgraph.CallPathBuilder.aCallPath;
import static castendyck.functionidentifier.FunctionIdentifierBuilder.aFunctionIdentifier;
import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aPomFile;
import static castendyck.analyzing.reducing.internal.MockedPomFileBuilder.aSubModule;

public class CallPathNeverTraversesAnArtifactWithTestScopeReducerExamples {
    public static ReductionSetup noCallPaths() {
        final Reduction previousReduction = aReduction()
                .containingNoCallPaths()
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(aPomFile())
                .build();

        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup oneCallPathWithTestScopeAndOneWithout() {
        final ArtifactIdentifier artifactWithCompileScope = ArtifactIdentifierBuilder.buildShort("local", "DepWithCompileScope", "1.0");
        final ArtifactIdentifier artifactWithTestScope = ArtifactIdentifierBuilder.buildShort("local", "DepWithTestScope", "1.0");

        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope, "someClass", "function"),
                        aCallPathEndingIn(artifactWithTestScope, "someClass", "function")
                )
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(aPomFile()
                        .withADependency(artifactWithCompileScope, "compile")
                        .withADependency(artifactWithTestScope, "test"))
                .build();

        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope, "someClass", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup twoCallPathsWithTestScope() {
        final ArtifactIdentifier artifactWithTestScope1 = ArtifactIdentifierBuilder.buildShort("local", "DepWithTestScope1", "1.0");
        final ArtifactIdentifier artifactWithTestScope2 = ArtifactIdentifierBuilder.buildShort("local", "DepWithTestScope2", "1.0");

        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithTestScope1, "someClass", "function"),
                        aCallPathEndingIn(artifactWithTestScope2, "someClass", "function")
                )
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(aPomFile()
                        .withADependency(artifactWithTestScope1, "test")
                        .withADependency(artifactWithTestScope2, "test"))
                .build();

        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup twoCallPathsWithCompileScope() {
        final ArtifactIdentifier artifactWithCompileScope1 = ArtifactIdentifierBuilder.buildShort("local", "DepWithCompileScope1", "1.0");
        final ArtifactIdentifier artifactWithCompileScope2 = ArtifactIdentifierBuilder.buildShort("local", "DepWithCompileScope2", "1.0");

        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope1, "someClass", "function"),
                        aCallPathEndingIn(artifactWithCompileScope2, "someClass", "function")
                )
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(aPomFile()
                        .withADependency(artifactWithCompileScope1, "compile")
                        .withADependency(artifactWithCompileScope2, "compile"))
                .build();

        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope1, "someClass", "function"),
                        aCallPathEndingIn(artifactWithCompileScope2, "someClass", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aCallPathTraversingSeveralArtifactsEndingInTestScope() {
        final ArtifactIdentifier artifactWithTestScope = ArtifactIdentifierBuilder.buildShort("local", "DepWithTestScope", "1.0");
        final FunctionIdentifier endingFunction = FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor(artifactWithTestScope, "someClass.class", "function");

        final ArtifactIdentifier intermediateArtifact1 = ArtifactIdentifierBuilder.buildShort("local", "IntermediateArtifact1", "1.0");
        final FunctionIdentifier intermediateFunction1 = FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor(intermediateArtifact1, "someClass.class", "function");

        final ArtifactIdentifier intermediateArtifact2 = ArtifactIdentifierBuilder.buildShort("local", "IntermediateArtifact2", "1.0");
        final FunctionIdentifier intermediateFunction2 = FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor(intermediateArtifact2, "someClass.class", "function");

        final ArtifactIdentifier startingArtifact = ArtifactIdentifierBuilder.buildShort("local", "StartingArtifact", "1.0");
        final FunctionIdentifier startingFunction = FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor(startingArtifact, "someClass.class", "function");


        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPath()
                                .containingFunctionIdentifiers(startingFunction,
                                        intermediateFunction1,
                                        intermediateFunction2,
                                        endingFunction)
                                .build()
                )
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(aPomFile()
                        .withADependency(artifactWithTestScope, "test"))
                .build();

        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containingNoCallPaths()
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    public static ReductionSetup aProjectWithTwoSubModulesOneHavingACallPathWithTestScopeAndTheOtherOneWithCompileScope() {
        final ArtifactIdentifier root = ArtifactIdentifierBuilder.buildShort("local", "root", "1.0");
        final ArtifactIdentifier subModuleWithDepWithCompileScope = ArtifactIdentifierBuilder.buildShort("local", "subModuleWithDepWithCompileScope", "1.0");
        final ArtifactIdentifier subModuleWithDepWithTestScope = ArtifactIdentifierBuilder.buildShort("local", "subModuleWithDepWithTestScope", "1.0");
        final ArtifactIdentifier artifactWithCompileScope = ArtifactIdentifierBuilder.buildShort("local", "DepWithCompileScope", "1.0");
        final ArtifactIdentifier artifactWithTestScope = ArtifactIdentifierBuilder.buildShort("local", "DepWithTestScope", "1.0");


        final Reduction previousReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope, "someClass", "function"),
                        aCallPathEndingIn(artifactWithTestScope, "someClass", "function"))
                .build();

        final ReducerConfiguration configuration = aConfiguration()
                .with(MockedPomFileBuilder.aPomFile()
                        .forArtifact(root)
                        .withSubmodules(
                                aSubModule().forArtifact(subModuleWithDepWithCompileScope)
                                        .withADependency(artifactWithCompileScope, "compile"),
                                aSubModule().forArtifact(subModuleWithDepWithTestScope)
                                        .withADependency(artifactWithTestScope, "test")))
                .build();
        final Reducer reducer = new CallPathNeverTraversesAnArtifactWithTestScopeReducerImpl(configuration);

        final Reduction expectedReduction = aReduction()
                .containing(
                        aCallPathEndingIn(artifactWithCompileScope, "someClass", "function"))
                .build();
        return new ReductionSetup(previousReduction, reducer, expectedReduction);
    }

    private static CallPath aCallPathEndingIn(ArtifactIdentifier artifactIdentifier, String className, String function) {
        final FunctionIdentifier functionIdentifier = aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(className + ".class")
                .forFunction(function)
                .build();

        final CallPath callPath = aCallPath()
                .containingFunctionIdentifier(functionIdentifier)
                .build();
        return callPath;
    }
}
