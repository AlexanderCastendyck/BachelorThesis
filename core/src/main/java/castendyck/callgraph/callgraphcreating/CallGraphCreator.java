package castendyck.callgraph.callgraphcreating;

import castendyck.callgraph.CallPath;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.StrategyUnexpectedStateException;

import java.util.List;

public interface CallGraphCreator {
    List<CallPath> analyzeControlFlow(CallGraphStrategy strategy) throws StrategyUnexpectedStateException;
}
