package castendyck.callgraphstrategy.internal;


import castendyck.callgraphstrategy.CallGraphStrategyCalculator;
import castendyck.callgraphstrategy.CallGraphStrategy;
import castendyck.callgraphstrategy.CallGraphStrategyRequestDto;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.roadmap.RoadMap;
import castendyck.callgraphstrategy.roadmap.RoadMapCreator;
import castendyck.callgraphstrategy.roadmap.RoadMapCreatorFactory;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class CallGraphStrategyCalculatorImpl implements CallGraphStrategyCalculator {
    private final RoadMapCreator roadMapCreator;

    public CallGraphStrategyCalculatorImpl() {
        roadMapCreator = RoadMapCreatorFactory.newInstance();
    }

    @Override
    public CallGraphStrategy calculateStrategy(CallGraphStrategyRequestDto callGraphStrategyRequestDto) {
        final DependencyGraph dependencyGraph = callGraphStrategyRequestDto.getDependencyGraph();
        final List<VulnerablePoint> vulnerablePoints = callGraphStrategyRequestDto.getVulnerablePoints();
        final RoadMap roadMap = roadMapCreator.createRoadMap(dependencyGraph, vulnerablePoints);
        final RoadMapGuidedCallGraphStrategyImpl strategy = new RoadMapGuidedCallGraphStrategyImpl(roadMap);
        return strategy;
    }
}
