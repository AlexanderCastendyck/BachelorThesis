package castendyck.dependencygraphing;

import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;

public class DependencyGraphTestBuilder {

    private GraphNodeTestBuilder rootNodeBuilder;

    public static DependencyGraphTestBuilder aDependencyGraph() {
        return new DependencyGraphTestBuilder();
    }

    public DependencyGraphTestBuilder withARootNode(GraphNodeTestBuilder graphNodeBuilder) {
        this.rootNodeBuilder = graphNodeBuilder;
        return this;
    }

    public DependencyGraph build() {
        final GraphNode rootNode = rootNodeBuilder.build();
        return new DependencyGraph(rootNode);
    }
}
