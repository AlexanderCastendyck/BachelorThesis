package castendyck.callgraph.functiondata;

import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functioninformation.CallGraphFunctionInformation;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class Then {
    private final CallGraphFunctionInformation controlFlowInformation;
    private final List<FunctionIdentifier> expectedCalls;

    public Then(CallGraphFunctionInformation controlFlowInformation, List<FunctionIdentifier> expectedCalls) {
        this.controlFlowInformation = controlFlowInformation;
        this.expectedCalls = expectedCalls;
    }

    public void theReceivedControlFlowInformationShouldNotHaveAnyCalls() {
        final List<FunctionIdentifier> functionsCalledBy = controlFlowInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, hasSize(0));
    }

    public void theReceivedControlFlowInformationShouldHaveThisOneCall() {
        final List<FunctionIdentifier> functionsCalledBy = controlFlowInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, hasSize(1));
        assertThat(functionsCalledBy, equalTo(expectedCalls));
    }

    public void theReceivedControlFlowInformationShouldHaveThisTwoCalls() {
        final List<FunctionIdentifier> functionsCalledBy = controlFlowInformation.getFunctionsCalledBy();
        assertThat(functionsCalledBy, hasSize(2));
        assertThat(functionsCalledBy, equalTo(expectedCalls));
    }
}
