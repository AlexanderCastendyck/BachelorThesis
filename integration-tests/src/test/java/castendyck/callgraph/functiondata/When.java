package castendyck.callgraph.functiondata;

import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functioninformation.CallGraphFunctionInformation;

import java.util.List;

public class When {

    private final SourceCodeStateBuilder sourceCodeStateBuilder;

    public When(SourceCodeStateBuilder sourceCodeStateBuilder) {
        this.sourceCodeStateBuilder = sourceCodeStateBuilder;
    }

    public Then then() throws Exception {
        final SourceCodeStateBuilder.SourceCodeState sourceCodeState = sourceCodeStateBuilder.build();
        CallGraphFunctionInformation controlFlowInformation = sourceCodeState.getControlFlowInformation();
        List<FunctionIdentifier> expectedCalls = sourceCodeStateBuilder.getExpectedCalls();
        return new Then(controlFlowInformation, expectedCalls);
    }
}
