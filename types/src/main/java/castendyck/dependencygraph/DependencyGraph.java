package castendyck.dependencygraph;

import de.castendyck.enforcing.NotNullConstraintEnforcer;

public class DependencyGraph {
    private final GraphNode root;

    public DependencyGraph(GraphNode root) {
        NotNullConstraintEnforcer.ensureNotNull(root);
        this.root = root;
    }

    public GraphNode getRootNode() {
        return root;
    }
}
