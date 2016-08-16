package castendyck.callgraph;

import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.dependencygraphing.DependencyGraphTestBuilder;
import castendyck.dependencygraph.DependencyGraph;

public class CallGraphCollectorRequestDtoBuilder {

    private DependencyGraphTestBuilder dependencyGraphTestBuilder;
    private CallGraphStrategyBuilder callGraphStrategyBuilder;

    public static CallGraphCollectorRequestDtoBuilder aRequest() {
        return new CallGraphCollectorRequestDtoBuilder();
    }

    public CallGraphCollectorRequestDtoBuilder with(DependencyGraphTestBuilder dependencyGraphTestBuilder) {
        this.dependencyGraphTestBuilder = dependencyGraphTestBuilder;
        return this;
    }
    public CallGraphCollectorRequestDtoBuilder with(CallGraphStrategyBuilder callGraphStrategyBuilder) {
        this.callGraphStrategyBuilder = callGraphStrategyBuilder;
        return this;
    }

    public CallGraphCollectRequestDto build() {
        final DependencyGraph dependencyGraph = dependencyGraphTestBuilder.build();
        final CallGraphStrategy strategy = callGraphStrategyBuilder.build();
        return new CallGraphCollectRequestDto(strategy, dependencyGraph);
    }
}
