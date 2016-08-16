package castendyck.callgraphstrategy.roadmap;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.roadmap.RoadMap;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public interface RoadMapCreator {
    RoadMap createRoadMap(DependencyGraph dependencyGraph, List<VulnerablePoint> vulnerablePointList);
}
