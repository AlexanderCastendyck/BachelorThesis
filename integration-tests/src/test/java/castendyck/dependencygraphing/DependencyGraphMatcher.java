package castendyck.dependencygraphing;

import castendyck.dependency.Dependency;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.List;

public class DependencyGraphMatcher extends TypeSafeDiagnosingMatcher<DependencyGraph> {

    private final DependencyGraph expectedDependencyGraph;

    public DependencyGraphMatcher(DependencyGraph dependencyGraph) {
        this.expectedDependencyGraph = dependencyGraph;
    }

    public static DependencyGraphMatcher matches(DependencyGraphTestBuilder dependencyGraphTestBuilder) {
        final DependencyGraph dependencyGraph = dependencyGraphTestBuilder.build();
        return new DependencyGraphMatcher(dependencyGraph);
    }

    @Override
    protected boolean matchesSafely(DependencyGraph dependencyGraph, Description description) {
        final GraphNode rootNode = dependencyGraph.getRootNode();
        final GraphNode expectedRootNode = expectedDependencyGraph.getRootNode();
        matchNodes(rootNode, expectedRootNode);

        return true;
    }

    private void matchNodes(GraphNode node, GraphNode expectedNode) {
        matchDependencies(node, expectedNode);
        matchNumberOfChildren(node, expectedNode);
        matchChildren(node, expectedNode);

    }

    private void matchDependencies(GraphNode node, GraphNode expectedNode) {
        final Dependency dependency = node.getRelatedDependency();
        final Dependency expectedDependency = expectedNode.getRelatedDependency();
        final boolean dependenciesEqual = dependency.equals(expectedDependency);
        if (!dependenciesEqual) {
            throw new AssertionError("Mismatch in nodes' dependencies: Expected: " + expectedDependency + " but got " + dependency);
        }
    }

    private void matchNumberOfChildren(GraphNode node, GraphNode expectedNode) {
        final List<GraphNode> children = node.getChildren();
        final List<GraphNode> expectedChildren = expectedNode.getChildren();
        if (children.size() != expectedChildren.size()) {
            final Dependency dependency = node.getRelatedDependency();
            throw new AssertionError("Mismatch in number of children of " + dependency + ": Expected: " + expectedChildren.size() + " but got " + children.size());
        }
    }

    private void matchChildren(GraphNode node, GraphNode expectedNode) {
        final List<GraphNode> children = node.getChildren();
        final List<GraphNode> expectedChildren = expectedNode.getChildren();
        for (int i = 0; i < expectedChildren.size(); i++) {
            final GraphNode nextExpectedNode = expectedChildren.get(i);
            final GraphNode nextNode = children.get(i);
            matchNodes(nextNode, nextExpectedNode);
        }
    }


    @Override
    public void describeTo(Description description) {

    }
}
