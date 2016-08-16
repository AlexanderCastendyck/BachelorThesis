package castendyck.dependencygraphing.dependencyregistry;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraphing.dependencyregistry.internal.DependencyGraphToMapMapper;
import castendyck.dependencygraphing.dependencyregistry.internal.DependencyRegistryImpl;
import castendyck.dependencygraph.DependencyGraph;

import java.util.List;
import java.util.Map;

public class DependencyRegistryBuilder {
    private DependencyGraph dependencyGraph;

    public static DependencyRegistryBuilder aDependencyRegistry() {
        return new DependencyRegistryBuilder();
    }

    public DependencyRegistryBuilder initializedWithGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
        return this;
    }

    public DependencyRegistry build() {
        final Map<ArtifactIdentifier, List<ArtifactIdentifier>> dependentArtifactsMap = DependencyGraphToMapMapper.mapToMap(dependencyGraph);
        return new DependencyRegistryImpl(dependentArtifactsMap);
    }

}
