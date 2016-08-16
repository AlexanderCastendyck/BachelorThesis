package castendyck.callgraph.callgraphcreating.internal;

import castendyck.callgraph.callgraphcreating.Direction;
import castendyck.callgraph.functiondata.CouldNotExtractCalledFunctionsOutOfClassException;
import castendyck.callgraph.functiondata.FunctionDataProvider;
import castendyck.functionidentifier.FunctionIdentifier;
import castendyck.functioninformation.CallGraphFunctionInformation;

import java.util.List;

public class DirectionReverseImpl implements Direction {
    private final FunctionDataProvider functionDataProvider;

    public DirectionReverseImpl(FunctionDataProvider functionDataProvider) {
        this.functionDataProvider = functionDataProvider;
    }

    @Override
    public List<FunctionIdentifier> getNextFunctionsToTraverseAfter(FunctionIdentifier functionIdentifier) throws CouldNotExtractCalledFunctionsOutOfClassException {
        final CallGraphFunctionInformation callGraphFunctionInformation = functionDataProvider.provideControlFlowInformation(functionIdentifier);
        final List<FunctionIdentifier> calledFunctions = callGraphFunctionInformation.getFunctionsCalledBy();
        return calledFunctions;
    }
}
