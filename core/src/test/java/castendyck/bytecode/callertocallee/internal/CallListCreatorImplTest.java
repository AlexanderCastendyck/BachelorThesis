package castendyck.bytecode.callertocallee.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.bytecode.bytecodehandling.ByteCodeHandler;
import castendyck.calllist.CallList;
import castendyck.bytecode.calllist.internal.CallListCreatorImpl;
import castendyck.callgraph.functiondata.internal.ByteCodeHandlerInstructor;
import castendyck.callgraph.functiondata.internal.EnvironmentInstructor;
import castendyck.callgraph.sourcecoderegistering.SourceCodeRegistry;
import castendyck.functionidentifier.FunctionIdentifier;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static castendyck.callgraph.functiondata.internal.FunctionCallBuilder.CalledFunctionsFor;
import static castendyck.functionidentifier.FunctionIdentifierAbbreviatedBuilder.aFunctionIdentifierFor;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class CallListCreatorImplTest {
    private ByteCodeHandler byteCodeHandler;
    private CallListCreatorImpl callerToCalleeAssociator;
    private SourceCodeRegistry sourceCodeRegistry;

    public CallListCreatorImplTest() {
        byteCodeHandler = Mockito.mock(ByteCodeHandler.class);
        sourceCodeRegistry = Mockito.mock(SourceCodeRegistry.class);
        callerToCalleeAssociator = new CallListCreatorImpl(sourceCodeRegistry, byteCodeHandler);
    }

    @Test
    public void createForArtifact_returnsEmptyListForNoFunctionCalled() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final FunctionIdentifier functionIdentifier = aFunctionIdentifierFor(artifactIdentifier, "someClass.class", "someFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths("someClass.class")
                .startingNow();
        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass("someClass.class", CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .startingNow();

        final CallList controlFlowFunctionInformation = act(artifactIdentifier);

        final List<FunctionIdentifier> functionsCalledBy = controlFlowFunctionInformation.getCalledFunctionsFor(functionIdentifier);
        assertThat(functionsCalledBy, hasSize(0));
    }

    @Test
    public void createForArtifact_returnsOneCallForOneFunctionCalled() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");
        final FunctionIdentifier functionIdentifier = aFunctionIdentifierFor(artifactIdentifier, "someClass.class", "someFunction");
        final FunctionIdentifier calledFunction = aFunctionIdentifierFor(artifactIdentifier, "differentClass.class", "calledFunction");

        EnvironmentInstructor.prepare(sourceCodeRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths("someClass.class", "differentClass.class")
                .startingNow();
        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass("someClass.class", CalledFunctionsFor(functionIdentifier)
                        .calling(calledFunction))
                .havingClass("differentClass.class", CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .startingNow();

        final CallList controlFlowFunctionInformation = act(artifactIdentifier);

        final List<FunctionIdentifier> functionsCalledBy = controlFlowFunctionInformation.getCalledFunctionsFor(functionIdentifier);
        assertThat(functionsCalledBy, contains(calledFunction));
    }

    @Test
    public void createForArtifact_returnsThreeCallsForThreeFunctionsCalled() throws Exception {
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", "artifact", "1.0");

        final FunctionIdentifier functionIdentifier = aFunctionIdentifierFor(artifactIdentifier, "someClass.class", "someFunction");
        final FunctionIdentifier calledFunction1 = aFunctionIdentifierFor(artifactIdentifier, "someClass.class", "calledFunction1");
        final FunctionIdentifier calledFunction2 = aFunctionIdentifierFor(artifactIdentifier, "differentClass.class", "calledFunction2");
        final FunctionIdentifier calledFunction3 = aFunctionIdentifierFor(artifactIdentifier, "differentClass.class", "calledFunction3");

        EnvironmentInstructor.prepare(sourceCodeRegistry)
                .withArtifactIdentifierOfInterest(artifactIdentifier)
                .registerArtifactOfInterestWithClassPaths("someClass.class", "differentClass.class")
                .startingNow();
        ByteCodeHandlerInstructor.instruct(byteCodeHandler)
                .havingClass("someClass.class", CalledFunctionsFor(functionIdentifier)
                        .calling(calledFunction1, calledFunction2, calledFunction3))
                .havingClass("differentClass.class", CalledFunctionsFor(functionIdentifier)
                        .callingNoFunction())
                .startingNow();

        final CallList controlFlowFunctionInformation = act(artifactIdentifier);

        final List<FunctionIdentifier> functionsCalledBy = controlFlowFunctionInformation.getCalledFunctionsFor(functionIdentifier);
        assertThat(functionsCalledBy, contains(calledFunction1, calledFunction2, calledFunction3));
    }

    private CallList act(ArtifactIdentifier artifactIdentifier) {
        return callerToCalleeAssociator.createForArtifact(artifactIdentifier);
    }
}