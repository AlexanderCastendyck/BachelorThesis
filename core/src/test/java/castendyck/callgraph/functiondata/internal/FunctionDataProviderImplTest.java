package castendyck.callgraph.functiondata.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.calllist.CallListCreator;
import castendyck.bytecode.calllist.CallListCreatorFactory;
import castendyck.bytecode.reversecalllist.ReverseCallListCreator;
import castendyck.bytecode.reversecalllist.ReverseCallListCreatorFactory;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.dependencygraphing.dependencyregistry.DependencyRegistry;
import castendyck.dependencygraphing.dependencyregistry.NotRegisteredArtifactException;
import castendyck.functioninformation.CallGraphFunctionInformation;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.List;

import static castendyck.callgraph.functiondata.internal.FunctionCallBuilder.CalledFunctionsFor;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class FunctionDataProviderImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final FunctionDataProviderImpl functionDataProvider;
    private final SourceCodeRegistry sourceCodeRegistry;
    private final DependencyRegistry dependencyRegistry;
    private final ByteCodeHandler byteCodeHandler;
    private final String className;
    private final FunctionIdentifier functionIdentifier;
    private final ArtifactIdentifier artifactIdentifier;

    public FunctionDataProviderImplTest() {
        sourceCodeRegistry = Mockito.mock(SourceCodeRegistry.class);
        dependencyRegistry = Mockito.mock(DependencyRegistry.class);
        byteCodeHandler = Mockito.mock(ByteCodeHandler.class);
        final CallListCreator callListCreator = CallListCreatorFactory.newInstance(sourceCodeRegistry, byteCodeHandler);
        final ReverseCallListCreator reverseCallListCreator = ReverseCallListCreatorFactory.newInstance(dependencyRegistry, sourceCodeRegistry, byteCodeHandler);
        functionDataProvider = new FunctionDataProviderImpl(callListCreator, reverseCallListCreator);

        artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withArtifactId("someArtifact")
                .withGroupId("someGroup")
                .withVersion("1.0")
                .build();
        className = "someClass.class";
        functionIdentifier = functionIdentifierFor(artifactIdentifier, className, "someFunction");
    }

    @Test
    public void testProvideControlFlowInformation_returnsEmptyListForNoCallingFunction() throws Exception {
        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .startingNow();
        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, hasSize(0));
    }

    @Test
    public void testProvideControlFlowInformation_returnsOneCallForOneCallingFunction() throws Exception {
        final String callingClass = "differentClass.class";
        FunctionIdentifier callingFunctionIdentifier = functionIdentifierFor(callingClass, "callingFunction");
        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className, callingClass)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(callingClass, CalledFunctionsFor(callingFunctionIdentifier)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunctionIdentifier));
    }

    @Test
    public void testProvideControlFlowInformation_returnsTwoCallFromTwoCallingFunctionInDifferentClasses() throws Exception {
        final String classpath2 = "ClassB.class";
        final String classpath3 = "ClassC.class";
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(classpath2, "someFunction");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor(classpath3, "anotherFunction");
        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(this.className, classpath2, classpath3)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(this.className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(callingFunction1)
                        .calling(functionIdentifier))
                .havingClass(classpath3, CalledFunctionsFor(callingFunction2)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, containsInAnyOrder(callingFunction1, callingFunction2));
    }

    @Test
    public void testProvideControlFlowInformation_returnsOneCallFromOneCallInDifferentArtifact() throws Exception {
        final String classpath2 = "ClassB.class";
        ArtifactIdentifier callingArtifactIdentifier = artifactIdentifierFor("differentGroup", "differentArtifact", "1.0");
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(callingArtifactIdentifier, classpath2, "someFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .registerArtifactWithClassPaths(callingArtifactIdentifier, classpath2)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(callingFunction1)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();
        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunction1));
    }

    @Test
    public void testProvideControlFlowInformation_returnsTwoCallFromTwoCallOfSameClassInDifferentArtifact() throws Exception {
        final String classpath2 = "ClassB.class";

        ArtifactIdentifier callingArtifactIdentifier = artifactIdentifierFor("differentGroup", "differentArtifact", "1.0");
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(callingArtifactIdentifier, classpath2, "someFunction");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor(callingArtifactIdentifier, classpath2, "andFunction2");

        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .registerArtifactWithClassPaths(callingArtifactIdentifier, classpath2)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(functionIdentifier)
                        .calledBy(callingFunction1)
                        .calledBy(callingFunction2))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunction1, callingFunction2));
    }

    @Test
    public void testProvideControlFlowInformation_returnsTwoCallFromTwoCallOfTwoDifferentClassInDifferentArtifact() throws Exception {
        final String classpath2 = "ClassB.class";
        final String classpath3 = "ClassC.class";
        ArtifactIdentifier callingArtifactIdentifier = artifactIdentifierFor("differentGroup", "differentArtifact", "1.0");
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(callingArtifactIdentifier, classpath2, "someFunction");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor(callingArtifactIdentifier, classpath3, "differentFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .registerArtifactWithClassPaths(callingArtifactIdentifier, classpath2, classpath3)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(callingFunction1)
                        .calling(functionIdentifier))
                .havingClass(classpath3, CalledFunctionsFor(callingFunction2)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunction1, callingFunction2));
    }

    @Test
    public void testProvideControlFlowInformation_returnsTwoCallFromTwoCallOfTwoDifferentClassInTwoDifferentArtifacts() throws Exception {
        final String classpath2 = "ClassB.class";
        final String classpath3 = "ClassC.class";
        ArtifactIdentifier callingArtifactIdentifier1 = artifactIdentifierFor("differentGroup", "differentArtifact", "1.0");
        ArtifactIdentifier callingArtifactIdentifier2 = artifactIdentifierFor("anotherGroup", "anotherArtifact", "1.0");
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(callingArtifactIdentifier1, classpath2, "someFunction");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor(callingArtifactIdentifier2, classpath3, "differentFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .registerArtifactWithClassPaths(callingArtifactIdentifier1, classpath2)
                .registerArtifactWithClassPaths(callingArtifactIdentifier2, classpath3)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(callingFunction1)
                        .calling(functionIdentifier))
                .havingClass(classpath3, CalledFunctionsFor(callingFunction2)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunction1, callingFunction2));
    }

    @Test
    public void testProvideControlFlowInformation_returnsTwoCallForOneCallWithinSameArtifactAndAnotherCallFromDifferentArtifact() throws Exception {
        final String classpath2 = "ClassB.class";
        final String classpath3 = "ClassC.class";
        ArtifactIdentifier callingArtifactIdentifier1 = artifactIdentifierFor("differentGroup", "differentArtifact", "1.0");
        final FunctionIdentifier callingFunction1 = functionIdentifierFor(artifactIdentifier, classpath2, "someFunction");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor(callingArtifactIdentifier1, classpath3, "differentFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className, classpath2)
                .registerArtifactWithClassPaths(callingArtifactIdentifier1, classpath3)
                .startingNow();

        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .havingClass(classpath2, CalledFunctionsFor(callingFunction1)
                        .calling(functionIdentifier))
                .havingClass(classpath3, CalledFunctionsFor(callingFunction2)
                        .calling(functionIdentifier))
                .startingNow();

        final CallGraphFunctionInformation callGraphFunctionInformation = act();

        final List<FunctionIdentifier> functionsCalledBy = callGraphFunctionInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, contains(callingFunction1, callingFunction2));
    }


    @SuppressWarnings("unchecked")
    @Ignore
    @Test
    public void testProvideControlFlowInformation_throwsExceptionForAKnownArtifactIdentifier() throws Exception {
        EnvironmentInstructor.prepare(sourceCodeRegistry, dependencyRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths(className)
                .startingNow();
        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass(className, CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .startingNow();
        when(dependencyRegistry.getArtifactsThatDependOn(artifactIdentifier)).thenThrow(NotRegisteredArtifactException.class);

        expectedException.expect(CouldNotExtractCalledFunctionsOutOfClassException.class);
        act();
    }

    private CallGraphFunctionInformation act() throws CouldNotExtractCalledFunctionsOutOfClassException {
        return functionDataProvider.provideControlFlowInformation(this.functionIdentifier);
    }

    private FunctionIdentifier functionIdentifierFor(String className, String functionName) {
        return functionIdentifierFor(artifactIdentifier, className, functionName);
    }

    private FunctionIdentifier functionIdentifierFor(ArtifactIdentifier artifactIdentifier, String className, String functionName) {
        final String fullClassName = className;
        return FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(fullClassName)
                .forFunction(functionName)
                .build();
    }

    private ArtifactIdentifier artifactIdentifierFor(String groupName, String artifactName, String version) {
        ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId(groupName)
                .withArtifactId(artifactName)
                .withVersion(version)
                .build();
        return artifactIdentifier;
    }
}