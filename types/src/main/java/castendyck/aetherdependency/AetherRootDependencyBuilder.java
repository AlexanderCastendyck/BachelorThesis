package castendyck.aetherdependency;

import castendyck.artifactidentifier.ArtifactIdentifier;

import java.util.List;

public class AetherRootDependencyBuilder {
    private ArtifactIdentifier artifactIdentifier;
    private List<AetherLeafDependency> aetherLeafDependencies;

    public static AetherRootDependencyBuilder anAetherRootDependency() {
        return new AetherRootDependencyBuilder();
    }

    public AetherRootDependencyBuilder forArtifact(ArtifactIdentifier artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
        return this;
    }

    public AetherRootDependencyBuilder withDependencies(List<AetherLeafDependency> aetherDependencies){
        aetherLeafDependencies = aetherDependencies;
        return this;
    }

    public AetherRootDependency build() {
        AetherRootDependency aetherRootDependency = new AetherRootDependency(artifactIdentifier, aetherLeafDependencies);
        return aetherRootDependency;
    }
}
