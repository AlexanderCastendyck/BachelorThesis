package castendyck.dependencygraph;

import castendyck.dependency.Dependency;
import castendyck.dependency.DependencyFactory;

import java.util.ArrayList;
import java.util.List;

public class GraphNodeBuilder {
    private final List<GraphNode> children = new ArrayList<>();
    private Dependency dependency;

    public static GraphNodeBuilder aGraphNode() {
        return new GraphNodeBuilder();
    }

    public GraphNodeBuilder withAetherDependency(org.eclipse.aether.graph.Dependency aetherDependency) {
        final Dependency dependency = DependencyFactory.fromAetherDependency(aetherDependency);
        return withDependency(dependency);
    }

    public GraphNodeBuilder withDependency(Dependency dependency) {
        this.dependency = dependency;
        return this;
    }

    public GraphNodeBuilder withChildren(List<GraphNode> children) {
        this.children.addAll(children);
        return this;
    }

    public GraphNode build() {
        return new GraphNode(dependency, children);
    }
}
