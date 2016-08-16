package castendyck.callgraphstrategy.roadmap.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.callgraphstrategy.roadmap.RoadMapCreator;
import castendyck.dependency.Dependency;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import castendyck.roadmap.RoadMap;
import castendyck.roadmap.RoadMapBuilder;
import castendyck.roadmap.RoadSectionBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;
import java.util.stream.Collectors;

public class RoadMapCreatorImpl implements RoadMapCreator {
    private RoadMapBuilder roadMapBuilder;
    private List<VulnerablePoint> vulnerablePoints;

    @Override
    public RoadMap createRoadMap(DependencyGraph dependencyGraph, List<VulnerablePoint> vulnerablePointList) {
        roadMapBuilder = RoadMapBuilder.aRoadMap();
        vulnerablePoints = vulnerablePointList;
        final GraphNode rootNode = dependencyGraph.getRootNode();
        traverseNode(rootNode);

        final RoadMap roadMap = roadMapBuilder.build();
        return roadMap;
    }

    public boolean traverseNode(GraphNode graphNode){
        final List<GraphNode> children = graphNode.getChildren();
        boolean toBeAnalyzed = false;
        for (GraphNode child : children) {
            toBeAnalyzed |= traverseNode(child);
        }

        final RoadSectionBuilder roadSectionBuilder = RoadSectionBuilder.aRoadSection();
        List<VulnerablePoint> vulnerablePointsOfModule = getVulnerablePointsOfThisModule(graphNode);
        if(vulnerablePointsOfModule.size() > 0){
            roadSectionBuilder.withVulnerablePoints(vulnerablePointsOfModule);
            toBeAnalyzed = true;
        }

        if(toBeAnalyzed){
            final Dependency dependency = graphNode.getRelatedDependency();
            final ArtifactIdentifier artifactIdentifier = dependency.getArtifactIdentifier();
            roadSectionBuilder.withArtifactIdentifier(artifactIdentifier);
            roadMapBuilder.withARoadSection(roadSectionBuilder);
        }
        return toBeAnalyzed;
    }

    private List<VulnerablePoint> getVulnerablePointsOfThisModule(GraphNode graphNode) {
        List<VulnerablePoint> vulnerablePointsOfCurrentNode = findVulnerablePointsForCurrentNode(graphNode);
        this.vulnerablePoints.removeAll(vulnerablePointsOfCurrentNode);

        return vulnerablePointsOfCurrentNode;
    }

    private List<VulnerablePoint> findVulnerablePointsForCurrentNode(GraphNode graphNode) {
        final ArtifactIdentifier artifactIdentifier = graphNode.getRelatedDependency().getArtifactIdentifier();
        final List<VulnerablePoint> vulnerablePointsForGraphNode = this.vulnerablePoints.stream()
                .filter(v -> v.getArtifactIdentifier().equals(artifactIdentifier))
                .collect(Collectors.toList());

        return vulnerablePointsForGraphNode;
    }
}
