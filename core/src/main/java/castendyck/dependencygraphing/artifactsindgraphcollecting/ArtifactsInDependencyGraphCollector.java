package castendyck.dependencygraphing.artifactsindgraphcollecting;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraph.DependencyGraph;

import java.util.List;

public interface ArtifactsInDependencyGraphCollector {

    List<ArtifactIdentifier> collectArtifactsThatAreInDependencyGraph(DependencyGraph dependencyGraph, List<ArtifactIdentifier> artifactsToCheck);

    List<ArtifactIdentifier> collectArtifactsThatMatch(DependencyGraph dependencyGraph, ArtifactMatcher artifactMatcher);
}
