package castendyck.dependencygraphing;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.artifactidentifier.ArtifactIdentifierBuilder;
import castendyck.dependency.Dependency;
import castendyck.dependency.DependencyFactory;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraph.GraphNodeBuilder;
import castendyck.vulnerablepoint.VulnerablePoint;
import castendyck.vulnerablepoint.VulnerablePointBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GraphNodeWithVulnerablePointsBuilder {
    private final String name;
    private final List<VulnerablePointBuilder> vulnerablePointBuilders;
    private final List<GraphNodeWithVulnerablePointsBuilder> graphNodeBuilders;

    public GraphNodeWithVulnerablePointsBuilder(String name) {
        this.name = name;
        this.vulnerablePointBuilders = new ArrayList<>();
        this.graphNodeBuilders = new ArrayList<>();
    }

    public static GraphNodeWithVulnerablePointsBuilder aNode(String name) {
        return new GraphNodeWithVulnerablePointsBuilder(name);
    }

    public GraphNodeWithVulnerablePointsBuilder withVulnerablePoints(VulnerablePointBuilder... vulnerablePointBuilders) {
        Arrays.stream(vulnerablePointBuilders)
                .forEach(this::withVulnerablePoint);
        return this;
    }

    private GraphNodeWithVulnerablePointsBuilder withVulnerablePoint(VulnerablePointBuilder vulnerablePointBuilder) {
        vulnerablePointBuilders.add(vulnerablePointBuilder);
        return this;
    }

    public GraphNodeWithVulnerablePointsBuilder withChildren(GraphNodeWithVulnerablePointsBuilder... graphNodeBuilders) {
        Arrays.stream(graphNodeBuilders)
                .forEach(this::withChild);
        return this;
    }

    private GraphNodeWithVulnerablePointsBuilder withChild(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        graphNodeBuilders.add(graphNodeBuilder);
        return this;
    }

    public GraphNodeWithVulnerablePointsBuilder withNoVulnerablePoints() {
        vulnerablePointBuilders.clear();
        return this;
    }

    public GraphNodePackage build() {
        final List<GraphNodePackage> childBuilderResults = graphNodeBuilders.stream()
                .map(GraphNodeWithVulnerablePointsBuilder::build)
                .collect(Collectors.toList());
        final ArtifactIdentifier artifactIdentifier = ArtifactIdentifierBuilder.buildShort("local", name, "1.0");


        final List<VulnerablePoint> vulnerablePoints = vulnerablePointBuilders.stream()
                .map(VulnerablePointBuilder::build)
                .collect(Collectors.toList());
        childBuilderResults.stream()
                .map(p -> p.vulnerablePoints)
                .flatMap(Collection::stream)
                .forEach(vulnerablePoints::add);

        List<GraphNode> children = childBuilderResults.stream()
                .map(p -> p.graphNode)
                .collect(Collectors.toList());
        final Dependency dependency = DependencyFactory.dependencyFor(artifactIdentifier);
        final GraphNode graphNode = GraphNodeBuilder.aGraphNode()
                .withDependency(dependency)
                .withChildren(children)
                .build();

        GraphNodePackage graphNodePackage = new GraphNodePackage(vulnerablePoints, graphNode);
        return graphNodePackage;
    }

    public String getName() {
        return name;
    }

    public class GraphNodePackage {
        private final List<VulnerablePoint> vulnerablePoints;
        private final GraphNode graphNode;

        public GraphNodePackage(List<VulnerablePoint> vulnerablePoints, GraphNode graphNode) {
            this.vulnerablePoints = vulnerablePoints;
            this.graphNode = graphNode;
        }

        public List<VulnerablePoint> getVulnerablePoints() {
            return vulnerablePoints;
        }

        public GraphNode getGraphNode() {
            return graphNode;
        }
    }


}
