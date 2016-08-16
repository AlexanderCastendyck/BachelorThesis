package castendyck.aetherdependency;

import castendyck.aetherdependency.internal.ArtifactIdentifierToRealAetherDependencyMapper;
import castendyck.artifactidentifier.ArtifactIdentifier;
import de.castendyck.enforcing.NotNullConstraintEnforcer;

import java.util.List;

public class AetherRootDependency {
    private final ArtifactIdentifier artifactIdentifier;
    private final List<AetherLeafDependency> aetherLeafDependencies;

    AetherRootDependency(ArtifactIdentifier artifactIdentifier, List<AetherLeafDependency> aetherLeafDependencies) {
        NotNullConstraintEnforcer.ensureNotNull(artifactIdentifier);
        this.artifactIdentifier = artifactIdentifier;
        NotNullConstraintEnforcer.ensureNotNull(aetherLeafDependencies);
        this.aetherLeafDependencies = aetherLeafDependencies;
    }

    public ArtifactIdentifier getArtifactIdentifier() {
        return artifactIdentifier;
    }

    public List<AetherLeafDependency> getAetherLeafDependencies() {
        return aetherLeafDependencies;
    }

    public org.eclipse.aether.graph.Dependency getRealAetherDependency() {
        final org.eclipse.aether.graph.Dependency realAetherDependency = ArtifactIdentifierToRealAetherDependencyMapper.getRealAetherDependency(artifactIdentifier);
        return realAetherDependency;
    }
}
