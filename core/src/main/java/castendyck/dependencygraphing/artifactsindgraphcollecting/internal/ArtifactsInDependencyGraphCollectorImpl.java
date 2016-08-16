package castendyck.dependencygraphing.artifactsindgraphcollecting.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;
import castendyck.dependencygraphing.artifactsindgraphcollecting.ArtifactMatcher;
import castendyck.dependencygraphing.artifactsindgraphcollecting.ArtifactsInDependencyGraphCollector;

import java.util.ArrayList;
import java.util.List;

public class ArtifactsInDependencyGraphCollectorImpl implements ArtifactsInDependencyGraphCollector {
    private List<ArtifactIdentifier> foundArtifacts;
    @Override
    public List<ArtifactIdentifier> collectArtifactsThatAreInDependencyGraph(DependencyGraph dependencyGraph, List<ArtifactIdentifier> artifactsToCheck) {
        this.foundArtifacts = new ArrayList<>();
        final ArtifactMatcher artifactMatcher = artifactsToCheck::contains;
        searchDependencyGraph(dependencyGraph, artifactMatcher);
        return foundArtifacts;
    }

    @Override
    public List<ArtifactIdentifier> collectArtifactsThatMatch(DependencyGraph dependencyGraph, ArtifactMatcher artifactMatcher) {
        this.foundArtifacts = new ArrayList<>();
        searchDependencyGraph(dependencyGraph, artifactMatcher);
        return foundArtifacts;
    }

    private void searchDependencyGraph(DependencyGraph dependencyGraph, ArtifactMatcher artifactMatcher) {
        final GraphNode rootNode = dependencyGraph.getRootNode();
        checkIfNodeContainsAnSearchArtifact(rootNode, artifactMatcher);
    }

    private void checkIfNodeContainsAnSearchArtifact(GraphNode node, ArtifactMatcher artifactMatcher) {
        final ArtifactIdentifier nodeArtifact = node.getRelatedDependency().getArtifactIdentifier();
        if(artifactMatcher.matches(nodeArtifact)){
            foundArtifacts.add(nodeArtifact);
        }

        final List<GraphNode> children = node.getChildren();
        for (GraphNode child : children) {
            checkIfNodeContainsAnSearchArtifact(child, artifactMatcher);
        }
    }
}
