package castendyck.dependencygraph;

import castendyck.dependency.Dependency;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;

public class GraphNode {
    private final List<GraphNode> children;
    private final Dependency dependency;

    GraphNode(Dependency dependency, List<GraphNode> children) {
        NotNullConstraintEnforcer.ensureNotNull(dependency);
        this.dependency = dependency;
        NotNullConstraintEnforcer.ensureNotNull(children);
        this.children = children;
    }

    public List<GraphNode> getChildren() {
        return children;
    }

    public Dependency getRelatedDependency() {
        return dependency;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
