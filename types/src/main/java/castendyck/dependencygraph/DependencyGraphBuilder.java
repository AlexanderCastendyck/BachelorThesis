package castendyck.dependencygraph;

public class DependencyGraphBuilder {
    private GraphNodeBuilder rootNodeBuilder;

    public static DependencyGraphBuilder aDependencyGraph() {
        return new DependencyGraphBuilder();
    }

    public DependencyGraphBuilder withARootNode(GraphNodeBuilder graphNodeBuilder) {
        this.rootNodeBuilder = graphNodeBuilder;
        return this;
    }

    public DependencyGraph build() {
        final GraphNode rootNode = rootNodeBuilder.build();
        return new DependencyGraph(rootNode);
    }
}
