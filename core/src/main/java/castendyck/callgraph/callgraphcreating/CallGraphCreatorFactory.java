package castendyck.callgraph.callgraphcreating;

import castendyck.callgraph.callgraphcreating.internal.CallGraphCreatorImpl;
import castendyck.callgraph.callgraphcreating.internal.DirectionForwardImpl;
import castendyck.callgraph.callgraphcreating.internal.DirectionReverseImpl;
import castendyck.callgraph.functiondata.FunctionDataProvider;

public class CallGraphCreatorFactory {

    public static CallGraphCreator newInstanceForCallGraphAnalysis(FunctionDataProvider functionDataProvider) {
        final DirectionForwardImpl direction = new DirectionForwardImpl(functionDataProvider);
        return new CallGraphCreatorImpl(direction);
    }
    public static CallGraphCreator newInstanceForReversedCallGraphAnalysis(FunctionDataProvider functionDataProvider) {
        final DirectionReverseImpl direction = new DirectionReverseImpl(functionDataProvider);
        return new CallGraphCreatorImpl(direction);
    }
}
