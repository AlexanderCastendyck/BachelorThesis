package castendyck.callgraphstrategy;

import castendyck.callgraphstrategy.internal.CallGraphStrategyCalculatorImpl;
import castendyck.dependencygraphing.GraphBuilder;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class When {
    private final GraphBuilder.RoadMapPackage roadMapPackage;

    public When(GraphBuilder.RoadMapPackage roadMapPackage) {
        this.roadMapPackage = roadMapPackage;
    }

    public When(GraphBuilder graphBuilder) {
        roadMapPackage = graphBuilder.build();
    }

    public Then whenStrategyIsCreated() {
        final CallGraphStrategyCalculatorImpl controlFlowStrategyCalculator = CallGraphStrategyCalculatorFactory.newInstance();
        final DependencyGraph dependencyGraph = roadMapPackage.getDependencyGraph();
        final List<VulnerablePoint> vulnerablePoints = roadMapPackage.getVulnerablePoints();
        final CallGraphStrategyRequestDto callGraphStrategyRequestDto = new CallGraphStrategyRequestDto(dependencyGraph, vulnerablePoints);
        final CallGraphStrategy strategy = controlFlowStrategyCalculator.calculateStrategy(callGraphStrategyRequestDto);
        return new Then(strategy);
    }
}
