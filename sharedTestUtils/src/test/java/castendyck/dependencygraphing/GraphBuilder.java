package castendyck.dependencygraphing;


import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import de.castendyck.enforcing.NotNullConstraintEnforcer;
import castendyck.vulnerablepoint.VulnerablePoint;

import java.util.List;

public class GraphBuilder {
    private GraphNodeWithVulnerablePointsBuilder graphNodeBuilder;

    public static GraphBuilder aGraph() {
        return new GraphBuilder();
    }

    public GraphBuilder withARootNode(GraphNodeWithVulnerablePointsBuilder graphNodeBuilder) {
        this.graphNodeBuilder = graphNodeBuilder;
        return this;
    }

    public RoadMapPackage build() {
        final GraphNodeWithVulnerablePointsBuilder.GraphNodePackage build = graphNodeBuilder.build();

        final GraphNode root = build.getGraphNode();
        final DependencyGraph dependencyGraph = new DependencyGraph(root);
        final List<VulnerablePoint> vulnerablePoints = build.getVulnerablePoints();
        final RoadMapPackage roadMapPackage = new RoadMapPackage(vulnerablePoints, dependencyGraph);
        return roadMapPackage;
    }

    public class RoadMapPackage {
        private final List<VulnerablePoint> vulnerablePoints;
        private final DependencyGraph dependencyGraph;

        public RoadMapPackage(List<VulnerablePoint> vulnerablePoints, DependencyGraph dependencyGraph) {
            NotNullConstraintEnforcer.ensureNotNull(vulnerablePoints);
            this.vulnerablePoints = vulnerablePoints;
            NotNullConstraintEnforcer.ensureNotNull(dependencyGraph);
            this.dependencyGraph = dependencyGraph;
        }

        public List<VulnerablePoint> getVulnerablePoints() {
            return vulnerablePoints;
        }

        public DependencyGraph getDependencyGraph() {
            return dependencyGraph;
        }
    }
}
