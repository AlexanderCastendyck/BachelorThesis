package castendyck.callgraph;

import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.dependencygraph.DependencyGraph;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class CallGraphCollectRequestDto {
    private final CallGraphStrategy strategy;
    private final DependencyGraph dependencyGraph;

    public CallGraphCollectRequestDto(CallGraphStrategy strategy, DependencyGraph dependencyGraph) {
        NotNullConstraintEnforcer.ensureNotNull(strategy);
        this.strategy = strategy;
        NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
        this.dependencyGraph = dependencyGraph;
    }


    public CallGraphStrategy getStrategy() {
        return strategy;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }
}
