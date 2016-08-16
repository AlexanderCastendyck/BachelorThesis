package castendyck.dependencygraphing.dependencyregistry;

import castendyck.artifactidentifier.ArtifactIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependentArtifactsBuilder {
    private final List<ArtifactIdentifier> dependentArtifacts;
    private final ArtifactIdentifier artifactIdentifier;

    private DependentArtifactsBuilder(ArtifactIdentifier artifactIdentifier) {
        this.dependentArtifacts = new ArrayList<>();
        this.artifactIdentifier = artifactIdentifier;
    }

    public static DependentArtifactsBuilder anArtifact(ArtifactIdentifier artifactIdentifier) {
        return new DependentArtifactsBuilder(artifactIdentifier);
    }

    public DependentArtifactsBuilder beingUsedBy(ArtifactIdentifier... artifactIdentifiers) {
        final List<ArtifactIdentifier> artifacts = Arrays.asList(artifactIdentifiers);
        dependentArtifacts.addAll(artifacts);
        return this;
    }
    public DependentArtifactsBuilder beingUsedBy(List<ArtifactIdentifier> artifactIdentifiers) {
        dependentArtifacts.addAll(artifactIdentifiers);
        return this;
    }

    public DependentArtifactsBuilder notBeingUsed() {
        dependentArtifacts.clear();
        return this;
    }

    public List<ArtifactIdentifier> build() {
        return dependentArtifacts;
    }

    public ArtifactIdentifier getArtifactIdentifier() {
        return artifactIdentifier;
    }
}
