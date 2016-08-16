package castendyck.callgraphstrategy.roadmap;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraphing.GraphBuilder;
import castendyck.roadmap.RoadMap;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class When {
    private final GraphBuilder.RoadMapPackage roadMapPackage;

    public When(GraphBuilder.RoadMapPackage roadMapPackage) {
        this.roadMapPackage = roadMapPackage;
    }

    public Then thenRoadMapShouldHave() {
        final RoadMapCreator roadMapCreator = RoadMapCreatorFactory.newInstance();
        final DependencyGraph dependencyGraph = roadMapPackage.getDependencyGraph();
        final List<VulnerablePoint> vulnerablePoints = roadMapPackage.getVulnerablePoints();
        final RoadMap roadMap = roadMapCreator.createRoadMap(dependencyGraph, vulnerablePoints);
        return new Then(roadMap);
    }
}
