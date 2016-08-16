package castendyck.dependencygraphing.dependencyregistry.internal;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependency.Dependency;
import castendyck.dependencygraph.DependencyGraph;
import castendyck.dependencygraph.GraphNode;

import java.util.*;

public final class DependencyGraphToMapMapper {
    private Map<ArtifactIdentifier, Set<ArtifactIdentifier>> artifactToDependingArtifactsMap;

    public static Map<ArtifactIdentifier, List<ArtifactIdentifier>> mapToMap(DependencyGraph dependencyGraph) {
        final DependencyGraphToMapMapper mapper = new DependencyGraphToMapMapper();
        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = mapper.map(dependencyGraph);
        return map;
    }

    public Map<ArtifactIdentifier, List<ArtifactIdentifier>> map(DependencyGraph dependencyGraph) {
        this.artifactToDependingArtifactsMap = new HashMap<>();
        final GraphNode rootNode = dependencyGraph.getRootNode();

        fillNodeIntoMap(rootNode);

        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = mapToMapWithCorrectTypes(artifactToDependingArtifactsMap);
        return map;
    }

    private void fillNodeIntoMap(GraphNode graphNode) {
        ensureNodeStoredInMap(graphNode);

        for (GraphNode child : graphNode.getChildren()) {
            ensureNodeStoredInMap(child);
            storeNodeAsDependingOnChild(graphNode, child);
            fillNodeIntoMap(child);
        }
    }

    private void ensureNodeStoredInMap(GraphNode rootNode) {
        final Dependency dependency = rootNode.getRelatedDependency();
        final ArtifactIdentifier artifactIdentifier = dependency.getArtifactIdentifier();

        if (!artifactToDependingArtifactsMap.containsKey(artifactIdentifier)) {
            final HashSet<ArtifactIdentifier> emptyHashSet = new HashSet<>();
            artifactToDependingArtifactsMap.put(artifactIdentifier, emptyHashSet);
        }
    }

    private void storeNodeAsDependingOnChild(GraphNode graphNode, GraphNode child) {
        final Dependency dependency = graphNode.getRelatedDependency();
        final ArtifactIdentifier artifactIdentifier = dependency.getArtifactIdentifier();

        final Dependency dependencyChild = child.getRelatedDependency();
        final ArtifactIdentifier childArtifactIdentifier = dependencyChild.getArtifactIdentifier();

        final Set<ArtifactIdentifier> dependingArtifacts = artifactToDependingArtifactsMap.get(childArtifactIdentifier);
        dependingArtifacts.add(artifactIdentifier);
    }

    private Map<ArtifactIdentifier, List<ArtifactIdentifier>> mapToMapWithCorrectTypes(Map<ArtifactIdentifier, Set<ArtifactIdentifier>> artifactToDependendingArtifactsMap) {
        Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = new HashMap<>();

        for (Map.Entry<ArtifactIdentifier, Set<ArtifactIdentifier>> entry : artifactToDependendingArtifactsMap.entrySet()) {
            final ArtifactIdentifier key = entry.getKey();
            final Set<ArtifactIdentifier> value = entry.getValue();

            final ArrayList<ArtifactIdentifier> dependingArtifacts = new ArrayList<>(value);
            map.put(key, dependingArtifacts);
        }

        return map;
    }
}
