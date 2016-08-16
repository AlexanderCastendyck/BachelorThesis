package castendyck.callgraph.controlflowanalyzer.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.callgraph.callgraphcreating.internal.CallGraphCreatorImpl;
import castendyck.callgraph.callgraphcreating.internal.DirectionReverseImpl;
import castendyck.callgraph.CallPath;
import castendyck.callgraph.callgraphcreating.CallGraphCreator;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functionidentifier.FunctionIdentifierBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static castendyck.callgraph.CallPathBuilder.aCallPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CallPathAnalyzerImplReverseDirectionTest {
    private final CallGraphStrategy strategy;
    private final FunctionDataProvider functionDataProvider;
    private final CallGraphCreator callGraphCreator;
    private final FunctionIdentifier functionIdentifier;
    private final ArtifactIdentifier artifactIdentifier;


    public CallPathAnalyzerImplReverseDirectionTest() {
        this.functionDataProvider = Mockito.mock(FunctionDataProvider.class);
        final DirectionReverseImpl direction = new DirectionReverseImpl(functionDataProvider);
        this.callGraphCreator = new CallGraphCreatorImpl(direction);

        this.artifactIdentifier = ArtifactIdentifierBuilder.anArtifactIdentifier()
                .withGroupId("someGroup")
                .withArtifactId("someArtifact")
                .withVersion("1.0")
                .build();
        this.functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass("someClass.class")
                .forFunction("someFunction")
                .build();
        this.strategy = new ReturnAlwaysTheFirstElementStrategy(functionIdentifier);
    }

    @Test
    public void analyzeControlFlow_withAFunctionCalledNotAnyFurther() throws Exception {
        letFunction(functionIdentifier).notBeCalled();

        final List<CallPath> resultingCallPaths = act();

        assertThat(resultingCallPaths, hasSize(1));
        CallPath expectedCallPath = controlFlowContaining(functionIdentifier);
        assertThat(resultingCallPaths, contains(expectedCallPath));

    }

    @Test
    public void analyzeControlFlow_withOneCallingFunctionResultsInOneContinuedControlFlow() throws Exception {
        final FunctionIdentifier callingFunction = functionIdentifierFor("ClassA");
        letFunction(functionIdentifier).beCalledBy(callingFunction);
        letFunction(callingFunction).notBeCalled();

        final List<CallPath> resultingCallPaths = act();

        assertThat(resultingCallPaths, hasSize(1));
        CallPath expectedCallPath = controlFlowContaining(functionIdentifier, callingFunction);
        assertThat(resultingCallPaths, contains(expectedCallPath));

    }

    @Test
    public void analyzeControlFlow_withTwoCallingFunctionResultsInTwoDivergedControlFlows() throws Exception {
        final FunctionIdentifier callingFunction1 = functionIdentifierFor("ClassA");
        final FunctionIdentifier callingFunction2 = functionIdentifierFor("ClassB");

        letFunction(functionIdentifier).beCalledBy(callingFunction1, callingFunction2);
        letFunction(callingFunction1).notBeCalled();
        letFunction(callingFunction2).notBeCalled();

        final List<CallPath> resultingCallPaths = act();

        List<CallPath> expectedCallPaths = Arrays.asList(
                controlFlowContaining(functionIdentifier, callingFunction1),
                controlFlowContaining(functionIdentifier, callingFunction2)
        );
        assertThat(resultingCallPaths, equalTo(expectedCallPaths));

    }

    @Test
    public void analyzeControlFlow_withCallingFunctionBeingCalledAgain() throws Exception {
        final FunctionIdentifier callingFunctionStep1 = functionIdentifierFor("ClassA");
        final FunctionIdentifier callingFunctionStep2 = functionIdentifierFor("ClassB");

        letFunction(functionIdentifier).beCalledBy(callingFunctionStep1);
        letFunction(callingFunctionStep1).beCalledBy(callingFunctionStep2);
        letFunction(callingFunctionStep2).notBeCalled();

        final List<CallPath> resultingCallPaths = act();

        assertThat(resultingCallPaths, hasSize(1));
        CallPath expectedCallPath = controlFlowContaining(
                functionIdentifier,
                callingFunctionStep1,
                callingFunctionStep2
        );
        assertThat(resultingCallPaths, contains(expectedCallPath));
    }

    @Test
    public void analyzeControlFlow_doesNotAddRecursiveFunction() throws Exception {
        final FunctionIdentifier callingFunction = functionIdentifierFor("ClassA");
        letFunction(functionIdentifier).beCalledBy(callingFunction, functionIdentifier);
        letFunction(callingFunction).notBeCalled();

        final List<CallPath> resultingCallPaths = act();

        assertThat(resultingCallPaths, hasSize(1));
        CallPath expectedCallPath = controlFlowContaining(functionIdentifier, callingFunction);
        assertThat(resultingCallPaths, contains(expectedCallPath));
    }

    private FunctionIdentifier functionIdentifierFor(String classname) {
        final FunctionIdentifier functionIdentifier = FunctionIdentifierBuilder.aFunctionIdentifier()
                .withArtifactIdentifier(artifactIdentifier)
                .forClass(classname + ".class")
                .forFunction("someFunction")
                .build();
        return functionIdentifier;
    }

    private CallRegisterer letFunction(FunctionIdentifier function) {
        return CallRegisterer.letFunctionAtProvider(function, functionDataProvider);
    }

    private CallPath controlFlowContaining(FunctionIdentifier... functionIdentifiers) {
        final CallPath callPath = aCallPath()
                .containingFunctionIdentifiers(functionIdentifiers)
                .build();
        return callPath;
    }

    private List<CallPath> act() throws StrategyUnexpectedStateException {
        return callGraphCreator.analyzeControlFlow(strategy);
    }

    private class ReturnAlwaysTheFirstElementStrategy implements CallGraphStrategy {
        private final CallPath initialCallPath;
        private boolean canFinish;

        public ReturnAlwaysTheFirstElementStrategy(FunctionIdentifier functionIdentifier) {
            initialCallPath = aCallPath()
                    .containingFunctionIdentifiers(functionIdentifier)
                    .build();
            canFinish = false;
        }

        @Override
        public CallPath determineNextCallPath(CallPath currentCallPath, List<CallPath> callPaths) {
            if (currentCallPath == null) {
                canFinish = true;
                return initialCallPath;
            }
            return callPaths.get(0);
        }

        @Override
        @Nullable
        public ArtifactIdentifier getActiveArtifact() {
            return null;
        }

        @Override
        public boolean isFinished(List<CallPath> callPaths) {
            return canFinish && callPaths.size() == 0;
        }
    }
}