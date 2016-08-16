package castendyck.dependencygraphing.dependencyregistry;

import castendyck.artifactidentifier.ArtifactIdentifier;
import castendyck.dependencygraphing.dependencyregistry.internal.DependencyRegistryImpl;

import java.util.*;

public class DependencyRegistryTestBuilder {
    private final List<DependentArtifactsBuilder> dependentArtifactsBuilders;

    private DependencyRegistryTestBuilder() {
        this.dependentArtifactsBuilders = new ArrayList<>();
    }

    public static DependencyRegistryTestBuilder aDependencyRegistry(){
        return new DependencyRegistryTestBuilder();
    }

    public DependencyRegistryTestBuilder withNoData(){
        dependentArtifactsBuilders.clear();
        return this;
    }

    public DependencyRegistryTestBuilder with(DependentArtifactsBuilder dependentArtifactsBuilder){
        dependentArtifactsBuilders.add( dependentArtifactsBuilder);
        return this;
    }

    public DependencyRegistry build(){
        Map<ArtifactIdentifier, List<ArtifactIdentifier>> map = new HashMap<>();
        for (DependentArtifactsBuilder dependentArtifactsBuilder : dependentArtifactsBuilders) {
            final List<ArtifactIdentifier> dependentArtifacts = dependentArtifactsBuilder.build();
            final ArtifactIdentifier artifactIdentifier = dependentArtifactsBuilder.getArtifactIdentifier();
            map.put(artifactIdentifier, dependentArtifacts);
        }

        return new DependencyRegistryImpl(map);
    }
}
