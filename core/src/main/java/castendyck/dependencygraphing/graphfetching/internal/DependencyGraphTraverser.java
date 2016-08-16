package castendyck.dependencygraphing.graphfetching.internal;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraph.GraphNodeBuilder;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

import java.util.List;
import java.util.stream.Collectors;

public class DependencyGraphTraverser {

    public DependencyGraph getResultingGraph(DependencyNode rootDependency) {
        final GraphNode root = mapToGraphNode(rootDependency);

        return new DependencyGraph(root);
    }

    private GraphNode mapToGraphNode(DependencyNode dependencyNode) {
        final List<DependencyNode> childrenDependencyNodes = dependencyNode.getChildren();
        final List<GraphNode> graphNodes = childrenDependencyNodes.stream()
                .map(this::mapToGraphNode)
                .collect(Collectors.toList());

        final Dependency aetherDependency = dependencyNode.getDependency();
        final GraphNode graphNode = GraphNodeBuilder.aGraphNode()
                .withAetherDependency(aetherDependency)
                .withChildren(graphNodes)
                .build();
        return graphNode;
    }
}
