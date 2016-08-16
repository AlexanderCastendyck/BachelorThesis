package castendyck.callgraph;

import org.junit.Test;

import static castendyck.callgraph.CallGraphCollectExamples.*;
import static castendyck.callgraph.CallGraphCollectExamples.exampleForAFunctionNotCalled;
import static castendyck.callgraph.CallGraphCollectExamples.exampleForFunctionCalledFromTwoDifferentArtifacts;
import static castendyck.callgraph.Given.given;

public class CallGraphCollectorTestIT {

    @Test
    public void oneFunctionNotCalled_resultsInOneControlFlow() throws Exception {
        given(exampleForAFunctionNotCalled())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledByPublicFunction_resultsInOneControlFlowWithBothFunctions() throws Exception {
        given(exampleForFunctionCalledByPublicFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledByPrivateFunction_resultsInOneControlFlowWithBothFunctions() throws Exception {
        given(exampleForFunctionCalledByPrivateFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();

    }

    @Test
    public void oneFunctionCalledByStaticFunction_resultsInOneControlFlowWithBothFunctions() throws Exception {
        given(exampleForFunctionCalledByStaticFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();

    }

    @Test
    public void oneFunctionCalledByImplementationOfAbstractFunction_resultsInOneControlFlowWithBothTheImplementedFunctionIncluded() throws Exception {
        given(exampleForFunctionCalledByImplementationOfAbstractFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledByInterfaceFunction_resultsInOneControlFlowWithBothFunctions() throws Exception {
        given(exampleForFunctionCalledByInterfaceFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledAndCallingFunctionAlsoCalled_resultsInOneControlFlowWithThreeFunctions() throws Exception {
        given(exampleForFunctionCalledAndCallingFunctionAlsoCalled())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledTwice_resultsInTwoControlFlowWithTwoFunctionsEach() throws Exception {
        given(exampleForFunctionCalledTwice())
                .whenCallGraphIsCollected()
                .thenResultShouldContainTheTwoExpectedControlFlow();
    }

    @Test
    public void oneFunctionCalledFromAnotherArtifact_resultsInOneControlFlowWithBothFunctions() throws Exception {
        given(exampleForFunctionCalledFromAnotherArtifact())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledFromTwoDifferentArtifacts_resultsInTwoControlFlowsWithTwoFunctionsEach() throws Exception {
        given(exampleForFunctionCalledFromTwoDifferentArtifacts())
                .whenCallGraphIsCollected()
                .thenResultShouldContainTheTwoExpectedControlFlow();
    }

    @Test
    public void oneFunctionCalledFromDifferentArtifactWhoseFunctionIsThenCalledAgainByAThirdArtifact_resultsInOneControlFlowWithThreeFunctions() throws Exception {
        given(exampleForFunctionCalledFromDifferentArtifactWhoseFunctionIsThenCalledAgainByAThirdArtifact())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledRecursivelyAndByOneOtherFunction_resultsInOneControlFlowWithRecursiveFunctionOnce() throws Exception {
        given(exampleForFunctionCalledRecursivelyAndByOneOtherFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }
    @Test
    public void oneFunctionCalledOnlyRecursively_resultsInOneControlFlowWithRecursiveFunctionOnce() throws Exception {
        given(exampleForOneFunctionCalledOnlyRecursively())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }

    @Test
    public void oneFunctionCalledByOverloadedFunction_resultsInOneControlFlowWithCorrectOverloadedFunctionIncluded() throws Exception {
        given(exampleForFunctionCalledByOverloadedFunction())
                .whenCallGraphIsCollected()
                .thenResultShouldContainExpectedCallGraph();
    }
}
